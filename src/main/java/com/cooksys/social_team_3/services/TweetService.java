package com.cooksys.social_team_3.services;

import com.cooksys.social_team_3.dtos.TweetResponseDto;

import java.util.List;

public interface TweetService {
	
	List<TweetResponseDto> getAllActiveTweets();
	
}
