package com.cooksys.social_team_3.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserResponseDto {

    private CredentialsDto credentials;

    private ProfileDto profile;

}
