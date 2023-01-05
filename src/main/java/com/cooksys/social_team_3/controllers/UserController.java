package com.cooksys.social_team_3.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.social_team_3.dtos.CredentialsDto;
import com.cooksys.social_team_3.dtos.ProfileDto;
import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	
	@PatchMapping("/{username}")
	public UserResponseDto updateUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto, @RequestBody ProfileDto profileDto) {			
		return userService.updateUser(username, credentialsDto, profileDto);
	}

	@DeleteMapping("/{username}")
	public UserResponseDto deleteUser(@PathVariable String username, @PathVariable CredentialsDto credentialsDto) {
		return userService.deleteUser(username, credentialsDto);
	}

}
