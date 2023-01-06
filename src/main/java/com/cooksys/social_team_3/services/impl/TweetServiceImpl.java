package com.cooksys.social_team_3.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_team_3.dtos.CredentialsDto;
import com.cooksys.social_team_3.dtos.TweetRequestDto;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.dtos.UserRequestDto;
import com.cooksys.social_team_3.entities.Credentials;
import com.cooksys.social_team_3.entities.Hashtag;
import com.cooksys.social_team_3.entities.Tweet;
import com.cooksys.social_team_3.entities.User;
import com.cooksys.social_team_3.exceptions.BadRequestException;
import com.cooksys.social_team_3.exceptions.NotFoundException;
import com.cooksys.social_team_3.mappers.CredentialsMapper;
import com.cooksys.social_team_3.mappers.TweetMapper;
import com.cooksys.social_team_3.repositories.TweetRepository;
import com.cooksys.social_team_3.services.TweetService;
import com.cooksys.social_team_3.services.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	
	private final UserService userService;
	
	private final TweetMapper tweetMapper;
	private final CredentialsMapper credentialsMapper;
	
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
	public TweetResponseDto deleteTweet(Long id, UserRequestDto userRequestDto) {
		Tweet tweetToDelete = getTweet(id);
		CredentialsDto credentialDto = userRequestDto.getCredentials();
		
		if (credentialsMapper.requestDtoToEntity(credentialDto).equals(tweetToDelete.getAuthor().getCredentials())) {
			tweetToDelete.setDeleted(true);
			return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToDelete));
		}
		throw new NotFoundException("Credentials are not valid");
	}
	
	@Override
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
		Credentials userCredentials = credentialsMapper.requestDtoToEntity(tweetRequestDto.getCredentials());
		User currentUser = userService.getUser(userCredentials.getUsername());
		
		Tweet newTweet = new Tweet();
		newTweet.setContent(tweetRequestDto.getContent());
		newTweet.setAuthor(currentUser);
		List<Hashtag> newHashtags = new ArrayList<Hashtag>();
		
		if(!newTweet.getContent().equals(null)) {
			for (String t : newTweet.getContent().split(" ")) {
				if (Character.toString(t.charAt(0)) == "#") {
					Hashtag newTag = new Hashtag();
					newTag.setLabel(t);
					newHashtags.add(newTag);
				}
			}
		}
		
		newTweet.setHashtags(newHashtags);
//		System.out.println(newTweet);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(newTweet));
	}

	@Override
	public TweetResponseDto createTweetRepost(Long id, TweetRequestDto tweetRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TweetResponseDto createTweetReply(Long id, String content, TweetRequestDto tweetRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TweetResponseDto createTweetLike(Long id, TweetRequestDto tweetRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
