package com.abbooks.service;

import java.util.List;

import com.abbooks.modle.TestModel;

public interface ITokenService {
	public String getId(String token);
	public String updateToken(String id,String token);
	
	public String generateToken();
}
