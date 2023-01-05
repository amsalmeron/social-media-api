package com.cooksys.social_team_3.mappers;

import org.mapstruct.Mapper;

import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.entities.User;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {

	UserResponseDto entityToResponseDto(User entity);

}
