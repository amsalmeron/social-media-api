package com.cooksys.social_team_3.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
@Data
public class Credentials {
	
	@Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    
}