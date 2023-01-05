package com.cooksys.social_team_3.services.impl;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_team_3.exceptions.BadRequestException;
import com.cooksys.social_team_3.exceptions.NotFoundException;
import com.cooksys.social_team_3.dtos.CredentialsDto;
import com.cooksys.social_team_3.dtos.ProfileDto;
import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.entities.User;
import com.cooksys.social_team_3.mappers.CredentialsMapper;
import com.cooksys.social_team_3.mappers.ProfileMapper;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.dtos.UserRequestDto;
import com.cooksys.social_team_3.entities.Tweet;

import com.cooksys.social_team_3.exceptions.NotFoundException;

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
	private final UserRepository userRepository;
	
	private final UserMapper userMapper;
	private final ProfileMapper profileMapper;
	private final CredentialsMapper credentialsMapper;
	
	@Override
	public User getUser(String username) {
		Optional<User> optionalUser = userRepository.findByCredentials_Username(username);
		
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("No user found with the username: " + username);
		}
		return optionalUser.get();
	}
	
	@Override
	public UserResponseDto updateUser(String username, CredentialsDto credentialsDto, ProfileDto profileDto) {
		User userToUpdate = getUser(username);
		if (userToUpdate.getCredentials() != credentialsMapper.requestDtoToEntity(credentialsDto)) {
			throw new BadRequestException("Unable to update profile, credentials are not valid.");
		}
		userToUpdate.setProfile(profileMapper.requestDtoToEntity(profileDto));
		return userMapper.entityToResponseDto(userRepository.saveAndFlush(userToUpdate));
	}

	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
		User userToDelete = getUser(username);
		if(userToDelete.getCredentials().getPassword() != credentialsDto.getPassword()) {
			throw new BadRequestException("User credentials are invalid.");
		}
		userToDelete.setDeleted(true);
		return userMapper.entityToResponseDto(userRepository.saveAndFlush(userToDelete));
	}
	


    private final TweetMapper tweetMapper;


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
        return tweetMapper.entitiesToDtos(newestToOldest(removeDeleted(checkUsername(username).getMentions())));
    }

    @Override //GET users/@{username}/tweets
    public List<TweetResponseDto> getUsernameTweets(String username) {
        return tweetMapper.entitiesToDtos(newestToOldest(removeDeleted(checkUsername(username).getTweets())));
    }

    @Override //GET users/@{username}/feed
    public List<TweetResponseDto> getFeed(String username) {
        User user = checkUsername(username);
        List<User> following = removeDeleted(user.getFollowing());
        List<Tweet> tweets = removeDeleted(user.getTweets());
        for (User followed : following) {
            tweets.addAll(removeDeleted(followed.getTweets()));
        }
        return tweetMapper.entitiesToDtos(newestToOldest(tweets));
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


