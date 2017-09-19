package com.accountbook.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.TokenDao;
import com.accountbook.modle.TokenInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.ITokenService;
import com.accountbook.utils.IDUtil;

@Service
public class TokenServiceImpl implements ITokenService {
	
	private static final int EXPIRE_TIME_INTERVAL=1000*60*60*24*7;//7天过期
	
	@Autowired
	public TokenDao dao;
	
	
	@Override
	public Result validate(String token) {
		Result result = new Result();
		String id = getId(token);
		System.out.println("UserController(根据token["+token+"]查找的openid)："+id);
		if(id==null){
			result.status=1;
			result.msg="token无效";
			return result;
		}
		result.status=0;
		result.msg=id;
		return result;
	}
	
	

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
			queryTokenById.id=id;
			queryTokenById.token=token;
			if(isExpire)
				queryTokenById.expireTime=System.currentTimeMillis()+EXPIRE_TIME_INTERVAL;
			else
				queryTokenById.expireTime=-1;
			System.out.println("queryTokenById:"+queryTokenById);
			dao.update(queryTokenById);
		}
		return null;
	}



	@Override
	public String generateToken() {
		return IDUtil.generateNewId();
	}
}
