package com.cooksys.social_team_3.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class HashtagDto {

    private String label;

    private Timestamp firstUsed;

    private Timestamp lastUsed;

}
