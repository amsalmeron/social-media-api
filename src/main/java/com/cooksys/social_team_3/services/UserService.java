package com.cooksys.social_team_3.services;

import com.cooksys.social_team_3.dtos.CredentialsDto;
import com.cooksys.social_team_3.dtos.ProfileDto;
import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.entities.User;

public interface UserService {

	UserResponseDto updateUser(String username, CredentialsDto credentialsDto, ProfileDto profileDto);

	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	User getUser(String username);

}
