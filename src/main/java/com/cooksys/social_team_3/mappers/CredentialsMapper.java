package com.cooksys.social_team_3.mappers;

import org.mapstruct.Mapper;

import com.cooksys.social_team_3.dtos.CredentialsDto;
import com.cooksys.social_team_3.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

	Credentials requestDtoToEntity(CredentialsDto credentialsDto);
	
	CredentialsDto entityToDto(Credentials credentials);
}
