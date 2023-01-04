package com.cooksys.social_team_3.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class Credentials {
	
	@Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    
}