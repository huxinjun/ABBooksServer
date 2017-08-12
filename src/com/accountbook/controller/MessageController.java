package com.accountbook.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.modle.Message;
import com.accountbook.modle.result.ListResult;
import com.accountbook.service.IMessageService;


@Controller
@RequestMapping("/msg")
public class MessageController {
	

	
	
	@Autowired
	public  IMessageService mMsgService;
	
	
    
    @ResponseBody
    @RequestMapping("/invite")
    public ListResult getInviteMessage(HttpServletRequest request,HttpServletResponse response,String token,String id){
    	
    	ListResult result=new ListResult();
    	result.status=0;
    	
    	List<Message> msgs = mMsgService.findInviteMessage(id);
    	result.datas=msgs;
    	
		return result;
    	
    }
}
