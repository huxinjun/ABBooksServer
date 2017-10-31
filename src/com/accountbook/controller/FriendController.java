package com.accountbook.controller;

import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.model.UserInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.IFriendService;
import com.accountbook.service.IGroupService;


@Controller
@RequestMapping("/friend")
	
public class FriendController {
	
	@Autowired
	IGroupService groupService;
	@Autowired
	IFriendService friendService;
	

	
	/**
	 * 查询帐友
	 */
	@ResponseBody
	@RequestMapping("/getAll")
    public Object getMyFriends(ServletRequest req,String groupId){
		String findId=req.getAttribute("userid").toString();
		Result result=new Result();
		List<UserInfo> findAll = friendService.findAll(findId);
		List<Result> convert = Result.convert(findAll);
		return result.put(Result.RESULT_OK, "查询好友列表成功!").put("friends",convert);
	}
	
}
