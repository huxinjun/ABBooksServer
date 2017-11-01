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
import com.accountbook.model.SummaryInfo;
import com.accountbook.service.IAccountService;
import com.accountbook.utils.CommonUtils;
import com.accountbook.utils.CommonUtils.Limit;

@Service
public class AccountServiceImpl implements IAccountService {
	
	@Autowired
	AccountDao dao;

	@Override
	public List<Member> findAllMembers(String userId) {
		return dao.queryMembers(userId);
	}
	
	@Override
	public Account findAccount(String accountId) {
		Account account = dao.queryAccount(accountId);
		account.setMembers((ArrayList<Member>) dao.queryMembersByAccountId(account.getId()));
		
		List<PayTarget> payTargets = dao.queryPayTargetByAccountId(account.getId());
		if(payTargets!=null && payTargets.size()>0){
			account.setPayResult(new ArrayList<PayResult>());
			account.getPayResult().add(new PayResult());
			account.getPayResult().get(0).setPayTarget((ArrayList<PayTarget>) payTargets);
		}
		return account;
	}
	
	public List<Account> findAccounts(String userId) {
		return findAccounts(userId, null);
	}
	public List<Account> findAccounts(String userId,String bookId) {
		List<Account> accounts;
		if(bookId==null)
			accounts=dao.queryMyAccounts(userId);
		else
			accounts=dao.queryMyAccountsByBookId(userId,bookId);
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

	@Override
	public void updatePayTarget(PayTarget target) {
		dao.updatePayTarget(target);
	}

	@Override
	public void deletePayTargets(String accountId) {
		dao.deletePayTargets(accountId);
	}

	@Override
	public void deletePayTarget(String targetId) {
		dao.deletePayTarget(targetId);
	}

	@Override
	public void deleteMember(String id) {
		dao.deleteMember(id);
	}

	@Override
	public List<SummaryInfo> getSummarySimpleInfo(String userId) {
		return dao.queryAccountSummarySimple(userId);
	}

	@Override
	public PayTarget findPayTarget(String targetId) {
		return dao.queryPayTarget(targetId);
	}

	@Override
	public List<Account> findAccounts2P(String user1Id, String user2Id,Integer pageIndex, Integer pageSize) {
		
		
		Limit limit = CommonUtils.getLimit(pageIndex, pageSize);
			
		return dao.queryTwoPersonAccounts(new HashMap<String,Object>(){
			private static final long serialVersionUID = 1L;
			{
				put("user1Id", user1Id);
				put("user2Id", user2Id);
				put("ls", limit.start);
				put("le", limit.end);
			}
		});
	}

	@Override
	public int findAccounts2PCount(String user1Id, String user2Id) {
		return dao.queryTwoPersonAccountsCount(user1Id, user2Id);
	}

	

}
