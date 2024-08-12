package com.evergreen.evergreenmedic.dtos;

import com.evergreen.evergreenmedic.entities.UserEntity;
import com.evergreen.evergreenmedic.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtectedUserDto {
    private Short id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private UserRoleEnum Role;
    private Instant createdAt;
    private Instant updatedAt;

    public static ProtectedUserDto mapToDto(UserEntity user) {
        ProtectedUserDto userDto = new ProtectedUserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        return userDto;
    }

    public static UserEntity mapToEntity(UserDto user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhoneNumber(user.getPhoneNumber());
        return userEntity;
    }

}
