package com.accountbook.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.AccountDao;
import com.accountbook.modle.Member;
import com.accountbook.service.IAccountService;

@Service
public class AccountServiceImpl implements IAccountService {
	
	@Autowired
	AccountDao dao;

	@Override
	public List<Member> findAllMembers(String userId) {
		return dao.queryMembers(userId);
	}

}
