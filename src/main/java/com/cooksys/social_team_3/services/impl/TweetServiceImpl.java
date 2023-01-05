package com.cooksys.social_team_3.services.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

import com.cooksys.social_team_3.services.TweetService;
import com.cooksys.social_team_3.repositories.TweetRepository;
import com.cooksys.social_team_3.entities.Tweet;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.mappers.TweetMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;

	//Endpoint #60 mhu
	@Override
	public List<TweetResponseDto> getAllActiveTweets() {

		List<Tweet> allTweets = new ArrayList<Tweet>();
		for (Tweet tweet : tweetRepository.findAll()) {
			if (!tweet.isDeleted()) {
				allTweets.add(tweet);
			}
		}
		return tweetMapper.entitiesToDtos(allTweets);

	}
	
}
