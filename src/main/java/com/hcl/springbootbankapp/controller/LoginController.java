package com.hcl.springbootbankapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.springbootbankapp.entity.TransactionHistory;
import com.hcl.springbootbankapp.entity.User;
import com.hcl.springbootbankapp.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	LoginService loginService;
	
	@PostMapping("/user")
	public ResponseEntity<?> loginUser(@RequestBody User lUser){
		List<TransactionHistory> lTransactions=null;
		try {
			 lTransactions = loginService.loginUser(lUser);
			System.out.println("User is sucessfully login");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<List<TransactionHistory>>(lTransactions, HttpStatus.OK);
	}

}
