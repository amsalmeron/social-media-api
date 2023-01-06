package com.cooksys.social_team_3.services;

import java.util.List;

import com.cooksys.social_team_3.dtos.*;
import com.cooksys.social_team_3.entities.User;

public interface UserService {
	UserResponseDto updateUser(String username, UserRequestDto userRequestDto);

	UserResponseDto deleteUser(String username, UserRequestDto userRequestDto);

	User getUser(String username);

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

    UserResponseDto updateUser(String username, CredentialsDto credentialsDto, ProfileDto profileDto);

    UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

    User getUser(String username);
}
