package com.platform.base.framework.trunk.core.interceptor;

import com.platform.base.framework.trunk.core.exception.ServiceException;
import com.platform.base.framework.trunk.core.exception.utils.TrunkError;
import com.platform.base.framework.trunk.model.dto.BaseResultDto;
import com.platform.base.frramework.trunk.util.constants.TrunkConstans;
import com.platform.base.frramework.trunk.util.ip.InetUtils;
import com.platform.base.frramework.trunk.util.text.UUIDUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

@Component
@Aspect
public class ServiceInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ServiceInterceptor.class);

	private static final String localIP = InetUtils.getLocalIp();

	/**
	 * @Description 指定切入点匹配表达式，注意它是以方法的形式进行声明的。 即切点集合是：com.ecej包下所有类所有方法
	 *              如果要设置多个切点可以使用 || 拼接
	 */
	@Pointcut("execution(* com.platform..service..*(..)) && @within(org.springframework.stereotype.Service) && !execution(* com.platform.base.frramework.*(..))")
	public void anyMethod() {

		// 切点使用与任何方法

	}

	/**
	 * @Description 前置通知 在切点方法集合执行前，执行前置通知
	 * @param jp
	 * @throws Exception
	 */
	@Before(value = "anyMethod()")
	public void doBefore(JoinPoint jp) throws Exception {
        // 调用方法的业务参数，做基础的合法性校验
        Object[] args = jp.getArgs();
        logger.info(String.format("开始，调用类为：%s,方法为：%s", jp.getTarget()
                .getClass(), jp.getSignature().getName()));
        for (int i = 0; i < args.length; i++) {
            logger.info(String.format("传入参数:%s", args[i]));
        }

	}

	/**
	 * @Description 后置通知
	 * @param jp
	 * @param result
	 */
	@AfterReturning(value = "anyMethod()", returning = "result")
	public void doAfter(JoinPoint jp, Object result) {
        logger.info("结束，调用方法为：" + jp.getTarget().getClass() + "结果为：" + result);
        MDC.remove("requestId");
	}

	/**
	 * @Description 环绕通知（ ##环绕通知的方法中一定要有ProceedingJoinPoint 参数,与
	 *              Filter中的doFilter方法类似） 异常统一捕获
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	@Around(value = "anyMethod()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

		logger.debug("Service拦截器开始---->");

		if (MDC.get(TrunkConstans.REQUESTID) == null) {
			/* 如果没有reguestId就重新生成一个,这种情况可能会发生漏传的情况,需要在开始就要控制好 */
			MDC.put(TrunkConstans.REQUESTID, UUIDUtils.randomUUID().toString());//
		}

        Object result = null;
        BaseResultDto<String> baseRet = new BaseResultDto<String>();
        try {
            result = pjp.proceed();// result的值就是被拦截方法的返回值
            PropertyDescriptor systemFlowId = new PropertyDescriptor("systemFlowId", result.getClass());
            Method systemFlowIdMethod = systemFlowId.getWriteMethod();// 获得写方法
            systemFlowIdMethod.invoke(result, MDC.get(TrunkConstans.REQUESTID));
        } catch (ServiceException se) {
            logger.error(String.format(String.format("服务系统异常: 异常编号:%s，异常信息:%s",
                    se.getErrorCode(), se.getMessage()), se));
            baseRet.setRetCode(se.getErrorCode());
            baseRet.setRetMsg(se.getMessage());
            return baseRet;
        } catch (Exception e) {
            logger.error(String.format("系统异常:异常编号:%s,异常消息:%s",
                    TrunkError.SYSTEM_ERROR.code(),
                    TrunkError.SYSTEM_ERROR.getText()), e);
            baseRet.setRetCode(TrunkError.SYSTEM_ERROR.code());
            baseRet.setRetMsg(TrunkError.SYSTEM_ERROR.getText());
            return baseRet;
        }

        logger.debug("Service拦截器,服务结束---->");
		return result;
	}

	/**
	 * @Description 异常通知
	 * @param jp
	 */
    @AfterThrowing(value = "anyMethod()", throwing = "e")
	public void doThrow(JoinPoint jp, ServiceException e) {

	}

}
