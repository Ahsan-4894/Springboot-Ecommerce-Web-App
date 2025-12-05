package com.zepox.EcommerceWebApp.aspect;

import com.zepox.EcommerceWebApp.annotation.RateLimit;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Aspect
@RequiredArgsConstructor
public class RateLimitAspect {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private final HttpServletRequest request;

    @Around("@annotation(rateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {

        String key = getClientIdentifier() + ":" + joinPoint.getSignature().toShortString();

        Bucket bucket = cache.computeIfAbsent(key, k -> createBucket(rateLimit));

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            return joinPoint.proceed();
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Rate limit exceeded. Try again in " + waitForRefill + " seconds"
            );
        }
    }

    private Bucket createBucket(RateLimit rateLimit) {
        Bandwidth limit = Bandwidth.classic(
                rateLimit.requests(),
                Refill.intervally(rateLimit.requests(), Duration.ofSeconds(rateLimit.duration()))
        );
        return Bucket.builder().addLimit(limit).build();
    }

    private String getClientIdentifier() {
        return request.getRemoteAddr();
    }
}
