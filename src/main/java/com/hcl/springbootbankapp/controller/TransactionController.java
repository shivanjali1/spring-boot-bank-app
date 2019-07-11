package com.hcl.springbootbankapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.springbootbankapp.entity.Account;
import com.hcl.springbootbankapp.model.FundTransferRequest;
import com.hcl.springbootbankapp.service.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	@Autowired
	TransactionService transactionService;

	@GetMapping("/payee/{accountNO}")
	public ResponseEntity<?> getPayee(@PathVariable Long accountNO) {
		List<Account> payees = transactionService.getPayees(accountNO);
		return new ResponseEntity<List<Account>>(payees, HttpStatus.OK);
	}

	@PostMapping("/fundTransfer")
	public String fundTransafer(@RequestBody FundTransferRequest lFundTransferRequest) {
		String lStatus;
		try {
			validateRequest(lFundTransferRequest);
		} catch (Exception e) {
			return "Invalid Request. Error Message : " + e.getMessage();
		}

		try {
			lStatus = transactionService.fundTransfer(lFundTransferRequest);
			System.out.println(lStatus);
		} catch (Exception e) {
			return "Error Message : " + e.getMessage();
		}
		return lStatus;

	}

	private boolean validateRequest(FundTransferRequest lFundTransferRequest) throws Exception {

		if (StringUtils.isEmpty(lFundTransferRequest.getPayeeAccountNo())) {
			throw new Exception("Mandetory element missing: PayeeAccountNo");
		}
		if (StringUtils.isEmpty(lFundTransferRequest.getTrsandferAmt())) {
			throw new Exception("Mandetory element missing: Trsandfer Amount");
		}
		return true;

	}

}
