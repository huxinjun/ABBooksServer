package com.accountbook.service;

import com.accountbook.modle.result.Result;

/**
 * token
 * @author XINJUN
 *
 */
public interface ITokenService {
	
	/**
	 * 检查token有效性
	 * @param token
	 * @return
	 */
	public Result validate(String token);
	
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
