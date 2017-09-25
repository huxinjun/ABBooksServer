package com.accountbook.service;

import java.util.List;

import com.accountbook.modle.Member;

public interface IAccountService {
	
	public List<Member> findAllMembers(String userId);
}
