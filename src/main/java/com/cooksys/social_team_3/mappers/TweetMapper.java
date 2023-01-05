package com.cooksys.social_team_3.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.social_team_3.dtos.CredentialsDto;
import com.cooksys.social_team_3.dtos.TweetResponseDto;
import com.cooksys.social_team_3.entities.Credentials;
import com.cooksys.social_team_3.entities.Tweet;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper {

	List<TweetResponseDto> entitiesToDtos(List<Tweet> tweet);

	TweetResponseDto entityToDto(Tweet entity);
	
	CredentialsDto verifyCredentials(Credentials credential);
	
}