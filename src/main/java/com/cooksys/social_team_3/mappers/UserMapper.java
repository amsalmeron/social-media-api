package com.cooksys.social_team_3.mappers;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public class UserMapper {

}
