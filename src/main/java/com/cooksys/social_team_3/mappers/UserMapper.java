package com.cooksys.social_team_3.mappers;

import com.cooksys.social_team_3.dtos.UserRequestDto;
import com.cooksys.social_team_3.dtos.UserResponseDto;
import com.cooksys.social_team_3.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {

    List<UserResponseDto> entitiesToDtos(List<User> users);

    @Mapping(target = "username", source = "credentials.username")
    UserResponseDto entityToResponseDto(User user);

    User requestDtoToEntity(UserRequestDto userRequestDto);

    User dtoToEntity(UserRequestDto userRequestDto);
}
