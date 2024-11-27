package restfulapi.three.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;

@Component
public class RateLimiterInterceptor implements HandlerInterceptor {

    private final Bucket bucket;

    public RateLimiterInterceptor() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(5)               
                .refillGreedy(5, Duration.ofMinutes(1))  
                .build();

        this.bucket = Bucket.builder()
                .addLimit(limit) 
                .build();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);  // Try to consume 1 token
        if (probe.isConsumed()) {
            return true; // Continue with the request
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 429 Too Many Requests
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Rate limit exceeded. Please try again later.\"}");
            return false; // Don't continue with the request
        }
    }
}
