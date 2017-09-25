package com.accountbook.controller;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.modle.result.Result;
import com.accountbook.service.IAccountService;

/**
 * 账单
 * @author xinjun
 * create 2017.9.25
 */
@Controller
@RequestMapping("/account")
public class AccountController {
	


	@Autowired
	IAccountService accountService;
	

	/**
	 * 查询帐友和所有相关的分组
	 */
	@ResponseBody
	@RequestMapping("/getAllMembers")
    public Object findMembers(ServletRequest req){
		String findId=req.getAttribute("userid").toString();
		
		return new Result(Result.RESULT_OK, "查询分组信息成功!").put("members", accountService.findAllMembers(findId));
	}
	
	
	
	
}
