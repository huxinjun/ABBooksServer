package com.abbooks.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abbooks.globle.Constants;
import com.abbooks.modle.UserInfo;
import com.abbooks.modle.WxLoginInfo;
import com.abbooks.modle.result.LoginResult;
import com.abbooks.modle.result.Result;
import com.abbooks.service.ITokenService;
import com.abbooks.service.IUserService;
import com.abbooks.utils.HttpUtils;
import com.alibaba.fastjson.JSON;


@Controller	
@RequestMapping("/login")
public class LoginController {
	

	@Autowired
	IUserService userService;
	@Autowired
	ITokenService tokenService;
	
    
	@ResponseBody
    @RequestMapping("/fromWX")
    public Result loginFromWx(HttpServletRequest request,HttpServletResponse response,@RequestParam(required=true) String code){
		LoginResult result=new LoginResult();
    	
		//换取openid和session_key
    	Map<String,String> params=new HashMap<>();
    	params.put("appid", Constants.APP_ID);
    	params.put("secret", Constants.APP_SECRET);
    	params.put("js_code", code);
    	params.put("grant_type", "authorization_code");
    	String info=HttpUtils.send("https://api.weixin.qq.com/sns/jscode2session",params);
    	
    	WxLoginInfo loginInfo=JSON.parseObject(info, WxLoginInfo.class);
    	System.out.println(loginInfo);
    	
    	//查找数据库有没有此openid为username的用户
    	UserInfo databaseUser=userService.findUser(loginInfo.openid);
    	System.out.println(databaseUser);
    	if(databaseUser==null){
    		databaseUser=new UserInfo();
    		databaseUser.id=loginInfo.openid;
    		userService.newUSer(databaseUser);
    		
    		result.status=1;
        	result.msg="新用户";
        	
    		System.out.println("创建新用户一个,未完善个人信息!");
    	}else if(databaseUser.nickname==null || databaseUser.nickname.equals("")){
    		
    		result.status=1;
        	result.msg="未完善用户信息";
    		
    	}else{
    		
    		result.status=0;
        	result.msg="登录成功";
        	System.out.println("登录旧用户!");
    	}
    	
    	
    	
    	String token=tokenService.generateToken();
    	
    	tokenService.updateToken(databaseUser.id,token,false);
    	
    	result.token=token;
		return result;
    	
    }
	
	
	
	
	
	
    

}
