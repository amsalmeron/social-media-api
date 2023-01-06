package com.cooksys.social_team_3.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.services.HashtagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {
	
	private final HashtagService hashtagService;
	
	@GetMapping("/{hashtag}")
	public List<TweetResponseDto> getHashtagTweets(@PathVariable String hashtag) {
		return hashtagService.getHashtagTweets(hashtag);
	}

}
