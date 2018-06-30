package com.accountbook.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.AccountDao;
import com.accountbook.model.Account;
import com.accountbook.model.Member;
import com.accountbook.model.Offset;
import com.accountbook.model.AccountQueryRecord;
import com.accountbook.model.PayOffset;
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
		if(account==null)
			return null;
		wrapAccount(account);
		return account;
	}
	
	public List<Account> findAccounts(String userId,Integer pageIndex,Integer pageSize) {
		return findAccounts(userId, null,pageIndex,pageSize);
	}
	public List<Account> findAccounts(String userId,String bookId,Integer pageIndex,Integer pageSize) {
		List<Account> accounts;
		Limit limit = CommonUtils.getLimit(pageIndex, pageSize);
		if(bookId==null)
			accounts=dao.queryMyAccounts(new HashMap<String,Object>(){
				private static final long serialVersionUID = 1L;
				{
					put("userId", userId);
					put("ls", limit.start);
					put("lc", limit.count);
				}
			});
		else
			accounts=dao.queryMyAccountsByBookId(new HashMap<String,Object>(){
				private static final long serialVersionUID = 1L;
				{
					put("userId", userId);
					put("bookId", bookId);
					put("ls", limit.start);
					put("lc", limit.count);
				}
			});
		wrapAccount(accounts);
		return accounts;
	}
	public List<Account> findAccounts(String userId,String year,String month,String type,String name,Integer pageIndex,Integer pageSize) {
		List<Account> accounts;
		Limit limit = CommonUtils.getLimit(pageIndex, pageSize);
		accounts=dao.queryMyAccountsByMonthType(new HashMap<String,Object>(){
			private static final long serialVersionUID = 1L;
			{
				put("userId", userId);
				put("date", year+"-"+(Integer.parseInt(month)<10?"0"+month:month));
				put("type", type);
				put("name", name);
				put("ls", limit.start);
				put("lc", limit.count);
			}
		});
		wrapAccount(accounts);
		return accounts;
	}
	@Override
	public List<Account> findAccounts2P(String user1Id, String user2Id,Integer pageIndex, Integer pageSize) {
		
		List<Account> accounts;
		Limit limit = CommonUtils.getLimit(pageIndex, pageSize);
		accounts=dao.queryTwoPersonAccounts(new HashMap<String,Object>(){
			private static final long serialVersionUID = 1L;
			{
				put("user1Id", user1Id);
				put("user2Id", user2Id);
				put("ls", limit.start);
				put("lc", limit.count);
			}
		});
		wrapAccount(accounts);
			
		return accounts;
		
	}
	
	
	
	private void wrapAccount(Account account){
		wrapAccount(new ArrayList<Account>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				add(account);
			}
		});
	}
	/**
	 * 为查询出来的account添加成员和抵消记录
	 */
	private void wrapAccount(List<Account> accounts){
		for(Account account:accounts){
			account.setMembers((ArrayList<Member>) dao.queryMembersByAccountId(account.getId()));
			
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			List<PayTarget> payTargets = dao.queryPayTargetByAccountId(account.getId());
			if(payTargets!=null && payTargets.size()>0){
				account.setPayResult(new ArrayList<PayResult>());
				account.getPayResult().add(new PayResult());
				account.getPayResult().get(0).setPayTarget((ArrayList<PayTarget>) payTargets);
				
				for(PayTarget target:payTargets)
					if(target.getOffsetCount()>0)
						target.setOffsetMoney(Float.parseFloat(decimalFormat.format(dao.queryOffsetMoney(target.getId()))));
			}
		}
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
	public void addPayOffset(PayOffset offset) {
		dao.insertPayOffset(offset);
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
	public List<SummaryInfo> getSummaryInfo(String userId) {
		return dao.queryAccountSummary(userId);
	}
	
	@Override
	public List<SummaryInfo> getSummaryInfoToday(String userId) {
		return dao.queryAccountSummaryToday(userId);
	}

	@Override
	public List<SummaryInfo> getSummaryInfo(String user1Id, String user2Id) {
		return dao.queryAccountSummary2P(new HashMap<String,Object>(){
			private static final long serialVersionUID = 1L;
			{
				put("user1Id", user1Id);
				put("user2Id", user2Id);
			}
		});
	}
	@Override
	public PayTarget findPayTarget(String targetId) {
		return dao.queryPayTarget(targetId);
	}

	@Override
	public double getWaitPaidMoney(String user1Id, String user2Id,String targetId) {
		return dao.queryWaitPaidMoney(new HashMap<String,Object>(){
			private static final long serialVersionUID = 1L;
			{
				put("user1Id", user1Id);
				put("user2Id", user2Id);
				put("targetId", targetId);
			}
		});
	}

	@Override
	public PayTarget findEarliestNotSettledTarget(String user1Id, String user2Id,String targetId) {
		return dao.queryEarliestNotSettledTarget(new HashMap<String,Object>(){
			private static final long serialVersionUID = 1L;
			{
				put("user1Id", user1Id);
				put("user2Id", user2Id);
				put("targetId", targetId);
			}
		});
	}
	
	/**查询抵消记录*/
	public List<PayOffset> queryOriginOffsets(String payId){
		return dao.queryOriginOffsets(payId);
	}
	/**查询抵消记录,包装好了头像等,适用于列表显示*/
	@Override
	public List<Offset> findOffsets(String payId){
		return dao.queryOffsets(payId);
	}

	@Override
	public void deleteOffset(String offsetId){
		dao.deleteOffset(offsetId);
	}

	@Override
	public void deleteAccount(String accountId) {
		dao.deleteAccount(accountId);
	}

	@Override
	public void updateMemberIcon(String userId, String memberIcon) {
		dao.updateMemberIcon(userId, memberIcon);
	}

	@Override
	public List<SummaryInfo> getSummaryInfoMonthPaid(String userId,Integer year,Integer month) {
		String date;
		if(year==null || month==null)
			date=new SimpleDateFormat("yyyy-MM").format(new Date());
		else
			date=year+"-"+(month<10?"0"+month:String.valueOf(month));
			
		return dao.queryAccountSummaryMonthPaid(userId,date);
	}

	@Override
	public List<SummaryInfo> getSummaryInfoMonthPaidForOther(String userId,Integer year,Integer month) {
		String date;
		if(year==null || month==null)
			date=new SimpleDateFormat("yyyy-MM").format(new Date());
		else
			date=year+"-"+(month<10?"0"+month:String.valueOf(month));
		return dao.queryAccountSummaryMonthPaidForOther(userId,date);
	}

	@Override
	public List<AccountQueryRecord> getAllPaidRecords(String userId) {
		return dao.queryAllPaidRecords(userId);
	}

	@Override
	public List<AccountQueryRecord> getAllReceiptRecords(String userId) {
		return dao.queryAllReceiptRecords(userId);
	}

	

	


	

}
