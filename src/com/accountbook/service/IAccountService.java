package com.accountbook.service;

import java.util.List;

import com.accountbook.model.Account;
import com.accountbook.model.Member;
import com.accountbook.model.PayTarget;
import com.accountbook.model.SummaryInfo;

public interface IAccountService {
	
	/**
	 * 记账
	 * @param account
	 */
	public void addNewAccount(Account account);
	public void addMember(Member member);
	public void addPayTarget(PayTarget target);
	
	public Account findAccount(String accountId);
	public List<Account> findAccounts(String userId);
	public List<Account> findAccounts(String userId,String bookId);
	public List<Account> findAccounts2P(String user1Id,String user2Id, Integer pageIndex, Integer pageSize);
	public int findAccounts2PCount(String user1Id,String user2Id);
	
	public List<Member> findAllMembers(String userId);
	public PayTarget findPayTarget(String targetId);
	
	public List<SummaryInfo> getSummarySimpleInfo(String userId);
	
	//更新支付方案中的状态
	public void updatePayTarget(PayTarget target);
	public void deletePayTarget(String targetId);
	public void deletePayTargets(String accountId);
	
	
	public void deleteMember(String id);
	
}
