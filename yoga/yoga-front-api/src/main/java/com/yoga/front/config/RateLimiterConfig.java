package com.yoga.front.config;

import io.github.bucket4j.core.Bandwidth;
import io.github.bucket4j.core.Bucket;
import io.github.bucket4j.core.Bucket4j;
import io.github.bucket4j.core.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public Bucket4j buckets() {
        Bandwidth limit = Bandwidth.classic(100, Duration.ofMinutes(1));
        Refill refill = Refill.intervally(100, Duration.ofMinutes(1));
        
        Bucket4j buckets = new Bucket4j();
        
        buckets.addBucket("user_api", Bucket.builder()
                .addLimit(limit)
                .addRefill(refill)
                .build());
        
        buckets.addBucket("booking_api", Bucket.builder()
                .addLimit(Bandwidth.classic(50, Duration.ofMinutes(1)))
                .addRefill(Refill.intervally(50, Duration.ofMinutes(1)))
                .build());
        
        return buckets;
    }
}
