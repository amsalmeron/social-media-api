package com.cooksys.social_team_3.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_team_3.dtos.TweetRequestDto;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.entities.Credentials;
import com.cooksys.social_team_3.entities.Tweet;
import com.cooksys.social_team_3.exceptions.BadRequestException;
import com.cooksys.social_team_3.exceptions.NotFoundException;
import com.cooksys.social_team_3.mappers.TweetMapper;
import com.cooksys.social_team_3.repositories.TweetRepository;
import com.cooksys.social_team_3.services.TweetService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	
	@Override
	public Tweet getTweet(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findById(id);
		
		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("No tweet found with the id: " + id);
		}
		return optionalTweet.get();
	}
	
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

	@Override
	public TweetResponseDto getTweetById(Long id) {
		Tweet currentTweet = getTweet(id);
		if (currentTweet.isDeleted()) {
			throw new BadRequestException("This tweet has been deleted.");
		}
		return tweetMapper.entityToDto(currentTweet);
	}
	
//  POST Tweet postoned
	@Override
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
//		if (userRepository.findByCredentials_Username(tweetRequestDto.getCredentials().getUsername()) != null 
//				&& 
//				userRepository.findByCredentials_Username(tweetRequestDto.getCredentials().getPassword() == tweetRequestDto.getCredentials().getPassword()
//						)
		return null;
	}

	@Override
	public List<TweetResponseDto> getTweetRepostsById(Long id) {
		Tweet currentTweet = getTweet(id);
		List<Tweet> tweetReposts = new ArrayList<Tweet>();
		for (Tweet tweet : tweetRepository.findAll()) {
			if (tweet.getRepostOf() == currentTweet) {
				tweetReposts.add(tweet);
			}
		}
		if (tweetReposts.isEmpty()) {
			throw new NotFoundException("No tweet reposts found with the id: " + id); 
		}
		return tweetMapper.entitiesToDtos(tweetReposts);
	}

	@Override
	public TweetResponseDto deleteTweet(Long id, Credentials credentials) {
		Tweet tweetToDelete = getTweet(id);
		
//		if (tweetMapper.verifyCredentials(deleteTweet.getAuthor().getCredentials()) == tweetMapper.verifyCredentials(credentials)) {
//			deleteTweet.setDeleted(true);
//			return tweetMapper.entityToDto(deleteTweet);
//		} else {
//			throw new NotFoundException("Credentials are not valid");
//		}
		
		tweetToDelete.setDeleted(true);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToDelete));
	}
	
}
