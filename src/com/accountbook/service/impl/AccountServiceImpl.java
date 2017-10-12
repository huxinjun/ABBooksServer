package com.accountbook.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.accountbook.dao.AccountDao;
import com.accountbook.model.Account;
import com.accountbook.model.Member;
import com.accountbook.model.PayTarget;
import com.accountbook.service.IAccountService;

@Service
public class AccountServiceImpl implements IAccountService {
	
	@Autowired
	AccountDao dao;

	@Override
	public List<Member> findAllMembers(String userId) {
		return dao.queryMembers(userId);
	}

	
	@Override
	public void addNewAccount(Account account) {
		dao.insert(account);
	}


	@Override
	public void addMember(Member member) {
		dao.insertMember(member);
	}


	@Override
	public void addPayTarget(PayTarget target) {
		dao.insertPayTarget(target);
	}

}
