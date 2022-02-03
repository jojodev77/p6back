package com.payMyBuddy.dto;

import java.util.List;

import com.payMyBuddy.model.UserAccountInformations;

import lombok.Data;
import lombok.Value;

@Value
public class UserInfo {
	private String id, displayName, email;
	private List<String> roles;
}