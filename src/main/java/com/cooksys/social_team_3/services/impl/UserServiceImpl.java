package com.cooksys.social_team_3.services.impl;

import com.cooksys.social_team_3.dtos.CredentialsDto;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.dtos.UserRequestDto;
import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.entities.Tweet;
import com.cooksys.social_team_3.entities.User;
import com.cooksys.social_team_3.exceptions.NotFoundException;
import com.cooksys.social_team_3.mappers.CredentialsMapper;
import com.cooksys.social_team_3.mappers.TweetMapper;
import com.cooksys.social_team_3.mappers.UserMapper;
import com.cooksys.social_team_3.repositories.UserRepository;
import com.cooksys.social_team_3.services.UserService;
import com.cooksys.social_team_3.services.Deletables;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private TweetMapper tweetMapper;

    private CredentialsMapper credentialsMapper;


    @Override //POST Users
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        return null;
    }

    @Override //GET Users
    public List<UserResponseDto> getAllUsers() {
        return userMapper.entitiesDto(userRepository.findAllByDeleted(false));
    }

    @Override //GET users/@{username}
    public UserResponseDto getUserByUsername(String username) {
        User user = checkUsername(username);
        if (user.isDeleted()) {
            throw new NotFoundException("This account no longer exists");
        }
        return userMapper.entityToResponseDto(user);
    }

    @Override //GET users/@{username}/following
    public List<UserResponseDto> getFollowing(String username) {
        return userMapper.entitiesDto(removeDeleted(checkUsername(username).getFollowing()));
    }

    @Override //GET users/@{username}/followers
    public List<UserResponseDto> getFollowers(String username) {
        return userMapper.entitiesDto(removeDeleted(checkUsername(username).getFollowers()));
    }

    @Override //GET users/@{username}/mentions
    public List<TweetResponseDto> getMentions(String username) {
        return tweetMapper.entitiesToDto(newestToOldest(removeDeleted(checkUsername(username).getMentions())));
    }

    @Override //GET users/@{username}/tweets
    public List<TweetResponseDto> getUsernameTweets(String username) {
        return tweetMapper.entitiesToDto(newestToOldest(removeDeleted(checkUsername(username).getTweets())));
    }

    @Override //GET users/@{username}/feed
    public List<TweetResponseDto> getFeed(String username) {
        User user = checkUsername(username);
        List<User> following = removeDeleted(user.getFollowing());
        List<Tweet> tweets = removeDeleted(user.getTweets());
        for (User followed : following) {
            tweets.addAll(removeDeleted(followed.getTweets()));
        }
        return tweetMapper.entitiesToDto(newestToOldest(tweets));
    }

    @Override
    public void followUser(CredentialsDto credentialsDto, String username) {

    }

    @Override
    public void unfollowUser(CredentialsDto credentialsDto, String username) {

    }


    //Helpers
    private User checkUsername(String username) {
        Optional<User> user = userRepository.findByCredentials_Username(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("This account does not exist");
        }
    }

    private <Test extends Deletables> List<Test> removeDeleted(List<Test> toFilter) {
        List<Test> filtered = new ArrayList<>();
        for (Test item : toFilter) {
            if (!item.isDeleted()) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    private List<Tweet> newestToOldest(List<Tweet> toSort) {
        List<Tweet> result = new ArrayList<>(toSort);
        result.sort(Comparator.comparing(Tweet::getPosted));
        Collections.reverse(result);
        return result;
    }

}


