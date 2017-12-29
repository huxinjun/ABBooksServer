package com.accountbook.service;

import java.util.List;
import com.accountbook.model.Account;
import com.accountbook.model.Member;
import com.accountbook.model.Offset;
import com.accountbook.model.AccountQueryRecord;
import com.accountbook.model.PayOffset;
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
	public void addPayOffset(PayOffset offset);
	
	public Account findAccount(String accountId);
	public List<Account> findAccounts(String userId,Integer pageIndex,Integer pageSize);
	public List<Account> findAccounts(String userId,String bookId,Integer pageIndex,Integer pageSize);
	public List<Account> findAccounts2P(String user1Id,String user2Id, Integer pageIndex, Integer pageSize);
	
	/**查询两个用户待付的金额:user1Id向user2Id待付的*/
	public double getWaitPaidMoney(String user1Id, String user2Id,String targetId);
	/**查询两个用户之间最早的一个未付清的支付方案*/
	public PayTarget findEarliestNotSettledTarget(String user1Id, String user2Id,String targetId);
	
	/**查询抵消记录*/
	public List<PayOffset> queryOriginOffsets(String payId);
	/**查询抵消记录,包装好了头像等,适用于列表显示*/
	public List<Offset> findOffsets(String payId);
	
	public List<Member> findAllMembers(String userId);
	public PayTarget findPayTarget(String targetId);
	
	
	
	//更新支付方案中的状态
	public void updatePayTarget(PayTarget target);
	public void deletePayTarget(String targetId);
	public void deletePayTargets(String accountId);
	public void deleteOffset(String offsetId);
	
	
	public void deleteMember(String id);
	public void deleteAccount(String accountId);
	
	public void updateMemberIcon(String userId,String memberIcon);
	
	
	
	//-------------------------统计相关---------------------------------------------
	public List<SummaryInfo> getSummaryInfo(String userId);
	public List<SummaryInfo> getSummaryInfoToday(String userId);
	public List<SummaryInfo> getSummaryInfo(String user1Id,String user2Id);
	/**查询月度各类型下的消费额与消费次数*/
	public List<SummaryInfo> getSummaryInfoMonthPaid(String userId,Integer year,Integer month);
	/**查询月度(其他)类型下的消费额与消费次数*/
	public List<SummaryInfo> getSummaryInfoMonthPaidForOther(String userId,Integer year,Integer month);
	/**查询所有支付记录*/
	public List<AccountQueryRecord> getAllPaidRecords(String userId);
	/**查询所有收入记录*/
	public List<AccountQueryRecord> getAllReceiptRecords(String userId);
	
}
