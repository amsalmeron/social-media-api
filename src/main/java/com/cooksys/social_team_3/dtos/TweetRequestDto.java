package com.cooksys.social_team_3.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetRequestDto {

    private String content;

    private CredentialsDto credentials;

}
