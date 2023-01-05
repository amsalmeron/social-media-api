package com.cooksys.social_team_3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cooksys.social_team_3.entities.Credentials;
import com.cooksys.social_team_3.entities.Profile;
import com.cooksys.social_team_3.entities.Tweet;
import com.cooksys.social_team_3.entities.User;
import com.cooksys.social_team_3.repositories.TweetRepository;
import com.cooksys.social_team_3.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {

	private final UserRepository userRepository;
	private final TweetRepository tweetRepository;

	@Override
	public void run(String... args) throws Exception {
		Credentials credsOne = new Credentials();
		credsOne.setUsername("AntonioUser");
		credsOne.setPassword("password");
		Profile profileOne = new Profile();
		profileOne.setFirstName("Tony");
		profileOne.setLastName("Montana");
		profileOne.setEmail("letsgo@yahoo.com");
		profileOne.setPhone("7777777");
		User user1 = new User();
		user1.setCredentials(credsOne);
		user1.setProfile(profileOne);
		userRepository.save(user1);
		
		
		Credentials credsTwo = new Credentials();
		credsTwo.setUsername("RobertUser");
		credsTwo.setPassword("password");
		Profile profileTwo = new Profile();
		profileTwo.setFirstName("Rob");
		profileTwo.setLastName("Biscuit");
		profileTwo.setEmail("rob@yahoo.com");
		profileTwo.setPhone("000000000");
		User user2 = new User();
		user2.setCredentials(credsTwo);
		user2.setProfile(profileTwo);
		userRepository.save(user2);
		
		Tweet tweet1 = new Tweet();
		tweet1.setAuthor(user1);
		tweet1.setContent("First Tweet!");
		tweetRepository.save(tweet1);
		
		Tweet tweet2 = new Tweet();
		tweet2.setAuthor(user1);
		tweet2.setContent("First Tweet!");
		tweet2.setRepostOf(tweet1);
		tweetRepository.save(tweet2);
		
	}
}
