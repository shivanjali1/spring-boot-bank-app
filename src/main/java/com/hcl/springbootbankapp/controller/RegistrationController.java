package com.hcl.springbootbankapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.springbootbankapp.entity.User;
import com.hcl.springbootbankapp.model.UserDTO;
import com.hcl.springbootbankapp.service.DatabaseService;

@RestController
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	DatabaseService databaseService;

	@PostMapping("/user")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO user) {
		boolean isValidUser;
		User lRegisteredUser = null;

		try {
			isValidUser = validateUser(user);
		} catch (Exception e) {
			System.out.println("Invalid user data. Error Message : " + e.getMessage());
			return new ResponseEntity<String>("Invalid user data. Error Message : " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}

		if (isValidUser) {
			lRegisteredUser = databaseService.registerUser(user);
		}

		if (lRegisteredUser != null) {
			System.out.println("User sucessfully registered");
			return new ResponseEntity<User>(lRegisteredUser, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Not able to register User", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return databaseService.getUser();
	}

	private boolean validateUser(User user) throws Exception {

		if (StringUtils.isEmpty(user.getAddress())) {
			throw new Exception("Mandetory element missing: Address");
		}
		if (StringUtils.isEmpty(user.getdOB())) {
			throw new Exception("Mandetory element missing: Date of birth");
		}
		if (StringUtils.isEmpty(user.getEmail())) {
			throw new Exception("Mandetory element missing: Email");
		}
		if (StringUtils.isEmpty(user.getFirstName())) {
			throw new Exception("Mandetory element missing: FirstName");
		}
		if (StringUtils.isEmpty(user.getGender())) {
			throw new Exception("Mandetory element missing: Gender");
		}
		if (StringUtils.isEmpty(user.getLastName())) {
			throw new Exception("Mandetory element missing: LastName");
		}
		if (StringUtils.isEmpty(user.getUsername())) {
			throw new Exception("Mandetory element missing: Username");
		}
		if (StringUtils.isEmpty(user.getPassword())) {
			throw new Exception("Mandetory element missing: Password");
		}
		if (StringUtils.isEmpty(user.getPhoneNumber())) {
			throw new Exception("Mandetory element missing: PhoneNumber");
		}
		return true;

	}

}
