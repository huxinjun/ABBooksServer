package com.accountbook.controller;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.globle.Constants;
import com.accountbook.modle.Group;
import com.accountbook.modle.Message;
import com.accountbook.modle.UserInfo;
import com.accountbook.modle.result.GroupResult;
import com.accountbook.modle.result.Result;
import com.accountbook.service.IGroupService;
import com.accountbook.service.IMessageService;
import com.accountbook.service.ITokenService;
import com.accountbook.service.IUserService;
import com.accountbook.utils.IDUtil;
import com.accountbook.utils.IconUtil;
import com.accountbook.utils.ImageUtils;
import com.accountbook.utils.WxUtil;


@Controller
@RequestMapping("/group")
	
public class GroupController {
	

	@Autowired
	IUserService userService;
	
	@Autowired
	ITokenService tokenService;
	
	@Autowired
	IMessageService msgService;
	
	@Autowired
	IGroupService groupService;
	
	
	
	
	@ResponseBody
    @RequestMapping("/add")
    public Object createNewGroup(ServletRequest req,String name,String category){
		String findId=req.getAttribute("userid").toString();
		System.out.println("group name:"+name);
		
		Result result=new Result();
		
		Group group=new Group();
		group.id=IDUtil.generateNewId();
		group.name=name;
		group.category=category;
		group.icon=IconUtil.createIcon(name, null);
		group.adminId=findId;
		group.time=System.currentTimeMillis();
		
		groupService.addGroup(group);
		
		
		result.status=0;
		result.msg=group.id;
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/update")
	public Object updateGroupInfo(ServletRequest req,String groupId,String name,String category){
		String findId=req.getAttribute("userid").toString();
		System.out.println("group name:"+name);
		
		Result result=new Result();
		
		Group group=groupService.queryGroupInfo(groupId);
		int userCount=groupService.findUsersCountByGroupId(groupId);
		System.out.println("查询的组:"+group);
		System.out.println("查询的组用户数:"+userCount);
		
		if(!group.adminId.equals(findId)){
			result.status=Result.RESULT_COMMAND_INVALID; 
			result.msg="没有权限!";
			return result;
		}
		
		if(userCount==0 && !group.name.equals(name)){
			group.icon=IconUtil.createIcon(name, null);
			System.out.println("更新头像(无成员):"+group);
		}
		
		group.name=name;
		group.category=category;
		
		groupService.updateGroupInfo(group);
		
		result.status=0;
		result.msg="更新成功!";
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/get")
    public Object getGroupInfo(ServletRequest req,String groupId){
		String findId=req.getAttribute("userid").toString();
		
		
		GroupResult result=new GroupResult();
		
		result.group=groupService.queryGroupInfo(groupId);
		
		if(result.group!=null && findId.equals(result.group.adminId))
			result.isAdmin=true;
			
		result.users=groupService.findUsersByGroupId(groupId);
		if(result.users!=null)
			for(UserInfo info:result.users)
				if(findId.equals(info.id)){
					result.isMember=true;
					break;
				}
		
		
		result.status=0;
		result.msg="查询分组信息成功!";
		return result;
	}
	
	
	@ResponseBody
    @RequestMapping("/join")
    public Object joinGroup(ServletRequest req,String groupId){
		String findId=req.getAttribute("userid").toString();
		
		Result result=new Result();
		groupService.joinGroup(groupId, findId);
		
		//TODO 更新分组icon
		updateGroupIcon(groupId);
		
		
		result.status=0;
		result.msg="成功加入分组!";
		return result;
	}
	
	@ResponseBody
    @RequestMapping("/quit")
    public Object quitGroup(ServletRequest req,String groupId){
		String findId=req.getAttribute("userid").toString();
		
		Result result=new Result();
		groupService.exitGroup(groupId,findId);
		
		
		//TODO 更新分组icon
		updateGroupIcon(groupId);
		
		
		result.status=0;
		result.msg="成功加入分组!";
		return result;
	}
	
	/**
	 * 更新分组头像,当成员变动时,或者初次创建时
	 */
	private void updateGroupIcon(String groupId){
		
		Group group=groupService.queryGroupInfo(groupId);
		List<UserInfo> users = groupService.findUsersByGroupId(groupId);
		
		
		List<String> icons=new ArrayList<>();
		for(UserInfo user:users){
			String iconPath=Constants.EXTERN_FILE_DIR+Constants.PATH_IMAGE_UPLOAD+user.icon;
			icons.add(iconPath);
		}
		
		group.icon=IconUtil.createIcon(group.name, icons);
		
		System.out.println("更新头像:"+group);
		
		groupService.updateGroupInfo(group);
	}
	
	
	
	/**
	 * 此接口获取分组的二维码
	 */
	@ResponseBody
	@RequestMapping("/qr")
	public Object qr(HttpServletRequest request,HttpServletResponse response,String groupId){
		
		//已经生成过的直接取
		Group findGroup = groupService.queryGroupInfo(groupId);
		System.out.println("qr.findGroup:"+findGroup);
		
		if(findGroup==null)
			return new Result(Result.RESULT_FAILD,"未查询到分组");
		
		if(findGroup.qr!=null && !"".equals(findGroup.qr))
			return new Result(Result.RESULT_OK,findGroup.qr);
		
		
		//获取二维码start-------------------------------------------------------------------------------------------------------
		
		Result result=new Result();
		
		
		@SuppressWarnings("serial")
		byte[] image=WxUtil.getQrImage("pages/index/index",new HashMap<String,String>(){
			{
				put("groupId",groupId);
			}
		});
		
		try {
			//存储到服务器
			String filename=UUID.randomUUID().toString();
			String filePath=Constants.EXTERN_FILE_DIR+Constants.PATH_IMAGE_UPLOAD+filename;
			
			ImageUtils.send(image, new FileOutputStream(filePath));
			
			Group group=new Group();
			group.id=groupId;
			group.qr=filename;
			groupService.updateGroupInfo(group);
			
			result.status=Result.RESULT_OK;
			result.msg=filename;
		} catch (Exception e) {
			
			result.status=Result.RESULT_FILE_SAVE_ERROR;
			result.msg="二维码文件存储失败!";
			e.printStackTrace();
		}
		
		return result;
		  
	}
	
	
	
	
	
	
	@ResponseBody
	@RequestMapping("/invite")
	public Object inviteUser(ServletRequest req,String code,String openid,String groupId,String formId){
		String findId=req.getAttribute("userid").toString();
		
		
		Result result=new Result();
		
		
		UserInfo me = userService.findUser(findId);
		UserInfo he = userService.findUser(openid);
		
		Group groupInfo = groupService.queryGroupInfo(groupId);
		
		//数据库中加入邀请信息
		Message msg=new Message();
		msg.inviteId=me.id;
		msg.acceptId=openid;
		msg.type=Message.MESSAGE_TYPE_INVITE_GROUP;
		msg.content="hi~~"+he.nickname+",我是"+me.nickname+",加入分组["+groupInfo.name+"]一起记账哦^~^";
		msg.timeMiles=System.currentTimeMillis();
		msg.status=Message.STATUS_UNREAD;
		msgService.newMessage(msg);
		
		
		
		String sendResult = WxUtil.sendTemplateInviteMessage(openid, formId, me.nickname, msg.content);
		
		result.status=Result.RESULT_OK;
		result.msg=sendResult;
		return result;
		
	}
	
	
	
	
	
	
}
