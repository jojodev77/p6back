package com.payMyBuddy.service;

import org.springframework.stereotype.Service;

import com.payMyBuddy.dto.SignUpRequest;
import com.payMyBuddy.model.User;
import com.payMyBuddy.model.UserAccountInformations;
import com.payMyBuddy.model.UserPersonnalInformations;

@Service
public class UserAccountRegistrationService {

	public UserAccountInformations attributeAccountInformations(User userPersonnalInformations_dto) {
		UserAccountInformations userAccountInformations = new UserAccountInformations();
		userAccountInformations.setSoldAccount(0);
		String account_reference_transaction = "pmb" + userPersonnalInformations_dto.getEmail().substring(0, 1) + userPersonnalInformations_dto.getEmail().substring(2, 5) + userPersonnalInformations_dto.getDisplayName().substring(2, 5) + "b";
		userAccountInformations.setAccountReferenceTransaction(account_reference_transaction);
		userAccountInformations.setUser(userPersonnalInformations_dto);
		return userAccountInformations;
	}
}
