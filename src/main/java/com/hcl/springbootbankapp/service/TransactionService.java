package com.hcl.springbootbankapp.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.springbootbankapp.entity.Account;
import com.hcl.springbootbankapp.entity.TransactionHistory;
import com.hcl.springbootbankapp.model.FundTransferRequest;
import com.hcl.springbootbankapp.repository.AccountRepository;
import com.hcl.springbootbankapp.repository.TransactionHistoryRepository;
import com.hcl.springbootbankapp.repository.UserRepository;

@Service
public class TransactionService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	TransactionHistoryRepository transactionHistoryRepository;

	public List<Account> getPayees(Long pAccountNo) {
		return accountRepository.findByAccountNoNotIn(pAccountNo);
	}

	@Transactional
	public String fundTransfer(FundTransferRequest lFundTransferRequest) throws Exception {
		Account lAccount = accountRepository.findByUserName(lFundTransferRequest.getUsername());
		Long lOwnAccountNo = lAccount.getAccountNo();
		Long lPayeeAccountNo = lFundTransferRequest.getPayeeAccountNo();
		Double lTransferamt = lFundTransferRequest.getTrsandferAmt();
		List<Account> payees = accountRepository.findByAccountNoNotIn(lOwnAccountNo);

		boolean isValidPayee = false;
		for (Account laccountlist : payees) {
			if (!laccountlist.getAccountNo().equals(lOwnAccountNo)) {
				isValidPayee = true;
			}
		}

		if (isValidPayee) {
			Account lOwnAccount = accountRepository.findByAccountNo(lOwnAccountNo);
			Account lPayeeAccount = accountRepository.findByAccountNo(lPayeeAccountNo);
			
			lOwnAccount.setAccountBalance(lOwnAccount.getAccountBalance() - lTransferamt);
			accountRepository.save(lOwnAccount);
			
			lPayeeAccount.setAccountBalance(lPayeeAccount.getAccountBalance() + lTransferamt);
			accountRepository.save(lPayeeAccount);

			TransactionHistory lOwnTransactionHistory = new TransactionHistory();
			lOwnTransactionHistory.setAccountNo(lOwnAccountNo);
			lOwnTransactionHistory.setFinalBalance(lOwnAccount.getAccountBalance() - lTransferamt);
			lOwnTransactionHistory.setTransactionTime(LocalDateTime.now());
			lOwnTransactionHistory.setTransactionType("Debit");
			lOwnTransactionHistory.setTrsansactionAmt(lTransferamt);

			transactionHistoryRepository.save(lOwnTransactionHistory);

			TransactionHistory lPayeeTransactionHistory = new TransactionHistory();
			lPayeeTransactionHistory.setAccountNo(lPayeeAccountNo);
			lPayeeTransactionHistory.setFinalBalance(lPayeeAccount.getAccountBalance() + lTransferamt);
			lPayeeTransactionHistory.setTransactionTime(LocalDateTime.now());
			lPayeeTransactionHistory.setTransactionType("Credit");
			lPayeeTransactionHistory.setTrsansactionAmt(lTransferamt);

			transactionHistoryRepository.save(lPayeeTransactionHistory);
			
			

		}

		return "Fund transfered sucessfully";
	}
}
