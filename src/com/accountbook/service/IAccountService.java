package com.accountbook.service;

import java.util.List;

import com.accountbook.modle.Account;
import com.accountbook.modle.Member;

public interface IAccountService {
	
	/**
	 * 记账
	 * @param account
	 */
	public void addNewAccount(Account account);
	
	public List<Member> findAllMembers(String userId);
}
