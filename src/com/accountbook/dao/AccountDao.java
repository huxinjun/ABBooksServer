package com.accountbook.dao;


import java.util.List;
import java.util.Map;

import com.accountbook.model.Account;
import com.accountbook.model.Member;
import com.accountbook.model.Offset;
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
	/**查询userId当日支出,支出账单个数等信息*/
	public List<SummaryInfo> queryAccountSummaryToday(String userId);
	public List<SummaryInfo> queryAccountSummary2P(Map<String,Object> map);
	
	/**查询两个用户待付的金额:user1Id向user2Id待付的*/
	public double queryWaitPaidMoney(Map<String,Object> map);
	/**查询两个用户之间最早的一个未付清的支付方案*/
	public PayTarget queryEarliestNotSettledTarget(Map<String,Object> map);
	
	/**查询抵消记录*/
	public List<PayOffset> queryOriginOffsets(String payId);
	/**查询抵消记录,包装好了头像等,适用于列表显示*/
	public List<Offset> queryOffsets(String payId);
	
	//更新支付方案
	public void updatePayTarget(PayTarget target);
	public void deletePayTarget(String targetId);
	public void deletePayTargets(String accountId);
	public void deleteMember(String id);
	
	
	public void deleteAccount(String accountId);
	public void deleteOffset(String offsetId);
	
	
	public void updateMemberIcon(String userId,String memberIcon);
}
