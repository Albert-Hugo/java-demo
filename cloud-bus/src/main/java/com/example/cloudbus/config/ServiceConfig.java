package com.example.cloudbus.config;

import com.ido.tracing.CallingContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Ido
 * @date 2021/4/26 14:24
 */
@Aspect
@Configuration
public class ServiceConfig {

    /**
     * Controller层切点
     */
    @Pointcut("within(com.example.cloudbus.service..*)")
    public void controllerAspect() {
    }


    @Around("controllerAspect() ")
    public Object beforeOp(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CallingContext c = new CallingContext(signature.getName(),"com.example.cloudbus");
        c.startContext();
        Object ret;
        try {

            ret = joinPoint.proceed();
        } catch (Exception ex) {
            throw ex;
        } finally {
            c.endContext();
        }

        return ret;


    }
}
