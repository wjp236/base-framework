package com.platform.base.framework.trunk.core.interceptor;

import com.platform.base.frramework.trunk.util.text.UUIDUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ControllerInterceptor {

    private Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);

    /**
     * @Description 指定切入点匹配表达式，注意它是以方法的形式进行声明的。 即切点集合是：com.ecej包下所有类所有方法
     *              如果要设置多个切点可以使用 || 拼接
     */
    @Pointcut("execution(* com.platform..controller.web.*(..)) && @within(org.springframework.stereotype.Controller) && !execution(* com.platform.base.frramework..*(..))")
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

        if (MDC.get("requestId") == null) {
            String id = UUIDUtils.randomUUID().toString();
            /* 如果没有reguestId就重新生成一个,这种情况可能会发生漏传的情况,需要在开始就要控制好 */
            MDC.put("requestId", id);//
            logger.debug("新请求生成请求唯一ID:[{}]", id);
        }
    }

    /**
     * @Description 后置通知
     * @param jp
     * @param result
     */
    @AfterReturning(value = "anyMethod()", returning = "result")
    public void doAfter(JoinPoint jp, Object result) {

        /* 线程结束后需要清除,否则当前线程会一直占用这个requestId值 */
        MDC.remove("requestId");
    }

    /**
     * @Description 异常通知
     * @param jp
     */
    @AfterThrowing(value = "anyMethod()")
    public void doThrow(JoinPoint jp) {

    }
}
