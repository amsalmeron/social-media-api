package com.cooksys.social_team_3.services;

import com.cooksys.social_team_3.dtos.CredentialsDto;

import com.cooksys.social_team_3.dtos.ProfileDto;
import com.cooksys.social_team_3.entities.User;

public interface UserService {

	UserResponseDto updateUser(String username, CredentialsDto credentialsDto, ProfileDto profileDto);

	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	User getUser(String username);

import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.dtos.UserRequestDto;
import com.cooksys.social_team_3.dtos.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto userRequestDto);

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserByUsername(String username);

    List<UserResponseDto> getFollowing(String username);

    List<UserResponseDto> getFollowers(String username);

    List<TweetResponseDto> getMentions(String username);

    List<TweetResponseDto> getUsernameTweets(String username);

    List<TweetResponseDto> getFeed(String username);

    void followUser(CredentialsDto credentialsDto, String username);

    void unfollowUser(CredentialsDto credentialsDto, String username);


}
