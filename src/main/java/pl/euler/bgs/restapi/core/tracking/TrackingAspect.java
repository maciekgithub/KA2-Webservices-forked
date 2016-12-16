package pl.euler.bgs.restapi.core.tracking;

import javaslang.control.Try;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Arrays;

/**
 * Aspect for logging execution of @Timed methods.
 */
@Aspect
public class TrackingAspect {
    private final Logger log = LoggerFactory.getLogger("details-tracking");

    @Pointcut("within(pl.euler.bgs.restapi..*) && @annotation(pl.euler.bgs.restapi.core.tracking.Tracked)")
    public void trackingPointcut() {
    }

    @AfterThrowing(pointcut = "trackingPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'", joinPoint.getSignature().getDeclaringTypeName(),
                  joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL", e.getMessage(), e);
    }

    @Around("trackingPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean errorsOnly = logErrorsOnly(joinPoint);
        if (log.isInfoEnabled() && !errorsOnly) {
            log.info("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                     joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isInfoEnabled() && !errorsOnly) {
                log.info("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                         joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                      joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }

    /**
     * Determine based on annotation whether track errors only.
     */
    private boolean logErrorsOnly(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        return Try.of(() -> joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes))
                .map(method -> AnnotationUtils.getAnnotation(method, Tracked.class))
                .map(Tracked::errorsOnly)
                .orElse(Boolean.FALSE);
    }

}
