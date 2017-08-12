package com.accountbook.service.impl;

import java.util.Base64;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.TokenDao;
import com.accountbook.modle.TokenInfo;
import com.accountbook.service.ITokenService;

@Service
public class TokenServiceImpl implements ITokenService {
	
	private static final int EXPIRE_TIME_INTERVAL=1000*60*60*24*7;//7天过期
	
	@Autowired
	public TokenDao dao;

	@Override
	public String getId(String token) {
		TokenInfo tokenInfo = dao.queryIdByToken(token);
		if(tokenInfo==null)
			return null;
		if(tokenInfo.expireTime!=-1)
			if(System.currentTimeMillis()>tokenInfo.expireTime)
				return null;
		return tokenInfo.id;
	}

	@Override
	public String updateToken(String id, String token,boolean isExpire) {
		TokenInfo queryTokenById = dao.queryTokenById(id);
		if(queryTokenById==null){
			queryTokenById=new TokenInfo();
			queryTokenById.id=id;
			queryTokenById.token=token;
			if(isExpire)
				queryTokenById.expireTime=System.currentTimeMillis()+EXPIRE_TIME_INTERVAL;
			else
				queryTokenById.expireTime=-1;
			dao.insert(queryTokenById);
		}else{
			queryTokenById.token=token;
			if(isExpire)
				queryTokenById.expireTime=System.currentTimeMillis()+EXPIRE_TIME_INTERVAL;
			else
				queryTokenById.expireTime=-1;
			dao.update(queryTokenById);
		}
		return null;
	}
	
	
	
	/**
	 * 生产一个新的token
	 * @return
	 */
	public String generateToken(){
		UUID uuid=UUID.randomUUID();
    	String token = Base64.getEncoder().encodeToString(uuid.toString().getBytes());
    	System.out.println(token);
    	return token;
	}

//	public static void main(String args[]){
//		for(int i=0;i<100;i++)
//			new TokenServiceImpl().generateToken();
//	}
}
