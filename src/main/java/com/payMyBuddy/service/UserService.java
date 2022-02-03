package com.payMyBuddy.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import com.payMyBuddy.dto.LocalUser;
import com.payMyBuddy.dto.SignUpRequest;
import com.payMyBuddy.dto.UserReferenceTransaction;
import com.payMyBuddy.exception.UserAlreadyExistAuthenticationException;
import com.payMyBuddy.model.User;

/**
 * @author Chinna
 * @since 26/3/18
 */
public interface UserService {

	public User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;

	User findUserByEmail(String email);

	Optional<User> findUserById(Long id);

	LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);
	
//	ArrayList<UserReferenceTransaction> listReferenceTransaction();
//	
//	ArrayList<User> listUser();
}
