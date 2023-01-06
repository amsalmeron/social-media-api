package com.cooksys.social_team_3.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_team_3.dtos.HashtagDto;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.entities.Hashtag;
import com.cooksys.social_team_3.exceptions.BadRequestException;
import com.cooksys.social_team_3.mappers.HashtagMapper;
import com.cooksys.social_team_3.mappers.TweetMapper;
import com.cooksys.social_team_3.repositories.HashtagRepository;
import com.cooksys.social_team_3.services.HashtagService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HashtagServiceImpl implements HashtagService {
	
	private final HashtagRepository hashtagRepository;
	
	private final TweetMapper tweetMapper;
	private final HashtagMapper hashtagMapper;

	@Override
	public Hashtag getHashtag(String hashtag) {
		Optional<Hashtag> currentHashtag = hashtagRepository.findByLabel(hashtag);
		if (currentHashtag.isEmpty()) {
			throw new BadRequestException("Tweet with requested hashtag does not exist.");
		}
		return currentHashtag.get();
	}
	
	
	//Endpoint #62 mhu
	@Override
	public List<HashtagDto> getAllHashtags() {

		return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
		
	}


	@Override
	public List<TweetResponseDto> getHashtagTweets(String hashtag) {
		Hashtag currentTag = getHashtag(hashtag);
		return tweetMapper.entitiesToDtos(currentTag.getTweets());
	}

}
