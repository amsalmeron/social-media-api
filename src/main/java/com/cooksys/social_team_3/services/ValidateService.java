package com.cooksys.social_team_3.services;

public interface ValidateService {

	boolean usernameExists(String username);

	boolean hashtagExists(String label);
	
	boolean usernameIsAvailable(String username);
	
}
