package com.worldline.fpl.recruitment.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.worldline.fpl.recruitment.dao.TransactionRepository;
import com.worldline.fpl.recruitment.entity.Transaction;
import com.worldline.fpl.recruitment.exception.ServiceException;
import com.worldline.fpl.recruitment.json.ErrorCode;
import com.worldline.fpl.recruitment.json.TransactionResponse;

/**
 * Transaction service
 * 
 * @author A525125
 *
 */
@Service
public class TransactionService {

	private AccountService accountService;

	private TransactionRepository transactionRepository;

	@Autowired
	public TransactionService(AccountService accountService,
			TransactionRepository transactionRepository) {
		this.accountService = accountService;
		this.transactionRepository = transactionRepository;
	}

	/**
	 * Get transactions by account
	 * 
	 * @param accountId
	 *            the account id
	 * @param p
	 *            the pageable object
	 * @return
	 */
	public Page<TransactionResponse> getTransactionsByAccount(String accountId,
			Pageable p) {
		if (!accountService.isAccountExist(accountId)) {
			throw new ServiceException(ErrorCode.INVALID_ACCOUNT,
					"Account doesn't exist");
		}
		return new PageImpl<TransactionResponse>(transactionRepository
				.getTransactionsByAccount(accountId, p).getContent().stream()
				.map(this::map).collect(Collectors.toList()));
	}

	/**
	 * Map {@link Transaction} to {@link TransactionResponse}
	 * 
	 * @param transaction
	 * @return
	 */
	private TransactionResponse map(Transaction transaction) {
		TransactionResponse result = new TransactionResponse();
		result.setBalance(transaction.getBalance());
		result.setId(transaction.getId());
		result.setNumber(transaction.getNumber());
		return result;
	}

	public void removeTransactionByAccount(String accountId, String transactionId) {
		if (!accountService.isAccountExist(accountId)) {
			throw new ServiceException(ErrorCode.INVALID_ACCOUNT,
					"Account doesn't exist");
		}
		if(!transactionRepository.isTransactionExist(transactionId, accountId)){
			throw new ServiceException(ErrorCode.INVALID_TRANSACTION,
					"Transaction doesn't exist");
		}
		transactionRepository.removeTransaction(transactionId);
		
	}

}
