package com.cooksys.social_team_3.services.impl;

import org.springframework.stereotype.Service;

import java.util.List;

import com.cooksys.social_team_3.services.HashtagService;
import com.cooksys.social_team_3.dtos.HashtagDto;
import com.cooksys.social_team_3.repositories.HashtagRepository;
import com.cooksys.social_team_3.mappers.HashtagMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HashtagServiceImpl implements HashtagService {
	
	private final HashtagRepository hashtagRepository;
	private final HashtagMapper hashtagMapper;
	
	
	//Endpoint #62 mhu
	@Override
	public List<HashtagDto> getAllHashtags() {

		return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
		
	}

}
