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
import com.cooksys.social_team_3.mappers.HashtagMapper;
import com.cooksys.social_team_3.entities.Hashtag;
import com.cooksys.social_team_3.dtos.HashtagDto;
import com.cooksys.social_team_3.entities.User;
import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.mappers.UserMapper;
import com.cooksys.social_team_3.repositories.UserRepository;
import com.cooksys.social_team_3.dtos.ContextDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final HashtagMapper hashtagMapper;
	private final UserMapper userMapper;
	private final UserRepository userRepository;
	
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
	
	//Endpoint #48 mhu
	@Override
	public List<UserResponseDto> getMentionsInTweetById(Long id) {
		
		Tweet tweet = getTweet(id);
		List<User> allUsers = userRepository.findAll();
		List<User> mentionedUsers = new ArrayList<>();
		for(User u : allUsers) {
			if(tweet.getContent().contains("@" + u.getCredentials().getUsername()) && !u.isDeleted()) {
				u.getMentions().add(tweet);
				tweet.getUsersMentioned().add(u);
				mentionedUsers.add(u);
			}
		}
		return userMapper.entitiesToDtos(mentionedUsers);
		
	}
	
	//Endpoint #50 mhu
	@Override
	public List<TweetResponseDto> getRepliesToTweetById(Long id) {
	
		Tweet tweetReplies = getTweet(id);
		List<Tweet> replyList = 
				tweetRepository.findAll()
				.stream()
				.filter(tweet -> tweetReplies.getReplies().contains(tweet) && !tweet.isDeleted())
				.toList();
		return tweetMapper.entitiesToDtos(replyList);

	}
	
	//Endpoint #51 mhu
	@Override
	public ContextDto getContextForTweet(Long id) {

		ContextDto contextDto = new ContextDto();
		Tweet contextTweet = getTweet(id);
		
		if (contextTweet.isDeleted() || contextTweet == null) {
			throw new NotFoundException("Not Found");
		}
		
		Tweet beforeTweet = contextTweet.getInReplyTo();
		
		List<Tweet> beforeTweets = new ArrayList<>();
		List<Tweet> after = contextTweet.getReplies();
		List<Tweet> afterTweets = new ArrayList<>();
		
		afterTweets.addAll(after);
		
		while (beforeTweet != null) {
			beforeTweets.add(beforeTweet);
			beforeTweet = beforeTweet.getInReplyTo();
		}
		for (Tweet tweet : after) {
			if (tweet.getReplies() != (null)) {
				afterTweets.addAll(tweet.getReplies());
			}
		}
		for (Tweet tweet : beforeTweets) {
			if (tweet.isDeleted()) {
				beforeTweets.remove(tweet);
			}
		}
		for (Tweet tweet : afterTweets) {
			if (tweet.isDeleted()) {
				afterTweets.remove(tweet);
			}
		}
		
		contextDto.setBefore(tweetMapper.entitiesToDtos(beforeTweets));
		contextDto.setTarget(tweetMapper.entityToDto(contextTweet));
		contextDto.setAfter(tweetMapper.entitiesToDtos(afterTweets));

		return contextDto;
		
	}
	
	
	//Endpoint #52 mhu
	@Override
	public List<UserResponseDto> getLikesForTweet(Long id) {
		
		Tweet tweet = getTweet(id);
		List<User> likes = tweet.getLikes();
		return userMapper.entitiesToDtos(likes);
		
	}
	
	//Endpoint #53 mhu
	@Override
	public List<HashtagDto> getAllTagsForTweet(Long id) {
			
		List<Hashtag> hashtags = getTweet(id).getHashtags();
		
		return hashtagMapper.entitiesToDtos(hashtags);
			
	}
	
}
