package com.platform.base.framework.trunk.core.common;

import com.platform.base.framework.trunk.core.exception.ControllerException;
import com.platform.base.framework.trunk.core.exception.utils.ErrorBuilderTool;
import com.platform.base.frramework.trunk.util.constants.TrunkConstans;
import com.platform.base.frramework.trunk.util.json.FastJsonUtils;
import com.platform.base.frramework.trunk.util.protocol.ProtocolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by mylover on 13/11/2016.
 */
public abstract class AbstractController implements Serializable {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    public AbstractController() {
    }

    /**
     * distance of up or down
     */
    public static final int MARGIN_0 = 0;
    public static final int MARGIN_1 = 1;
    public static final int MARGIN_2 = 2;

    /**
     * Sub class must be Override
     *
     * @return
     */
    protected abstract String getClassName();

    public enum DATAGRAM_TYPE {
        XML {
            public String getValue() {
                return "XML";
            }
        },
        JSON {
            public String getValue() {
                return "JSON";
            }
        };
        public abstract String getValue();
    }

    public DATAGRAM_TYPE doCheckContentType(HttpServletRequest request) {
        return ProtocolUtil.doCheckContentType(request) == 1 ? DATAGRAM_TYPE.JSON
                : DATAGRAM_TYPE.XML;
    }

    public DATAGRAM_TYPE doCheckAccept(HttpServletRequest request) {
        return ProtocolUtil.doCheckAccept(request) == 1 ? DATAGRAM_TYPE.JSON
                : DATAGRAM_TYPE.XML;
    }

    /**
     * request parameter transfer object by parse json.
     *
     * @param type
     * @param key
     * @param body
     * @return
     * @throws Exception
     */
    protected Object parser(DATAGRAM_TYPE type, String key, String body)
            throws Exception {
        return null;
    }

    /**
     * @param key
     * @param body
     * @return
     * @throws Exception
     */
    protected final Object postRequest(HttpServletRequest request, String key, String body) throws ControllerException {
        try {
            logger.info("@Start parse body... [{}]", key);
            Object obj = parser(doCheckContentType(request), key, body);
            logger.info("@End parse body [{}]", obj == null ? "null" : obj.getClass().getName());
            return obj;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ControllerException(ErrorBuilderTool.buildError("000001"));
        }
    }

    /**
     * @param request
     * @param obj
     * @return
     */
    protected final ModelAndView postResponse(HttpServletRequest request,
                                              Map<String, ?> obj) {
        DATAGRAM_TYPE dt = doCheckAccept(request);
        logger.info("@Response Accept: {}", dt.getValue());
        if (dt == DATAGRAM_TYPE.JSON) {
            logger.info("\n\n" + FastJsonUtils.toJSONString(obj) + "\n\n");
            return new ModelAndView(TrunkConstans.JSON_VIEW_NAME, obj);
        }
        return new ModelAndView(TrunkConstans.XML_VIEW_NAME, TrunkConstans.ROOT_ELEMENT, obj);
    }

    /**
     * print start tag margin 1 with up.
     *
     * @param tagName
     * @return
     */
    public void printStartTag(String tagName) {
        logger.info("@Controller");
        printStartTag(tagName, MARGIN_0);
    }

    /**
     * print start tag
     *
     * @param tagName
     * @param space
     * @return
     */
    public void printStartTag(String tagName, int space) {
        StringBuilder sb = new StringBuilder();
        appendSpace(sb, space);
        sb.append("----------------------------[").append(tagName).append(" Start").append(
                "]-------------------------------");
        logger.info(sb.toString());
    }


    /**
     * print end tag
     *
     * @param tagName
     * @return
     */
    public final void printEndTag(String tagName) {
        printEndTag(tagName, MARGIN_1);
    }

    /**
     * print end tag
     *
     * @param tagName
     * @param space
     * @return
     */
    public final void printEndTag(String tagName, int space) {
        StringBuilder sb = new StringBuilder("----------------------------[").append(tagName).append(" End").append(
                "]-------------------------------");
        appendSpace(sb, space);
        logger.info(sb.toString());
    }

    public void appendSpace(StringBuilder sb, int space) {
        switch (space) {
            case MARGIN_0:
                break;
            case MARGIN_1:
                sb.append("\r\n");
                break;
            case MARGIN_2:
                sb.append("\r\n\r\n");
                break;
            default:
                sb.append("\r\n");
                break;
        }
    }

    /**
     * print http packet
     *
     * @param request
     * @param body
     */
    protected final void printHttpPacket(HttpServletRequest request, String body) {
        logger.info(ProtocolUtil.getHttpRequestPacket(request, body));
    }


}
