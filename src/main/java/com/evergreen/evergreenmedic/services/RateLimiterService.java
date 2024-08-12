package com.evergreen.evergreenmedic.services;

import com.evergreen.evergreenmedic.dtos.requests.ActivateRouteBucket;
import com.evergreen.evergreenmedic.dtos.requests.CreateRouteRateLimiterReq;
import com.evergreen.evergreenmedic.entities.RouteRateLimiterEntity;
import com.evergreen.evergreenmedic.entities.UserEntity;
import com.evergreen.evergreenmedic.enums.CustomHttpMethodEnum;
import com.evergreen.evergreenmedic.enums.UserRoleEnum;
import com.evergreen.evergreenmedic.repositories.RouteRateLimiterRepository;
import com.evergreen.evergreenmedic.repositories.UserRepository;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public class RateLimiterService {
    private String basic = "basic";
    private String free = "free";
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final RouteRateLimiterRepository routeRateLimiterRepository;
    private final UserRepository userRepository;


    public RateLimiterService(RouteRateLimiterRepository routeRateLimiterRepository, UserService userService, UserRepository userRepository) {
        this.routeRateLimiterRepository = routeRateLimiterRepository;
        this.userRepository = userRepository;
    }


    public Bucket activateBucket(ActivateRouteBucket activateRouteBucket) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        buckets.remove(authentication.getPrincipal().toString());
//        Bucket clientBucket = buckets.get(authentication.getPrincipal().toString());
//        if (clientBucket == null) {
        UserEntity userEntity = userRepository.findByEmail(authentication.getPrincipal().toString());
        Optional<RouteRateLimiterEntity> rateLimiterEntity = routeRateLimiterRepository.findByRouteUrlAndRouteMethodAndUserRole(activateRouteBucket.getRouteUri(), activateRouteBucket.getHttpMethod(), userEntity.getRole());
        if (rateLimiterEntity.isPresent()) {
            long allowedRequestPerMinute = rateLimiterEntity.get().getAllowedRequestPerMinute();
            Bandwidth bandwidthLimit = Bandwidth.classic(allowedRequestPerMinute, Refill.of(allowedRequestPerMinute, Duration.ofMinutes(1)));
            Bucket bucket = Bucket4j.builder().addLimit(bandwidthLimit).build();
            buckets.put(authentication.getPrincipal().toString(), bucket);
            return bucket;
        } else {
            throw new EntityNotFoundException("Could not find route rate limiter for " + activateRouteBucket.getRouteUri());
        }
    }

    public Bucket resolveBucket(String key) {
        return buckets.get(key);
    }

    private Bandwidth getClientBandwidth(String clientToken) {
        if (clientToken.equals(free)) {
            return Bandwidth.classic(5, Refill.of(5, Duration.ofMinutes(1)));
        }
        if (clientToken.equals(basic)) {
            return Bandwidth.classic(25, Refill.of(25, Duration.ofMinutes(1)));
        } else {
            return Bandwidth.classic(2, Refill.of(2, Duration.ofMinutes(1)));
        }

    }

    private Bandwidth getDefaultBucket(long allowedTokens) {
        return Bandwidth.classic(allowedTokens, Refill.of(allowedTokens, Duration.ofMinutes(1)));
    }

    public RouteRateLimiterEntity createRouteRateLimiter(CreateRouteRateLimiterReq createRouteRateLimiterReq) {
        UserRoleEnum userRole = createRouteRateLimiterReq.getUserRole();
        String routeUrl = createRouteRateLimiterReq.getRouteUrl();
        CustomHttpMethodEnum routeMethod = createRouteRateLimiterReq.getRouteMethod();
        Optional<RouteRateLimiterEntity> routeRateLimiterEntityExists = routeRateLimiterRepository.findByRouteUrlAndRouteMethodAndUserRole(routeUrl, routeMethod, userRole);
        if (routeRateLimiterEntityExists.isPresent()) {
            throw new DataIntegrityViolationException("Duplicate entry.");
        }
        Short allowedRequestsPerMinute = createRouteRateLimiterReq.getAllowedRequestPerMinute();
        RouteRateLimiterEntity routeRateLimiterEntity = new RouteRateLimiterEntity();
        routeRateLimiterEntity.setUserRole(userRole);
        routeRateLimiterEntity.setRouteUrl(routeUrl);
        routeRateLimiterEntity.setRouteMethod(routeMethod);
        routeRateLimiterEntity.setAllowedRequestPerMinute(allowedRequestsPerMinute);
        routeRateLimiterEntity = routeRateLimiterRepository.save(routeRateLimiterEntity);
        return routeRateLimiterEntity;
    }

    public List<RouteRateLimiterEntity> getAllRouteRateLimiterConfigs() {
        List<RouteRateLimiterEntity> routeRateLimiterEntities = routeRateLimiterRepository.findAll();
        return routeRateLimiterEntities;
    }

    public Optional<RouteRateLimiterEntity> getRouteRateLimiterById(Short id) {
        Optional<RouteRateLimiterEntity> routeRateLimiterEntity = Optional.ofNullable(routeRateLimiterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Route Rate Limiter not found.")));
        return routeRateLimiterEntity;
    }
}
