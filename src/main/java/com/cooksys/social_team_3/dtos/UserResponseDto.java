package com.cooksys.social_team_3.dtos;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserResponseDto {
    
    private String username;

    private CredentialsDto credentials;

    private Timestamp joined;

}
