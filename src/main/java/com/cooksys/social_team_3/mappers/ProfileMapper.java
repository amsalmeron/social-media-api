package com.cooksys.social_team_3.mappers;

import org.mapstruct.Mapper;

import com.cooksys.social_team_3.dtos.ProfileDto;
import com.cooksys.social_team_3.entities.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

	Profile requestDtoToEntity(ProfileDto profileDto); 
}
