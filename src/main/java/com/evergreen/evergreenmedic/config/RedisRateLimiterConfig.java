package com.evergreen.evergreenmedic.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
@Slf4j
public class RedisRateLimiterConfig {

    @Autowired
    private Environment env;

    //    This is how key resolver saves data in redis
//    1) "request_rate_limiter.{0:0:0:0:0:0:0:1}.timestamp"
//    2) "request_rate_limiter.{0:0:0:0:0:0:0:1}.tokens"
    @Bean
    public KeyResolver ipKeyResolver() {
        if ("default".equals(env.getProperty("spring.profiles.active"))) {
            return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
        } else if ("dev".equals(env.getProperty("spring.profiles.active"))) {
            return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
        } else if ("qa".equals(env.getProperty("spring.profiles.active"))) {
            return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
        } else if ("production".equals(env.getProperty("spring.profiles.active"))) {
            return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
        } else {
            return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());

        }
    }


    //    10 requests per second
    @Bean
    public RedisRateLimiter defaultRedisRateLimiter() {
        if ("default".equals(env.getProperty("spring.profiles.active"))) {
            return new RedisRateLimiter(10, 10, 1);
        } else if ("dev".equals(env.getProperty("spring.profiles.active"))) {
            return new RedisRateLimiter(10, 10, 1);
        } else if ("qa".equals(env.getProperty("spring.profiles.active"))) {
            return new RedisRateLimiter(10, 10, 1);
        } else if ("production".equals(env.getProperty("spring.profiles.active"))) {
            return new RedisRateLimiter(10, 10, 1);
        } else {
            return new RedisRateLimiter(10, 10, 1);

        }

    }


    //    1 request per second
    @Bean
    @Primary
    public RedisRateLimiter customRedisRateLimiter() {
        return new RedisRateLimiter(1, 60, 60);
    }

}
