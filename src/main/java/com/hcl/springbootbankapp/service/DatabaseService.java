package com.hcl.springbootbankapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hcl.springbootbankapp.entity.Account;
import com.hcl.springbootbankapp.entity.TransactionHistory;
import com.hcl.springbootbankapp.entity.User;
import com.hcl.springbootbankapp.model.FundTransferRequest;
import com.hcl.springbootbankapp.repository.AccountRepository;
import com.hcl.springbootbankapp.repository.TransactionHistoryRepository;
import com.hcl.springbootbankapp.repository.UserRepository;

@Service
public class DatabaseService {

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

	public List<TransactionHistory> loginUser(User pUser) throws Exception {
		Optional<User> lUser = userRepository.findByUsernameAndPassword(pUser.getUsername(), pUser.getPassword());
		
		boolean isPresent = lUser.isPresent();
		if (isPresent) {
			Account lAccount = accountRepository.findByUserName(lUser.get().getUsername());

			Pageable sortedByTransactionTime = PageRequest.of(0, 2, Sort.by("transactionTime").descending());
			List<TransactionHistory> lTenTransactionByAccountNo = transactionHistoryRepository
					.findByAccountNo(lAccount.getAccountNo(), sortedByTransactionTime);

			return lTenTransactionByAccountNo;
		} else {
			throw new Exception("User is not authentited.");
		}
	}

	public List<Account> getPayees(Long pAccountNo) {
		return accountRepository.findByAccountNoNotIn(pAccountNo);
	}

	public String fundTransfer(FundTransferRequest lFundTransferRequest) throws Exception {
		Account lAccount = accountRepository.findByUserName(lFundTransferRequest.getUsername());
		Long lOwnAccountNo = lAccount.getAccountNo();
		Long lPayeeAccountNo = lFundTransferRequest.getPayeeAccountNo();
		Double lTransferamt = lFundTransferRequest.getTrsandferAmt();
		List<Account> payees = accountRepository.findByAccountNoNotIn(lOwnAccountNo);

		boolean isValidPayee = false;
		for (Account laccountlist : payees) {
			if (laccountlist.getAccountNo().equals(lOwnAccountNo)) {
				isValidPayee = true;
			}
		}

		if (isValidPayee) {
			Account lOwnAccount = accountRepository.findByAccountNo(lOwnAccountNo);
			Account lPayeeAccount = accountRepository.findByAccountNo(lPayeeAccountNo);

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
