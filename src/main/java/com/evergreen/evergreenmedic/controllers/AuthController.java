package com.evergreen.evergreenmedic.controllers;


import com.evergreen.evergreenmedic.dtos.requests.RegisterUserReqDto;
import com.evergreen.evergreenmedic.dtos.requests.UserLoginByEmailReqDto;
import com.evergreen.evergreenmedic.dtos.response.RegisterUserRespDto;
import com.evergreen.evergreenmedic.dtos.response.UserLoginReqByEmailRespDto;
import com.evergreen.evergreenmedic.services.AuthService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserRespDto> registerUser(@RequestBody RegisterUserReqDto registerUserReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.registerUser(registerUserReqDto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginReqByEmailRespDto> loginByEmail(@RequestBody UserLoginByEmailReqDto userLoginByEmailReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.loginByEmail(userLoginByEmailReqDto));
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<Boolean> isAuthenticated() {
        return ResponseEntity.status(HttpStatus.OK).body(authService.isAuthenticated());
    }
}
