package com.accountbook.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.modle.result.Result;
import com.accountbook.service.IMessageService;


@Controller
@RequestMapping("/msg")
public class MessageController {
	

	
	
	@Autowired
	public  IMessageService mMsgService;
	
	
    
    @ResponseBody
    @RequestMapping("/")
    public Result getSimpleMapNeedInfo(HttpServletRequest request,HttpServletResponse response){
    	
    	Result result=new Result();
    	result.status=0;
    	
		return result;
    	
    }
}
