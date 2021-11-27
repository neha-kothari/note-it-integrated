package com.noteit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegistrationDTO {

    private int userType;
    private String emailAddress;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;

}
