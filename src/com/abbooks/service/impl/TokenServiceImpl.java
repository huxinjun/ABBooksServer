package com.abbooks.service.impl;

import java.util.Base64;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abbooks.dao.TokenDao;
import com.abbooks.modle.TokenInfo;
import com.abbooks.service.ITokenService;

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
		if(System.currentTimeMillis()>tokenInfo.expireTime)
			return null;
		return tokenInfo.id;
	}

	@Override
	public String updateToken(String id, String token) {
		TokenInfo queryTokenById = dao.queryTokenById(id);
		if(queryTokenById==null){
			queryTokenById=new TokenInfo();
			queryTokenById.id=id;
			queryTokenById.token=token;
			queryTokenById.expireTime=System.currentTimeMillis()+EXPIRE_TIME_INTERVAL;
			dao.insert(queryTokenById);
		}else{
			queryTokenById.token=token;
			queryTokenById.expireTime=System.currentTimeMillis()+EXPIRE_TIME_INTERVAL;
			dao.update(queryTokenById);
		}
		return null;
	}
	
	
	
	/**
	 * 生产一个新的token
	 * @return
	 */
	public String generateToken(){
		Random rnd = new Random(System.currentTimeMillis());
        byte[] tokenData = new byte[16];
        rnd.nextBytes(tokenData);
        String token=Base64.getEncoder().encodeToString(tokenData);
    	System.out.println("token:"+token);
    	
    	return token;
	}

}
