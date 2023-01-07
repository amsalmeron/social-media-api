package com.cooksys.social_team_3.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.social_team_3.dtos.TweetRequestDto;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.dtos.UserRequestDto;
import com.cooksys.social_team_3.services.TweetService;
import com.cooksys.social_team_3.dtos.HashtagDto;
import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.dtos.ContextDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {

	private final TweetService tweetService;
	
	@GetMapping("/{id}")
	public TweetResponseDto getTweetById(@PathVariable Long id) {
		return tweetService.getTweetById(id);
	}

	@PostMapping
	public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweet(tweetRequestDto);
	}
	
	@PostMapping("/{id}/repost")
	public TweetResponseDto createTweetRepost(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweetRepost(id, tweetRequestDto);
	}
	
	@PostMapping("/{id}/reply")
	public TweetResponseDto createTweetReply(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweetReply(id, tweetRequestDto);
	}
	
	@PostMapping("/{id}/like")
	public void createTweetLike(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		tweetService.createTweetLike(id, tweetRequestDto);

	}
	
	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getTweetRepostsById(@PathVariable Long id) {
		return tweetService.getTweetRepostsById(id);
	}
	
	@DeleteMapping("/{id}")
	public TweetResponseDto deleteTweet(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
		return tweetService.deleteTweet(id, userRequestDto);
	}
	
	@GetMapping
    public List<TweetResponseDto> getAllTweets() {
        return tweetService.getAllActiveTweets();
    }
	
	@GetMapping("/{id}/tags")
	public List<HashtagDto> getAllTagsForTweet(@PathVariable Long id) {
		return tweetService.getAllTagsForTweet(id);
	}
	
	@GetMapping("/{id}/replies")
	public List<TweetResponseDto> getReplyToTweetById(@PathVariable Long id) {
		return tweetService.getRepliesToTweetById(id);
	}
	
	@GetMapping("/{id}/likes")
	public List<UserResponseDto> getLikesForTweet(@PathVariable Long id) {
		return tweetService.getLikesForTweet(id);
	}
	
	@GetMapping("/{id}/mentions")
	public List<UserResponseDto> getMentionsInTweetById(@PathVariable Long id) {
		return tweetService.getMentionsInTweetById(id);
	}
	
	@GetMapping("/{id}/context")
	public ContextDto getContextForTweet(@PathVariable Long id) {
		return tweetService.getContextForTweet(id);
	}
}
