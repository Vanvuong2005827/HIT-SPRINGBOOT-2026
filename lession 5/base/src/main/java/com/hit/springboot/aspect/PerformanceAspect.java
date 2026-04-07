package com.hit.springboot.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Around("@annotation(com.hit.springboot.annotation.TrackPerformance)")
    public Object track(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long elapsed = System.currentTimeMillis() - start;
        if (elapsed > 500) {
            log.warn("⚠️ SLOW [{}]: {}ms", pjp.getSignature().getName(), elapsed);
        } else {
            log.debug("⏱ [{}]: {}ms", pjp.getSignature().getName(), elapsed);
        }
        return result;
    }
}
