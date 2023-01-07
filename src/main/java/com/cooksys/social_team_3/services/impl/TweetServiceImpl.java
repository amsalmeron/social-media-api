package com.cooksys.social_team_3.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_team_3.dtos.ContextDto;
import com.cooksys.social_team_3.dtos.CredentialsDto;
import com.cooksys.social_team_3.dtos.HashtagDto;
import com.cooksys.social_team_3.dtos.TweetRequestDto;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.dtos.UserRequestDto;
import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.entities.Credentials;
import com.cooksys.social_team_3.entities.Hashtag;
import com.cooksys.social_team_3.entities.Tweet;
import com.cooksys.social_team_3.entities.User;
import com.cooksys.social_team_3.exceptions.BadRequestException;
import com.cooksys.social_team_3.exceptions.NotFoundException;
import com.cooksys.social_team_3.mappers.CredentialsMapper;
import com.cooksys.social_team_3.mappers.HashtagMapper;
import com.cooksys.social_team_3.mappers.TweetMapper;
import com.cooksys.social_team_3.mappers.UserMapper;
import com.cooksys.social_team_3.repositories.TweetRepository;
import com.cooksys.social_team_3.repositories.UserRepository;
import com.cooksys.social_team_3.services.TweetService;
import com.cooksys.social_team_3.services.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final UserRepository userRepository;
 
	private final UserService userService;
	
	private final TweetMapper tweetMapper;
	private final CredentialsMapper credentialsMapper;
	private final HashtagMapper hashtagMapper;
	private final UserMapper userMapper;
	

	
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
		List<Tweet> allMentions = new ArrayList<Tweet>();
		
		if(!newTweet.getContent().equals(null)) {
			for (String t : newTweet.getContent().split(" ")) {
				if (Character.toString(t.charAt(0)) == "#") {
					Hashtag newTag = new Hashtag();
					newTag.setLabel(t);
					newHashtags.add(newTag);
				}
			}
			
			for (String n : newTweet.getContent().split(" ")) {
				if (Character.toString(n.charAt(0)).equals("@")) {
					String name = new String();
					name = n.substring(1);
					if(!userService.getUser(name).getId().equals(null)) {
						allMentions = userService.getUser(name).getMentions();
						allMentions.add(newTweet);
						userService.getUser(name).setMentions(allMentions);
						System.out.println(name);
						System.out.println(currentUser.getId());
						userRepository.save(currentUser);
					}
				}
			}
		}
		newTweet.setHashtags(newHashtags);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(newTweet));
	}

	@Override
	public TweetResponseDto createTweetRepost(Long id, TweetRequestDto tweetRequestDto) {
		Credentials userCredentials = credentialsMapper.requestDtoToEntity(tweetRequestDto.getCredentials());
		User currentUser = userService.getUser(userCredentials.getUsername());
		Tweet repostTweet = getTweet(id);
		
		Tweet newTweet = new Tweet();
		newTweet.setAuthor(currentUser);
		newTweet.setContent(repostTweet.getContent());
		newTweet.setRepostOf(repostTweet);		
		
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(newTweet));
	}

	@Override
	public TweetResponseDto createTweetReply(Long id, TweetRequestDto tweetRequestDto) {
		Credentials userCredentials = credentialsMapper.requestDtoToEntity(tweetRequestDto.getCredentials());
		User currentUser = userService.getUser(userCredentials.getUsername());
		Tweet replyToTweet = getTweet(id);
		
		Tweet newReply = new Tweet();
		newReply.setAuthor(currentUser);
		newReply.setContent(tweetRequestDto.getContent());
		newReply.setInReplyTo(replyToTweet);
		
		List<Hashtag> newHashtags = new ArrayList<Hashtag>();
		
		if(!newReply.getContent().equals(null)) {
			for (String t : newReply.getContent().split(" ")) {
				if (Character.toString(t.charAt(0)) == "#") {
					Hashtag newTag = new Hashtag();
					newTag.setLabel(t);
					newHashtags.add(newTag);
				}
			}
			
			for (String n : newReply.getContent().split(" ")) {
				if (Character.toString(n.charAt(0)) == "@") {
					String name = new String();
					name = n.substring(1);
					if(!userService.getUser(name).getId().equals(null)) {
						List<Tweet> allMentions = new ArrayList<Tweet>();
						allMentions = userService.getUser(name).getMentions();
						allMentions.add(newReply);
						userRepository.saveAndFlush(currentUser);
					}
				}
			}
		}
		
		
		newReply.setHashtags(newHashtags);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(newReply));
	}

	@Override
	public void createTweetLike(Long id, TweetRequestDto tweetRequestDto) {
		Credentials userCredentials = credentialsMapper.requestDtoToEntity(tweetRequestDto.getCredentials());
		User currentUser = userService.getUser(userCredentials.getUsername());
		Tweet tweetToLike = getTweet(id);	
		List<Tweet> allLikes = new ArrayList<Tweet>();
		allLikes = currentUser.getLikedTweets();
		allLikes.add(tweetToLike);
		userRepository.saveAndFlush(currentUser);
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
