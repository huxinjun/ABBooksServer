package com.abbooks.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abbooks.modle.TestModel;
import com.abbooks.modle.UserInfo;
import com.abbooks.modle.result.Result;
import com.abbooks.modle.result.TestAllResult;
import com.abbooks.service.ITestService;
import com.abbooks.service.ITokenService;
import com.abbooks.service.IUserService;
import com.alibaba.fastjson.JSON;


@Controller
@RequestMapping("/user")
	
public class UserController {
	

	@Autowired
	IUserService userService;
	
	@Autowired
	ITokenService tokenService;
	
	
    
	@ResponseBody
    @RequestMapping("/updateInfo")
    public Result updateUserInfo(HttpServletRequest request,String token,String info){
		Result result=new Result();
		
		UserInfo userinfo=JSON.parseObject(info, UserInfo.class);
		System.out.println(info);
		System.out.println(userinfo);
		
		String id = tokenService.getId(token);
		if(id==null){
			result.status=1;
			result.msg="token无效";
			return result;
		}
			
		userinfo.id=id;
		
		userService.updateUser(userinfo);
		
		
		result.status=0;
		result.msg="更新用户信息成功!";
		return result;
		
	}
}
