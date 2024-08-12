package com.evergreen.evergreenmedic.services;

import com.evergreen.evergreenmedic.dtos.ProtectedUserDto;
import com.evergreen.evergreenmedic.dtos.UserDto;
import com.evergreen.evergreenmedic.dtos.requests.RegisterUserReqDto;
import com.evergreen.evergreenmedic.dtos.requests.UserLoginByEmailReqDto;
import com.evergreen.evergreenmedic.dtos.response.RegisterUserRespDto;
import com.evergreen.evergreenmedic.dtos.response.UserLoginReqByEmailRespDto;
import com.evergreen.evergreenmedic.entities.UserEntity;
import com.evergreen.evergreenmedic.implementations.CustomUserDetailsServiceImpl;
import com.evergreen.evergreenmedic.repositories.UserRepository;
import com.evergreen.evergreenmedic.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserRespDto registerUser(RegisterUserReqDto registerUserReqDto) {
        String firstName = registerUserReqDto.getFirstName();
        String lastName = registerUserReqDto.getLastName();
        String email = registerUserReqDto.getEmail();
        String password = registerUserReqDto.getPassword();
        String phoneNumber = registerUserReqDto.getPhoneNumber();
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);
        userEntity.setEmail(email);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setPhoneNumber(phoneNumber);
        userEntity = userRepository.save(userEntity);
        RegisterUserRespDto registerUserRespDto = new RegisterUserRespDto();
        registerUserRespDto.setAccessToken(jwtUtil.generateToken(userEntity.getEmail()));
        registerUserRespDto.setUser(UserDto.mapToDto(userEntity));
        return registerUserRespDto;
    }

    public UserLoginReqByEmailRespDto loginByEmail(UserLoginByEmailReqDto userLoginByEmailReqDto) {
        String email = userLoginByEmailReqDto.getEmail();
        String password = userLoginByEmailReqDto.getPassword();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password, userDetails.getAuthorities());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        boolean isAuthenticated = usernamePasswordAuthenticationToken.isAuthenticated();
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        String jwt = jwtUtil.generateToken(email);
        UserLoginReqByEmailRespDto userLoginRespDto = new UserLoginReqByEmailRespDto();
        userLoginRespDto.setAccessToken(jwt);
        userLoginRespDto.setUser(ProtectedUserDto.mapToDto(userRepository.findByEmail(email)));
        return userLoginRespDto;
    }

    public Boolean isAuthenticated() {
        return true;
    }
}
