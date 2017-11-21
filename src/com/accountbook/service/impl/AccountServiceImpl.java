package com.accountbook.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.AccountDao;
import com.accountbook.model.Account;
import com.accountbook.model.Member;
import com.accountbook.model.Offset;
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
		account.setMembers((ArrayList<Member>) dao.queryMembersByAccountId(account.getId()));
		
		List<PayTarget> payTargets = dao.queryPayTargetByAccountId(account.getId());
		if(payTargets!=null && payTargets.size()>0){
			account.setPayResult(new ArrayList<PayResult>());
			account.getPayResult().add(new PayResult());
			account.getPayResult().get(0).setPayTarget((ArrayList<PayTarget>) payTargets);
		}
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
		for(Account account:accounts){
			account.setMembers((ArrayList<Member>) dao.queryMembersByAccountId(account.getId()));
			
			
			List<PayTarget> payTargets = dao.queryPayTargetByAccountId(account.getId());
			if(payTargets!=null && payTargets.size()>0){
				account.setPayResult(new ArrayList<PayResult>());
				account.getPayResult().add(new PayResult());
				account.getPayResult().get(0).setPayTarget((ArrayList<PayTarget>) payTargets);
				
				for(PayTarget target:payTargets)
					if(target.getOffsetCount()>0)
						target.setOffsetMoney((float) dao.queryOffsetMoney(target.getId()));
			}
		}
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
	@Override
	public List<Offset> findOffsets(String targetId){
		return dao.queryOffsets(targetId);
	}

	

	


	

}
