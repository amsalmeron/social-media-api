package com.cooksys.social_team_3.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.dtos.UserRequestDto;
import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.entities.Credentials;
import com.cooksys.social_team_3.services.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	
	@PatchMapping("/@{username}")
	public UserResponseDto updateUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {			
		return userService.updateUser(username, userRequestDto);
	}

	@DeleteMapping("/@{username}")
	public UserResponseDto deleteUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
		return userService.deleteUser(username, userRequestDto);
	}

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto){
        return userService.createUser(userRequestDto);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    @GetMapping("@{username}/following")
    public List<UserResponseDto> getFollowing(@PathVariable String username) {
        return userService.getFollowing(username);
    }

    @GetMapping("@{username}/followers")
    public List<UserResponseDto> getFollowers(@PathVariable String username) {
        return userService.getFollowers(username);
    }

    @GetMapping("@{username}/mentions")
    public List<TweetResponseDto> getMentions(@PathVariable String username){
        return userService.getMentions(username);
    }

    @GetMapping("@{username}/tweets")
    public List<TweetResponseDto> getUsernameTweets(@PathVariable String username){
        return userService.getUsernameTweets(username);
    }

    @GetMapping("@{username}/feed")
    public List<TweetResponseDto> getFeed(@PathVariable String username){
        return userService.getFeed(username);
    }

    @PostMapping("@{username}/follow")
    public void followUser(@PathVariable String username, @RequestBody Credentials credential) {
        userService.followUser(username, credential);
    }

    @PostMapping("@{username}/unfollow")
    public void unfollowUser(@PathVariable String username, @RequestBody Credentials credential) {
        userService.unfollowUser(username, credential);
    }

}