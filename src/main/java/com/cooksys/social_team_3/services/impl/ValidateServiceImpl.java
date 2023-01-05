package com.cooksys.social_team_3.services.impl;

import org.springframework.stereotype.Service;

import java.util.List;

import com.cooksys.social_team_3.services.ValidateService;
import com.cooksys.social_team_3.entities.Hashtag;
import com.cooksys.social_team_3.entities.User;
import com.cooksys.social_team_3.repositories.HashtagRepository;
import com.cooksys.social_team_3.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ValidateServiceImpl implements ValidateService{

    private final HashtagRepository hashtagRepository;
    private final UserRepository userRepository;

	//Endpoint #45 mhu
	@Override
	public boolean usernameIsAvailable(String username) {

		List<User> allUsers = userRepository.findAll();
		for (User user : allUsers) {
			if(user.getCredentials().getUsername().equals(username)) { 
				return false;
			}
		}
		
		return true;
	}
	
    //Endpoint #46 mhu
    @Override
	public boolean usernameExists(String username) {

		List<User> allUsers = userRepository.findAll();
		for (User user : allUsers) {
			if(user.getCredentials().getUsername().equals(username)) { 
				return true;
			}
		}
		
		return false;
	}

    //Endpoint #47 mhu
	@Override
	public boolean hashtagExists(String label) {

		List<Hashtag> allHashtags = hashtagRepository.findAll();
		for (Hashtag hashtag : allHashtags) {
			if(hashtag.getLabel().equals("#" + label)) {
				return true;
			}
		}
		
		return false;
	}
	
}
