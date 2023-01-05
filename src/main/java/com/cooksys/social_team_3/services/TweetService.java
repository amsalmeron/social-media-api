package com.cooksys.social_team_3.services;

import java.util.List;

import com.cooksys.social_team_3.dtos.TweetRequestDto;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.entities.Credentials;
import com.cooksys.social_team_3.entities.Tweet;

public interface TweetService {
	
	List<TweetResponseDto> getAllActiveTweets();

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	Tweet getTweet(Long id);

	TweetResponseDto getTweetById(Long id);

	List<TweetResponseDto> getTweetRepostsById(Long id);

	TweetResponseDto deleteTweet(Long id, Credentials credentials);
	
}
