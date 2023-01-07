package com.cooksys.social_team_3.services;

import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.dtos.UserRequestDto;
import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.entities.Credentials;
import com.cooksys.social_team_3.entities.User;

import java.util.List;

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

    void followUser(String username, Credentials credential);

    void unfollowUser(String username, Credentials credential);
}
