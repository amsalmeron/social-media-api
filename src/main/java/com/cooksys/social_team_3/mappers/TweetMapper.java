package com.cooksys.social_team_3.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.social_team_3.entities.Tweet;
import com.cooksys.social_team_3.dtos.TweetResponseDto;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper {

	List<TweetResponseDto> entitiesToDtos(List<Tweet> tweet);
	
}