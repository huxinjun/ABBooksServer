package com.accountbook.dao;


import java.util.List;
import java.util.Map;

import com.accountbook.model.Account;
import com.accountbook.model.Member;
import com.accountbook.model.PayTarget;

public interface AccountDao {
	
	public void insert(Account account);
	public void insertMember(Member member);
	public void insertPayTarget(PayTarget target);
	
	
	
	
	
	
	public Account queryAccount(String accountId);
	/**
	 * 查询一个用户的所有
	 * map中填写两个参数:
	 * 1.userId
	 * 2.bookId
	 */
	public List<Account> queryAccountsByUserIdAndBookId(Map<String,String> map);
	public List<Member> queryMembersByAccountId(String accountId);
	public List<PayTarget> queryPayTargetByAccountId(String accountId);
	/**查询和userId相关的所有成员:分组和帐友*/
	public List<Member> queryMembers(String userId);
	
	
	
	
	//更新支付方案
	public void updatePayTarget(PayTarget target);
	public void deletePayTarget(String targetId);
	public void deletePayTargets(String accountId);

	public void deleteMember(String id);
}
