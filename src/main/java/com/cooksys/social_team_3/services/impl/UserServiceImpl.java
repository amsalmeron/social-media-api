package com.cooksys.social_team_3.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_team_3.dtos.CredentialsDto;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.dtos.UserRequestDto;
import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.entities.Credentials;
import com.cooksys.social_team_3.entities.Tweet;
import com.cooksys.social_team_3.entities.User;
import com.cooksys.social_team_3.exceptions.BadRequestException;
import com.cooksys.social_team_3.exceptions.NotFoundException;
import com.cooksys.social_team_3.mappers.CredentialsMapper;
import com.cooksys.social_team_3.mappers.ProfileMapper;
import com.cooksys.social_team_3.mappers.TweetMapper;
import com.cooksys.social_team_3.mappers.UserMapper;
import com.cooksys.social_team_3.repositories.UserRepository;
import com.cooksys.social_team_3.services.Deletables;
import com.cooksys.social_team_3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	private final UserMapper userMapper;
	private final ProfileMapper profileMapper;
	private final CredentialsMapper credentialsMapper;
	private final TweetMapper tweetMapper;

	@Override
	public User getUser(String username) {
		Optional<User> optionalUser = userRepository.findByCredentials_Username(username);

		if (optionalUser.isEmpty()) {
			throw new NotFoundException("No user found with the username: " + username);
		}
		return optionalUser.get();
	}

	@Override
	public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
		User userToUpdate = getUser(username);
		Credentials credentialCheck = credentialsMapper.requestDtoToEntity(userRequestDto.getCredentials());
		if (credentialCheck.equals(userToUpdate.getCredentials())) {
			userToUpdate.setProfile(profileMapper.requestDtoToEntity(userRequestDto.getProfile()));
			return userMapper.entityToResponseDto(userRepository.saveAndFlush(userToUpdate));
		}
		throw new BadRequestException("Unable to update profile, credentials are not valid.");
	}

	@Override
	public UserResponseDto deleteUser(String username, UserRequestDto userRequestDto){
		User userToDelete = getUser(username);
		Credentials credentialCheck = credentialsMapper.requestDtoToEntity(userRequestDto.getCredentials());
		if (credentialCheck.equals(userToDelete.getCredentials())) {
			userToDelete.setDeleted(true);
			return userMapper.entityToResponseDto(userRepository.saveAndFlush(userToDelete));
		}
		throw new BadRequestException("User credentials are invalid.");
	}

    @Override //POST Users
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        createUserRequest(userRequestDto);
        String username = userRequestDto.getCredentials().getUsername();

        User userToSave = userMapper.requestDtoToEntity(userRequestDto);
        Optional<User> optionalUser = userRepository.findByCredentials_Username(username);
        User user = optionalUser.map(this::createUserExists).orElseGet(() -> createUserNotExists(userToSave));

        return userMapper.entityToResponseDto(user);
    }

    @Override //GET Users
    public List<UserResponseDto> getAllUsers() {
        return userMapper.entitiesToDtos(userRepository.findAllByDeleted(false));
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
        return userMapper.entitiesToDtos(removeDeleted(checkUsername(username).getFollowing()));
    }

    @Override // GET users/@{username}/followers
	public List<UserResponseDto> getFollowers(String username) {
		return userMapper.entitiesToDtos(removeDeleted(checkUsername(username).getFollowers()));
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
    public void followUser(String username, Credentials credential) {
        User toBeFollowed = checkUsername(username);
        User follower = checkCredentials(credential);
        if (isActive(toBeFollowed))
            throw new NotFoundException("User not found");
        if (follower.getFollowing().contains(toBeFollowed))
            throw new BadRequestException("User is already followed");
        follower.addFollowing(toBeFollowed);
        userRepository.saveAndFlush(follower);
        userRepository.saveAndFlush(toBeFollowed);
    }

    @Override
    public void unfollowUser(String username, Credentials credential) {
        User toBeUnfollowed = checkUsername(username);
        User follower = checkCredentials(credential);
        if (isActive(toBeUnfollowed))
            throw new NotFoundException("User not found");
        if (!follower.getFollowing().contains(toBeUnfollowed))
            throw new BadRequestException("User is not being followed");
        follower.removeFollowing(toBeUnfollowed);
        userRepository.saveAndFlush(follower);
        userRepository.saveAndFlush(toBeUnfollowed);
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

    private User checkCredentials(Credentials credential) {
        Optional<User> userOptional = userRepository.findByCredentials(credential);
        if (userOptional.isEmpty() || userOptional.get().isDeleted())
            throw new BadRequestException("Incorrect credentials");
        return userOptional.get();
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

    private boolean isActive(User user) {
        return user == null || user.isDeleted();
    }

    private void createUserRequest(UserRequestDto userRequestDto) {
        if (userRequestDto.getCredentials() == null || userRequestDto.getCredentials().getUsername() == null
                || userRequestDto.getCredentials().getPassword() == null || userRequestDto.getProfile() == null
                || userRequestDto.getProfile().getEmail() == null) {
            throw new BadRequestException("All fields must be completed");
        }
    }

    private User createUserExists(User user) {
        if (user.isDeleted()) {
            user.setDeleted(false);
            userRepository.saveAndFlush(user);
            return user;
        }
        throw new BadRequestException("User already exists");
    }

    private User createUserNotExists(User userToSave) {
        userRepository.saveAndFlush(userToSave);
        return userToSave;
    }

}
