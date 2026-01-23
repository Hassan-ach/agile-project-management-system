package com.ensa.agile.infrastructure.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Around("@annotation(com.ensa.agile.domain.global.annotation.Loggable) || "
            + "@within(com.ensa.agile.domain.global.annotation.Loggable)")
    public Object
    logUseCase(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        System.out.println("Entering method: " + methodName + " of " +
                           className);

        try {
            Object result = joinPoint.proceed();
            System.out.println("Exiting method: " + methodName + " of " +
                               className);
            return result;
        } catch (Throwable throwable) {
            System.out.println("Exception in method: " + methodName + " of " +
                               className +
                               " with message: " + throwable.getMessage());
            throw throwable;
        }
    }
}
