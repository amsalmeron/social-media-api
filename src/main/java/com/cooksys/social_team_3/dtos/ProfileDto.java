package com.cooksys.social_team_3.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProfileDto {

    private String firstName;

    private String lastName;
// Only the email property is required.
    private String email;

    private String phone;

}