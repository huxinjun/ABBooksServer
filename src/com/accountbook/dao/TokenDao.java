package com.accountbook.dao;


import com.accountbook.modle.TokenInfo;

public interface TokenDao {
	public void insert(TokenInfo info);
	public void update(TokenInfo info);
	public TokenInfo queryTokenById(String id);
	
}
