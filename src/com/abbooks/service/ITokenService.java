package com.abbooks.service;

import java.util.List;

import com.abbooks.modle.TestModel;

public interface ITokenService {
	public String getId(String token);
	/**
	 * 更新token
	 * @param id
	 * @param token
	 * @param isExpire 是否会过期,如果微信登录的话不会过期
	 * @return
	 */
	public String updateToken(String id,String token,boolean isExpire);
	
	public String generateToken();
}
