package com.evergreen.evergreenmedic.dtos.requests;


import lombok.Data;


@Data
public class CreateNewUserRequestDto {
    
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
}
