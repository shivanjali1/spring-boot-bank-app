package com.hcl.springbootbankapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.springbootbankapp.entity.Account;
import com.hcl.springbootbankapp.entity.User;
import com.hcl.springbootbankapp.repository.AccountRepository;
import com.hcl.springbootbankapp.repository.TransactionHistoryRepository;
import com.hcl.springbootbankapp.repository.UserRepository;

@Service
public class RegistrationService {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	TransactionHistoryRepository transactionHistoryRepository;
	
	public User registerUser(User lUser) {
		User lSavedUser = userRepository.save(lUser);
		Long lAccountNo = (long) (Math.random() * 100000 + 3333300000L);

		Account lAccount = new Account();
		lAccount.setAccountBalance(10000.0);
		lAccount.setAccountNo(lAccountNo);
		lAccount.setUserName(lSavedUser.getUsername());

		accountRepository.save(lAccount);
		return lSavedUser;
	}
	
	public List<User> getUser() {
		return userRepository.findAll();
	}


}
