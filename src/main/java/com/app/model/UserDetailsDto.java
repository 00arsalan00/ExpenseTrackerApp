package com.app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDetailsDto {

    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String email;
}

