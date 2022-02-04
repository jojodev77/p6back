package com.payMyBuddy.dto;

import com.payMyBuddy.model.User;

import lombok.Data;
import lombok.Value;

@Value
@Data
public class JwtAuthenticationResponse {
	private String accessToken;
	private User user;
}