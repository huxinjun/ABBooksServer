package com.accountbook.controller;

import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.model.Group;
import com.accountbook.model.UserInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.IGroupService;


@Controller
@RequestMapping("/friend")
	
public class FriendController {
	
	@Autowired
	IGroupService groupService;
	

	/**
	 * 查询帐友和所有相关的分组
	 */
	@ResponseBody
	@RequestMapping("/getAll")
    public Object getGroupInfo(ServletRequest req,String groupId){
		String findId=req.getAttribute("userid").toString();
		
		
		Result result=new Result();
		
		Group groupInfo = groupService.queryGroupInfo(groupId);
		result.put("group", groupService.queryGroupInfo(groupId));
		
		if(groupInfo!=null && findId.equals(groupInfo.adminId))
			result.put("isAdmin",true);
			
		List<UserInfo> findUsersByGroupId = groupService.findUsersByGroupId(groupId);
		if(findUsersByGroupId!=null)
			for(UserInfo info:findUsersByGroupId)
				if(findId.equals(info.id)){
					result.put("isMember",true);
					break;
				}
		
		
		
		return result.put(Result.RESULT_OK, "查询分组信息成功!").put("users", findUsersByGroupId);
	}
	
	
}
