package com.cooksys.social_team_3.controllers;

import com.cooksys.social_team_3.dtos.CredentialsDto;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.dtos.UserRequestDto;
import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private UserService userService;

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
    public void followUser(@RequestBody CredentialsDto credentialsDto, @PathVariable String username){
        userService.followUser(credentialsDto, username);
    }

    @PostMapping("@{username}/unfollow")
    public void unfollowUser(@RequestBody CredentialsDto credentialsDto, @PathVariable String username) {
        userService.unfollowUser(credentialsDto, username);
    }

}