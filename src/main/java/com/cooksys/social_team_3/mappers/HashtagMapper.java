package com.cooksys.social_team_3.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.social_team_3.entities.Hashtag;
import com.cooksys.social_team_3.dtos.HashtagDto;

@Mapper(componentModel = "spring", uses = { HashtagMapper.class })
public interface HashtagMapper {

	List<HashtagDto> entitiesToDtos(List<Hashtag> hashtags);
	
	Hashtag requestDtoToEntity(String hashtag);
		
	HashtagDto entityToDto(Hashtag hashtag);
}