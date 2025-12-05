package com.zepox.EcommerceWebApp.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

//    CONTROLLER LAYER - INFO level (detailed)
    @Around("execution(* com.zepox.EcommerceWebApp.controller..*(..))")
    public Object logControllerLayer(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();

        log.info("🌐 [CONTROLLER] Entering: {}", method);
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long time = System.currentTimeMillis() - start;
            log.info("✅ [CONTROLLER] Success: {} ({}ms)", method, time);
            return result;
        } catch (Exception e) {
            long time = System.currentTimeMillis() - start;
            log.error("❌ [CONTROLLER] Failed: {} ({}ms) - {}", method, time, e.getMessage());
            throw e;
        }
    }

//    SERVICE LAYER - DEBUG level (less detailed)
    @Around("execution(* com.zepox.EcommerceWebApp.service..*(..))")
    public Object logServiceLayer(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();

        log.debug("⚙️ [SERVICE] Entering: {}", method);

        try {
            Object result = joinPoint.proceed();
            log.debug("✅ [SERVICE] Success: {}", method);
            return result;
        } catch (Exception e) {
            log.error("❌ [SERVICE] Failed: {} - {}", method, e.getMessage());
            throw e;
        }
}

//    REPOSITORY LAYER - TRACE level (minimal)
    @Before("execution(* com.zepox.EcommerceWebApp.repository..*(..))")
    public void logRepositoryLayer(JoinPoint joinPoint) {
        log.trace("💾 [REPOSITORY] Calling: {}", joinPoint.getSignature().toShortString());
    }

//    LOG ALL EXCEPTIONS CENTRALLY
    @AfterThrowing(
            pointcut = """
                    (
                    execution(* com.zepox.EcommerceWebApp.controller..*(..)) ||
                    execution(* com.zepox.EcommerceWebApp.service..*(..)) ||
                    execution(* com.zepox.EcommerceWebApp.repository..*(..))
                    )""",
            throwing = "exception"
    )
    public void logAllExceptions(JoinPoint joinPoint, Throwable exception) {
        log.error("💥 EXCEPTION in {}: {} - {}",
                joinPoint.getSignature().toShortString(),
                exception.getClass().getSimpleName(),
                exception.getMessage());
    }
}
