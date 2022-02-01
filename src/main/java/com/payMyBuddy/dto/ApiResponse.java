package com.payMyBuddy.dto;

import lombok.Data;
import lombok.Value;

@Value
@Data
public class ApiResponse {
	private Boolean success;
	private String message;
}