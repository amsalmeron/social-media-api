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
	
	//Unfinished endpoint
	@PostMapping("/{id}/repost")
	public TweetResponseDto createTweetRepost(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweetRepost(id, tweetRequestDto);
	}
	
	//Unfinished endpoint
	@PostMapping("/{id}/reply")
	public TweetResponseDto createTweetReply(@PathVariable Long id, @RequestBody String content, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweetReply(id, content, tweetRequestDto);
	}
	
	//Unfinished endpoint
	@PostMapping("/{id}/like")
	public TweetResponseDto createTweetReply(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweetLike(id, tweetRequestDto);
	}
	
	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getTweetRepostsById(@PathVariable Long id) {
		return tweetService.getTweetRepostsById(id);
	}
	
	@DeleteMapping("/{id}")
	public TweetResponseDto deleteTweet(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
		return tweetService.deleteTweet(id, userRequestDto);
	}
}
