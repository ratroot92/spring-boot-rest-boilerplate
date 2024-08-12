package com.evergreen.evergreenmedic.dtos.requests;

import com.evergreen.evergreenmedic.enums.CustomHttpMethodEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateRouteBucket {
    @NotBlank
    private String routeUri;

    @NotBlank
    private CustomHttpMethodEnum httpMethod;
}
