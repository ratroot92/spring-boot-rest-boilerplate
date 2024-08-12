package com.evergreen.evergreenmedic.entities;

import com.evergreen.evergreenmedic.enums.CustomHttpMethodEnum;
import com.evergreen.evergreenmedic.enums.UserRoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;


@Entity
@Data
@Table(name = "route_rate_limiters", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_role", "route_url", "route_method"})
})
@AllArgsConstructor
@NoArgsConstructor
public class RouteRateLimiterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(name = "user_role", nullable = false, length = 14)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRole;

    @Column(name = "route_url", nullable = false, length = 100, updatable = false)
    private String routeUrl;

    @Column(name = "route_method", nullable = false, length = 100, updatable = false)
    private CustomHttpMethodEnum routeMethod;

    @Column(name = "allowed_requests_per_minute", nullable = false)
    @Min(value = 1, message = "Minimum 1 request is allowed per/min.")
    @Max(value = 100, message = "Maximum 100 request is allowed per/min.")
    private Short allowedRequestPerMinute;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, updatable = true)
    @UpdateTimestamp
    private Instant updatedAt;


}
