package com.payMyBuddy.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payMyBuddy.model.User;
import com.payMyBuddy.model.UserAccountInformations;

@Repository
public interface UserAccountInfomationsRepository extends JpaRepository<UserAccountInformations, Long>  {

}
