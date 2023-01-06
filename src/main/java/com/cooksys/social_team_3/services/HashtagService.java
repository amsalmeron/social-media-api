package com.cooksys.social_team_3.services;

import java.util.List;

import com.cooksys.social_team_3.dtos.HashtagDto;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.entities.Hashtag;

public interface HashtagService {

	List<HashtagDto> getAllHashtags();
	
	Hashtag getHashtag(String hashtag);
	
	List<TweetResponseDto> getHashtagTweets(String hashtag);
	
}
