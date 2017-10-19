package com.accountbook.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.accountbook.dao.AccountDao;
import com.accountbook.model.Account;
import com.accountbook.model.Member;
import com.accountbook.model.PayResult;
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
	
	public List<Account> findAccounts(String userId) {
		return findAccounts(userId, null);
	}
	@SuppressWarnings("serial")
	public List<Account> findAccounts(String userId,String bookId) {
		List<Account> accounts = dao.queryAccountsByUserIdAndBookId(new HashMap<String,String>(){
			{
				put("userId", userId);
				if(bookId==null)
					put("bookId", "");
				else
					put("bookId", bookId);
			}
		});
		for(Account account:accounts){
			account.setMembers((ArrayList<Member>) dao.queryMembersByAccountId(account.getId()));
			
			
			List<PayTarget> payTargets = dao.queryPayTargetByAccountId(account.getId());
			if(payTargets!=null && payTargets.size()>0){
				account.setPayResult(new ArrayList<PayResult>());
				account.getPayResult().add(new PayResult());
				account.getPayResult().get(0).setPayTarget((ArrayList<PayTarget>) payTargets);
			}
		}
		return accounts;
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
