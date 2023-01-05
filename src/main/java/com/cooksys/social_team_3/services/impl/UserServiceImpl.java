package com.cooksys.social_team_3.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_team_3.exceptions.BadRequestException;
import com.cooksys.social_team_3.exceptions.NotFoundException;
import com.cooksys.social_team_3.dtos.CredentialsDto;
import com.cooksys.social_team_3.dtos.ProfileDto;
import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.entities.User;
import com.cooksys.social_team_3.mappers.CredentialsMapper;
import com.cooksys.social_team_3.mappers.ProfileMapper;
import com.cooksys.social_team_3.mappers.UserMapper;
import com.cooksys.social_team_3.repositories.UserRepository;
import com.cooksys.social_team_3.services.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	private final UserMapper userMapper;
	private final ProfileMapper profileMapper;
	private final CredentialsMapper credentialsMapper;
	
	@Override
	public User getUser(String username) {
		Optional<User> optionalUser = userRepository.findByCredentials_Username(username);
		
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("No user found with the username: " + username);
		}
		return optionalUser.get();
	}
	
	@Override
	public UserResponseDto updateUser(String username, CredentialsDto credentialsDto, ProfileDto profileDto) {
		User userToUpdate = getUser(username);
		if (userToUpdate.getCredentials() != credentialsMapper.requestDtoToEntity(credentialsDto)) {
			throw new BadRequestException("Unable to update profile, credentials are not valid.");
		}
		userToUpdate.setProfile(profileMapper.requestDtoToEntity(profileDto));
		return userMapper.entityToResponseDto(userRepository.saveAndFlush(userToUpdate));
	}

	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
		User userToDelete = getUser(username);
		if(userToDelete.getCredentials().getPassword() != credentialsDto.getPassword()) {
			throw new BadRequestException("User credentials are invalid.");
		}
		userToDelete.setDeleted(true);
		return userMapper.entityToResponseDto(userRepository.saveAndFlush(userToDelete));
	}
	
	
}

