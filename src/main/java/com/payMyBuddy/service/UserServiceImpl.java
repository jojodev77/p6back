package com.payMyBuddy.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.payMyBuddy.dto.JwtUserResponse;
import com.payMyBuddy.dto.LocalUser;
import com.payMyBuddy.dto.SignUpRequest;
import com.payMyBuddy.dto.SocialProvider;
import com.payMyBuddy.dto.UserReferenceTransaction;
import com.payMyBuddy.exception.OAuth2AuthenticationProcessingException;
import com.payMyBuddy.exception.UserAlreadyExistAuthenticationException;
import com.payMyBuddy.model.Role;
import com.payMyBuddy.model.User;
import com.payMyBuddy.model.UserAccountInformations;
import com.payMyBuddy.repo.RoleRepository;
import com.payMyBuddy.repo.UserAccountInfomationsRepository;
import com.payMyBuddy.repo.UserRepository;
import com.payMyBuddy.security.oauth2.user.OAuth2UserInfo;
import com.payMyBuddy.security.oauth2.user.OAuth2UserInfoFactory;
import com.payMyBuddy.util.GeneralUtils;

/**
 * @author Chinna
 * @since 26/3/18
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserAccountRegistrationService userAccountRegistrationService;
	
	@Autowired
	private UserAccountInfomationsRepository userAccountInfomationsRepository;

	@Override
	@Transactional(value = "transactionManager")
	public User registerNewUser(final SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException {
		if (signUpRequest.getUserID() != null && userRepository.existsById(signUpRequest.getUserID())) {
			throw new UserAlreadyExistAuthenticationException("User with User id " + signUpRequest.getUserID() + " already exist");
		} else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new UserAlreadyExistAuthenticationException("User with email id " + signUpRequest.getEmail() + " already exist");
		}
		User user = buildUser(signUpRequest);
		Date now = Calendar.getInstance().getTime();
		user.setCreatedDate(now);
		user.setModifiedDate(now);
		user.setUserAccountInformations(userAccountRegistrationService.attributeAccountInformations(user));
		//user.getUserAccountInformations().setUser(user);
		userRepository.flush();
		return user;
	}

	private User buildUser(final SignUpRequest formDTO) {
		User user = new User();
		user.setDisplayName(formDTO.getDisplayName());
		user.setEmail(formDTO.getEmail());
		user.setPassword(passwordEncoder.encode(formDTO.getPassword()));
		final HashSet<Role> roles = new HashSet<Role>();
		roles.add(roleRepository.findByName(Role.ROLE_USER));
		user.setRoles(roles);
		user.setProvider(formDTO.getSocialProvider().getProviderType());
		user.setEnabled(true);
		user = userRepository.save(user);
		return user;
	}

	@Override
	public User findUserByEmail(final String email) {
		return userRepository.findByEmail(email);
	}
	



	@Override
	@Transactional
	public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
		if (StringUtils.isEmpty(oAuth2UserInfo.getName())) {
			throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
		} else if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
			throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}
		SignUpRequest userDetails = toUserRegistrationObject(registrationId, oAuth2UserInfo);
		User user = findUserByEmail(oAuth2UserInfo.getEmail());
		if (user != null) {
			if (!user.getProvider().equals(registrationId) && !user.getProvider().equals(SocialProvider.LOCAL.getProviderType())) {
				throw new OAuth2AuthenticationProcessingException(
						"Looks like you're signed up with " + user.getProvider() + " account. Please use your " + user.getProvider() + " account to login.");
			}
			user = updateExistingUser(user, oAuth2UserInfo);
		} else {
			user = registerNewUser(userDetails);
		}

		return LocalUser.create(user, attributes, idToken, userInfo);
	}

	private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
		existingUser.setDisplayName(oAuth2UserInfo.getName());
		return userRepository.save(existingUser);
	}

	private SignUpRequest toUserRegistrationObject(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
		return SignUpRequest.getBuilder().addProviderUserID(oAuth2UserInfo.getId()).addDisplayName(oAuth2UserInfo.getName()).addEmail(oAuth2UserInfo.getEmail())
				.addSocialProvider(GeneralUtils.toSocialProvider(registrationId)).addPassword("changeit").build();
	}

	@Override
	public Optional<User> findUserById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public JwtUserResponse getJwtUserResponseByEmail(String jwt, String email) {
		if (email == null) {
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email client is null");
		}
		Optional<User> user = Optional.ofNullable(userRepository.foundByEmail(email));
		if (!user.isPresent()) {
			ResponseEntity.status(HttpStatus.NO_CONTENT).body("not user found");
		}
		List<String> roles = new ArrayList<String>();
		roles.add(Role.ROLE_USER);
	UserAccountInformations userAccountInformations = new UserAccountInformations();
	userAccountInformations.setSoldAccount(user.get().getUserAccountInformations().getSoldAccount());
		JwtUserResponse jwtUserResponse = new JwtUserResponse(jwt,user.get().getId(),  user.get().getEmail(),user.get().getDisplayName(),roles, userAccountInformations);
		System.out.println("PUTINNNNNNNNNNNN" + jwtUserResponse);
		return jwtUserResponse;
	}


}