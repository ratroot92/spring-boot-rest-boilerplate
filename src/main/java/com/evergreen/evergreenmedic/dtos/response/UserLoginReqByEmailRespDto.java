package com.evergreen.evergreenmedic.dtos.response;

import com.evergreen.evergreenmedic.dtos.ProtectedUserDto;
import lombok.Data;

@Data
public class UserLoginReqByEmailRespDto {
    String accessToken;
    ProtectedUserDto user;
}
