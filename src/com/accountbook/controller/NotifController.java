package com.accountbook.controller;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.model.Form;
import com.accountbook.modle.result.Result;
import com.accountbook.service.INotifService;
import com.accountbook.service.IUserService;
import com.accountbook.utils.TextUtils;
import com.alibaba.fastjson.JSON;


@Controller
@RequestMapping("/notif")
	
public class NotifController {
	

	@Autowired
	IUserService userService;
	
	@Autowired
	INotifService notifService;
	
	
	
	@ResponseBody
    @RequestMapping("/open")
    public Object open(HttpServletRequest request,String userId){
		String findId=request.getAttribute("userid").toString();
		
		notifService.switcher(findId, true);
		return new Result(Result.RESULT_OK, "开启提醒功能!");
	}
	
	@ResponseBody
    @RequestMapping("/close")
    public Object close(HttpServletRequest request,String userId){
		String findId=request.getAttribute("userid").toString();
		
		notifService.switcher(findId, false);
		return new Result(Result.RESULT_OK, "关闭提醒功能!");
	}
	
	@ResponseBody
	@RequestMapping("/getValidCount")
	public Object getValidCount(HttpServletRequest request){
		String findId=request.getAttribute("userid").toString();
		System.out.println("isNotifOpen:"+notifService.isNotifOpen(findId));
		int validCount;
		if(notifService.isNotifOpen(findId))
			validCount = notifService.getValidCount(findId);
		else
			validCount=-1;
		return new Result(Result.RESULT_OK, "查询成功!").put("count", validCount);
	}
	
	
	@ResponseBody
	@RequestMapping(value="/addAll",method=RequestMethod.POST)
	public Object add(HttpServletRequest request,String formIds){
		String findId=request.getAttribute("userid").toString();
		if(TextUtils.isEmpty(formIds))
			return new Result(Result.RESULT_FAILD, "formIds不能为空!");
		
		List<String> formIdArr = JSON.parseArray(formIds, String.class);
		for(String formId:formIdArr){
			Form form=new Form();	
			form.userId=findId;
			form.formId=formId;
			form.time=new Timestamp(System.currentTimeMillis());
			notifService.add(form);
		}
		
		
		return new Result(Result.RESULT_OK, "批量添加成功!");
	}
	

}
