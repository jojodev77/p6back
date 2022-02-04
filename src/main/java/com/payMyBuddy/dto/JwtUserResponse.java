package com.payMyBuddy.dto;

import java.util.List;
import java.util.Set;

import com.payMyBuddy.model.Role;
import com.payMyBuddy.model.User;
import com.payMyBuddy.model.UserAccountInformations;

import lombok.Data;

@Data
public class JwtUserResponse {
String jwt;
public JwtUserResponse(String jwt, Long id, String email, String displayName, List<String> roles,
		UserAccountInformations userAccountInformations) {
	super();
	this.jwt = jwt;
	this.id = id;
	this.email = email;
	this.displayName = displayName;
	this.role = roles;
	this.userAccountInformations = userAccountInformations;
}
Long id;
String email;
String displayName;
List<String> role;
UserAccountInformations userAccountInformations;
}
