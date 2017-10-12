package com.accountbook.dao;


import java.util.List;

import com.accountbook.model.Account;
import com.accountbook.model.Member;
import com.accountbook.model.PayTarget;

public interface AccountDao {
	
	public void insert(Account account);
	public void insertMember(Member member);
	public void insertPayTarget(PayTarget target);
	
	/**查询和userId相关的所有成员:分组和帐友*/
	public List<Member> queryMembers(String userId);

}
