package com.platform.base.framework.trunk.core.interceptor;

import com.platform.base.framework.trunk.core.exception.ControllerException;
import com.platform.base.framework.trunk.core.exception.DaoException;
import com.platform.base.framework.trunk.core.exception.ServiceException;
import com.platform.base.framework.trunk.core.exception.utils.ErrorBuilderTool;
import com.platform.base.framework.trunk.model.common.Response;
import com.platform.base.frramework.trunk.util.constants.TrunkConstans;
import com.platform.base.frramework.trunk.util.json.JsonUtil;
import com.platform.base.frramework.trunk.util.protocol.ProtocolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  REST Exception union handler, also it can send alarm message.
 * Created by mylover on 4/22/16.
 */
public class ExceptionHandler implements HandlerExceptionResolver {

    protected static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);


    @Override
    public ModelAndView resolveException(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception e
    ) {
        logger.info("@Catch Exception: [" + e.getClass().getName() + "]");
        handleStackTraceElement(e);
        ModelAndView mv = null;
        if (e instanceof ControllerException) {
            // Action异常和错误, 直接给用户提示
            ControllerException xye = (ControllerException) e;
            logger.info("Controller Error: " + xye.getErrorCode() + "|" + xye.getErrorMsg());
            logger.info("Controller Custom: " + xye.getCustomMsg());
            mv = getModelAndView(request, new Response(xye.getErrorCode(), xye.getErrorMsg()));
        } else if (e instanceof ServiceException) {
            // 业务异常和错误, 直接给用户提示
            ServiceException xye = (ServiceException) e;
            logger.info("Service Error: " + xye.getErrorCode() + "|" + xye.getErrorMsg());
            logger.info("Service Custom: " + xye.getCustomMsg());
            String req = xye.getErrorMsg();
            if (req == null || req.equals("null") || req.equals("")) {
                req = xye.getCustomMsg();
            }
            mv = getModelAndView(request, new Response(xye.getErrorCode(), req));
        } else if (e instanceof DaoException) {
            // 数据库连接、SQL语句、存储过程执行异常或者错误, 这种错误可以给用户明确提示或者包装后提示
            DaoException xye = (DaoException) e;
            logger.info("Dao Error: " + xye.getErrorCode() + "|" + xye.getErrorMsg());
            logger.info("Dao Custom: " + xye.getCustomMsg());
            mv = getModelAndView(request, new Response(xye.getErrorCode(), xye.getErrorMsg()));
        } else {
            //未知错误
            logger.error("ExceptionHandler: ", e);
            mv = getModelAndView(request, handleUnknownResponse(e));
        }

        logger.info("----------------------------[End]-------------------------------\n\n");
        return mv;
    }

    private Response handleUnknownResponse(Exception e) {
        String errorMsg = e.getMessage();
        if (e instanceof org.springframework.web.HttpRequestMethodNotSupportedException) {
            return new Response("000001", String.format(ErrorBuilderTool.buildError("000001")));
        } else if (errorMsg != null && errorMsg.length() == 6) {
            return new Response(errorMsg, String.format(ErrorBuilderTool.buildError(errorMsg)));
        }
        return new Response("999999", String.format(ErrorBuilderTool.buildError("999999")));
    }

    private ModelAndView getModelAndView(final HttpServletRequest request, Object obj) {
        if (ProtocolUtil.doCheckAccept(request) == 1) {
            return new ModelAndView(TrunkConstans.JSON_VIEW_NAME, JsonUtil.objToMap4Obj(obj));
        }
        return new ModelAndView(TrunkConstans.XML_VIEW_NAME, TrunkConstans.ROOT_ELEMENT, obj);
    }

    private void handleStackTraceElement(Exception ex) {
        StackTraceElement[] ste = ex.getStackTrace();
        if (ste != null) {
            for (int i = 0; i < ste.length; i++) {
                StackTraceElement cause = ste[i];
                if (i == 0) {
                    logger.info("Exception Location [ClassName: " + cause.getClassName() + ", MethodName: "
                            + cause.getMethodName() + ", LineNumber: " + cause.getLineNumber() + "]");
                    break;
                }
            }
        }
    }
}
