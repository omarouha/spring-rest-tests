package com.worldline.fpl.recruitment.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.worldline.fpl.recruitment.dao.TransactionRepository;
import com.worldline.fpl.recruitment.entity.Transaction;

/**
 * Implementation of {@link TransactionRepository}
 * 
 * @author A525125
 *
 */
@Repository
public class TransactionRepositoryImpl implements TransactionRepository,
		InitializingBean {

	private List<Transaction> transactions;

	@Override
	public void afterPropertiesSet() throws Exception {
		transactions = new ArrayList<>();
		{
			Transaction transaction = new Transaction();
			transaction.setAccountId("1");
			transaction.setBalance(BigDecimal.valueOf(42.12));
			transaction.setId("1");
			transaction.setNumber("12151885120");
			transactions.add(transaction);
		}
		{
			Transaction transaction = new Transaction();
			transaction.setAccountId("1");
			transaction.setBalance(BigDecimal.valueOf(456.00));
			transaction.setId("2");
			transaction.setNumber("12151885121");
			transactions.add(transaction);
		}
		{
			Transaction transaction = new Transaction();
			transaction.setAccountId("1");
			transaction.setBalance(BigDecimal.valueOf(-12.12));
			transaction.setId("3");
			transaction.setNumber("12151885122");
			transactions.add(transaction);
		}
	}

	@Override
	public Page<Transaction> getTransactionsByAccount(String accountId,
			Pageable p) {
		return new PageImpl<Transaction>(transactions.stream()
				.filter(t -> t.getAccountId().equals(accountId))
				.collect(Collectors.toList()));
	}

	@Override
	public boolean isTransactionExist(String transactionId, String accountId) {
		return transactions.stream().anyMatch(t -> t.getAccountId().equals(accountId) && t.getId().equals(transactionId));
	}

	@Override
	public void removeTransaction(String transactionId) {
		transactions.removeIf(t -> t.getId().equals(transactionId));
	}

}
