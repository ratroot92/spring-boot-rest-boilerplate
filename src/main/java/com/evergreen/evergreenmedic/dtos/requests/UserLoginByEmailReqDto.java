package com.evergreen.evergreenmedic.dtos.requests;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserLoginByEmailReqDto {
    @NotNull
    private String email;
    @NotNull
    private String password;
}
