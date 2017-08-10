package com.accountbook.dao;


import java.util.List;

import com.accountbook.modle.TestModel;
import com.accountbook.modle.TokenInfo;

public interface TokenDao {
	public void insert(TokenInfo info);
	public void update(TokenInfo info);
	public TokenInfo queryIdByToken(String token);
	public TokenInfo queryTokenById(String id);
	
}
