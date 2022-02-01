package com.payMyBuddy.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "userAccountInformations")
public class UserAccountInformations {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	@OneToOne
	User user;
	
	String accountReferenceTransaction;
	
	int number_account;
	
	double sold_account;
	
	@OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	UserPartnerAccount userPartner_account;
	
	@OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	HistoryTransaction historyTransaction;
	
	boolean state;
	
	
}
