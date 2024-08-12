package com.evergreen.evergreenmedic.dtos.requests;

import com.evergreen.evergreenmedic.enums.CustomHttpMethodEnum;
import com.evergreen.evergreenmedic.enums.UserRoleEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRouteRateLimiterReq {


    @NotBlank
    private UserRoleEnum userRole;

    @NotBlank
    private String routeUrl;

    @NotBlank
    private CustomHttpMethodEnum routeMethod;

    @NotBlank
    @Min(value = 1, message = "Minimum 1 request is allowed per/min.")
    @Max(value = 100, message = "Maximum 100 request is allowed per/min.")
    private Short allowedRequestPerMinute;


}
