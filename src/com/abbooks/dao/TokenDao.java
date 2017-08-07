package com.abbooks.dao;


import java.util.List;

import com.abbooks.modle.TestModel;
import com.abbooks.modle.TokenInfo;

public interface TokenDao {
	public void insert(TokenInfo info);
	public void update(TokenInfo info);
	public TokenInfo queryIdByToken(String token);
	public TokenInfo queryTokenById(String id);
	
}
