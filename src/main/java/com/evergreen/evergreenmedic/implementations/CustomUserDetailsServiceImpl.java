package com.evergreen.evergreenmedic.implementations;

import com.evergreen.evergreenmedic.entities.UserEntity;
import com.evergreen.evergreenmedic.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        log.info(" CustomUserDetailsServiceImpl [loadUserByUsername] userEntity : {}", userEntity);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with email" + email);
        } else {
            List<String> userRoles = new ArrayList<>();
            userRoles.add(userEntity.getRole().name());
//            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
//                    .username(userEntity.getEmail())
//                    .password(userEntity.getPassword())
//                    .authorities("USER")
////                    .roles(String.valueOf(userRoles))
////                    .roles(userRoles.toArray(new String[0]))
//                    .build();
            return org.springframework.security.core.userdetails.User.withUsername(userEntity.getEmail()).password(userEntity.getPassword()).roles(userRoles.toArray(new String[0])).build();
//            log.info(" CustomUserDetailsServiceImpl [loadUserByUsername] userRoles.toArray(new String[0]) : {}", userRoles.toArray(new String[0]));
//            return userDetails;
        }
    }
}
