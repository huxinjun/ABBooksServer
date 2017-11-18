package com.accountbook.dao;


import java.util.List;
import java.util.Map;

import com.accountbook.model.Account;
import com.accountbook.model.Member;
import com.accountbook.model.PayOffset;
import com.accountbook.model.PayTarget;
import com.accountbook.model.SummaryInfo;

public interface AccountDao {
	
	public void insert(Account account);
	public void insertMember(Member member);
	public void insertPayTarget(PayTarget target);
	public void insertPayOffset(PayOffset offset);
	
	
	
	
	
	
	public Account queryAccount(String accountId);
	public List<Account> queryMyAccounts(Map<String,Object> map);
	public List<Account> queryMyAccountsByBookId(Map<String,Object> map);
	public List<Account> queryTwoPersonAccounts(Map<String,Object> map);
	
	
	public List<Member> queryMembersByAccountId(String accountId);
	public PayTarget queryPayTarget(String targetId);
	
	/**查询一个支付方案被抵扣的金额数目*/
	public double queryOffsetMoney(String targetId);
	
	public List<PayTarget> queryPayTargetByAccountId(String accountId);
	/**查询和userId相关的所有成员:分组和帐友*/
	public List<Member> queryMembers(String userId);
	/**查询userId本月支出,待付款,代收款等信息*/
	public List<SummaryInfo> queryAccountSummary(String userId);
	public List<SummaryInfo> queryAccountSummary2P(Map<String,Object> map);
	
	/**查询两个用户待付的金额:user1Id向user2Id待付的*/
	public double queryWaitPaidMoney(Map<String,Object> map);
	/**查询两个用户之间最早的一个未付清的支付方案*/
	public PayTarget queryEarliestNotSettledTarget(Map<String,Object> map);
	
	
	//更新支付方案
	public void updatePayTarget(PayTarget target);
	public void deletePayTarget(String targetId);
	public void deletePayTargets(String accountId);

	public void deleteMember(String id);
}
