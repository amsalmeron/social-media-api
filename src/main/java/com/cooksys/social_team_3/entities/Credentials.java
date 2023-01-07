package com.cooksys.social_team_3.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
@Data
@AllArgsConstructor
public class Credentials {
	
	@Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    
}