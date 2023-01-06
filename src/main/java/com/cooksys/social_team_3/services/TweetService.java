package com.cooksys.social_team_3.services;

import java.util.List;

import com.cooksys.social_team_3.dtos.TweetRequestDto;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.dtos.UserRequestDto;
import com.cooksys.social_team_3.entities.Tweet;

public interface TweetService {
	
	List<TweetResponseDto> getAllActiveTweets();

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	Tweet getTweet(Long id);

	TweetResponseDto getTweetById(Long id);

	List<TweetResponseDto> getTweetRepostsById(Long id);

	TweetResponseDto deleteTweet(Long id, UserRequestDto userRequestDto);

	TweetResponseDto createTweetRepost(Long id, TweetRequestDto tweetRequestDto);

	TweetResponseDto createTweetReply(Long id, String content, TweetRequestDto tweetRequestDto);

	TweetResponseDto createTweetLike(Long id, TweetRequestDto tweetRequestDto);
	
}
