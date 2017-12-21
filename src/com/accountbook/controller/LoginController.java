package com.accountbook.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.globle.Constants;
import com.accountbook.model.UserInfo;
import com.accountbook.model.WxSession;
import com.accountbook.modle.result.Result;
import com.accountbook.service.ITokenService;
import com.accountbook.service.IUserService;
import com.accountbook.utils.HttpUtils;
import com.accountbook.utils.TextUtils;
import com.alibaba.fastjson.JSON;


@Controller	
@RequestMapping("/login")
public class LoginController {
	

	@Autowired
	IUserService userService;
	@Autowired
	ITokenService tokenService;
	
	
	@ResponseBody
    @RequestMapping("/checkLogin")
    public Result checkLogin(HttpServletRequest request){
		String findId = request.getAttribute("userid").toString();
		UserInfo findUser = userService.findUser(findId);
		if(findUser==null)
			return new Result(Result.RESULT_FAILD,"无此用户");
		if(TextUtils.isEmpty(findUser.nickname))
			return new Result(Result.RESULT_USERINFO_INVALID,"未完善用户信息");
		
		return new Result(Result.RESULT_OK,"已登录");
	}
	
	
    
	@ResponseBody
    @RequestMapping("/fromWX")
    public Result loginFromWx(HttpServletRequest request,HttpServletResponse response,@RequestParam(required=true) String code){
		Result result=new Result();
		System.out.println("loginFromWx");
    	
		//换取openid和session_key
    	Map<String,String> params=new HashMap<>();
    	params.put("appid", Constants.APP_ID);
    	params.put("secret", Constants.APP_SECRET);
    	params.put("js_code", code);
    	params.put("grant_type", "authorization_code");
    	String info=HttpUtils.sendPost("https://api.weixin.qq.com/sns/jscode2session",params);
    	
    	WxSession loginInfo=JSON.parseObject(info, WxSession.class);
    	
    	//查找数据库有没有此openid为username的用户
    	UserInfo databaseUser=userService.findUser(loginInfo.openid);
    	System.out.println(databaseUser);
    	if(databaseUser==null){
    		databaseUser=new UserInfo();
    		databaseUser.id=loginInfo.openid;
    		userService.newUSer(databaseUser);
    		
    		result.put(Result.RESULT_USERINFO_INVALID, "新用户,请完善用户信息");
        	
    		System.out.println("创建新用户一个,未完善个人信息!");
    	}else if(databaseUser.nickname==null || databaseUser.nickname.equals("")){
    		
    		
    		result.put(Result.RESULT_USERINFO_INVALID, "未完善用户信息");
    		
    	}else{
    		
    		result.put(Result.RESULT_OK, "登录成功");
        	System.out.println("登录旧用户!");
    	}
    	
    	
    	
    	String token=tokenService.generateToken();
    	System.out.println("派发token:"+token);
    	
    	tokenService.updateToken(databaseUser.id,token,false);
    	
    	result.put("token", token);
		return result;
    	
    }
	
	
	
	
	
	
    

}
