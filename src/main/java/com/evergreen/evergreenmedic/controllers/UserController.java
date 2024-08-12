package com.evergreen.evergreenmedic.controllers;

import com.evergreen.evergreenmedic.dtos.ProtectedUserDto;
import com.evergreen.evergreenmedic.dtos.requests.CreateNewUserRequestDto;
import com.evergreen.evergreenmedic.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/user")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ProtectedUserDto>> getAllUsers() {
        log.info("request received");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }

    @PostMapping()
    public ResponseEntity<ProtectedUserDto> createNewUser(@RequestBody CreateNewUserRequestDto createNewUserRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createNewUser(createNewUserRequestDto));
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<ProtectedUserDto>> getUserById(@PathVariable("id") Short id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Short> deleteUserById(@PathVariable("id") Short id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUserById(id));
    }

    @PostMapping("/seed")
    public ResponseEntity<List<ProtectedUserDto>> seedUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.seedUsers());
//        List<ProtectedUserDto> protectedUserDtos = userService.seedUsers();
//        return new ApiResponse<List<ProtectedUserDto>>().OK(200, "success", protectedUserDtos);
    }

}
