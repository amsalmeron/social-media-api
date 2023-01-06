package com.cooksys.social_team_3.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.social_team_3.services.ValidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {

	private final ValidateService validateService;

	@GetMapping("/username/exists/@{username}")
	public boolean usernameExists(@PathVariable String username) {
		return validateService.usernameExists(username);
	}

	@GetMapping("/username/available/@{username}")
	public boolean usernameIsAvailable(@PathVariable String username) {
		return validateService.usernameIsAvailable(username);
	}

	@GetMapping("/tag/exists/{label}")
	public Boolean hashtagExists(@PathVariable String label) {
		return validateService.hashtagExists(label);
	}

}
