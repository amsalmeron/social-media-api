package com.cooksys.social_team_3.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6580296965767415034L;
	
	private String message;

}
