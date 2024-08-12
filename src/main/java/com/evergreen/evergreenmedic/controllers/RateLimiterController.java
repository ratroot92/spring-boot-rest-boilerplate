package com.evergreen.evergreenmedic.controllers;


import com.evergreen.evergreenmedic.dtos.requests.ActivateRouteBucket;
import com.evergreen.evergreenmedic.dtos.requests.CreateRouteRateLimiterReq;
import com.evergreen.evergreenmedic.dtos.response.ActivateRouteBucketResponse;
import com.evergreen.evergreenmedic.entities.RouteRateLimiterEntity;
import com.evergreen.evergreenmedic.services.RateLimiterService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/rate-limiter")
@Slf4j
public class RateLimiterController {

    private Bucket clientBucket;

    private final RateLimiterService rateLimiterService;

    public RateLimiterController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping("/route-rate-limiter")
    public ResponseEntity<RouteRateLimiterEntity> createRouteRateLimiterConfig(@RequestBody CreateRouteRateLimiterReq createRouteRateLimiterReq) {
        return ResponseEntity.status(HttpStatus.OK).body(rateLimiterService.createRouteRateLimiter(createRouteRateLimiterReq));
    }

    @GetMapping("/route-rate-limiter")
    public ResponseEntity<List<RouteRateLimiterEntity>> getRouteRateLimiterConfigs() {
        return ResponseEntity.status(HttpStatus.OK).body(rateLimiterService.getAllRouteRateLimiterConfigs());
    }

    @GetMapping("/route-rate-limiter/{id}")
    public ResponseEntity<Optional<RouteRateLimiterEntity>> getRouteRateLimiterConfigs(@PathVariable("id") Short id) {
        return ResponseEntity.status(HttpStatus.OK).body(rateLimiterService.getRouteRateLimiterById(id));
    }


    @GetMapping("/generate-token")
    public ResponseEntity<String> getRateLimiter() {
        // Give me 5 tokens for a duration of one minute
        Refill refill = Refill.of(5, Duration.ofMinutes(1));
        clientBucket = Bucket4j.builder().addLimit(Bandwidth.classic(5, refill)).build();
        return ResponseEntity.status(HttpStatus.OK).body("Token generated successfully bucket " + clientBucket.toString());
    }

    @PostMapping("/activate-plan")
    public ResponseEntity<String> activateRateLimiterPlan(@RequestBody ActivateRouteBucket activateRouteBucket) {
        clientBucket = rateLimiterService.activateBucket(activateRouteBucket);
        ActivateRouteBucketResponse activateRouteBucketResponse = new ActivateRouteBucketResponse(clientBucket);
        return ResponseEntity.status(HttpStatus.OK).body("Bucket activated successfully bucket " + activateRouteBucketResponse.toString());
    }

    @GetMapping("/demo")
    public ResponseEntity<String> consumeRateLimiterToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getPrincipal().toString());
        Bucket clientBucket = rateLimiterService.resolveBucket(authentication.getPrincipal().toString());
        if (clientBucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.OK).body("Token consumed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Token not consumed successfully");
        }
    }


}
