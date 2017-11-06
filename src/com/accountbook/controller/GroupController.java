package com.accountbook.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.model.Group;
import com.accountbook.model.Message;
import com.accountbook.model.UserInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.IGroupService;
import com.accountbook.service.IMessageService;
import com.accountbook.service.ITokenService;
import com.accountbook.service.IUserService;
import com.accountbook.utils.FileUtils;
import com.accountbook.utils.IDUtil;
import com.accountbook.utils.IconUtil;
import com.accountbook.utils.TextUtils;
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
		group.icon=IconUtil.createIcon(group, null);
		group.adminId=findId;
		group.time=System.currentTimeMillis();
		
		groupService.addGroup(group);
		
		result.put(Result.RESULT_OK, group.id);
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
			return result.put(Result.RESULT_COMMAND_INVALID, "没有权限!");
		}
		
		if(userCount==0 && !group.name.equals(name)){
			
			if(TextUtils.isNotEmpty(group.icon)){
				File oldFile=new File(FileUtils.getImageAbsolutePath(group.icon));
				if(oldFile.exists()){
					System.out.println("updateGroupIcon.删除旧图(无成员):"+oldFile.getAbsolutePath());
					oldFile.delete();
				}
			}
			
			
			group.icon=IconUtil.createIcon(group, null);
			System.out.println("更新头像(无成员):"+group);
		}
		
		group.name=name;
		group.category=category;
		
		groupService.updateGroupInfo(group);
		
		return result.put(Result.RESULT_OK, "更新成功!");
	}
	
	
	/**
	 * 查询分组简单信息
	 */
	@ResponseBody
	@RequestMapping("/getMembers")
    public Object getGroupMembers(ServletRequest req,String groupId){
		Result result=new Result();
		
		List<UserInfo> findUsersByGroupId = groupService.findUsersByGroupId(groupId);
		List<Result> listResult=new ArrayList<>();
		for(UserInfo info:findUsersByGroupId)
			listResult.add(new Result().put("memberId", info.id).put("memberName", info.nickname).put("memberIcon", info.avatarUrl));
		
		return result.put(Result.RESULT_OK, "查询分组成员成功!").put("members",listResult);
		
	}
	
	
	/**
	 * 查询用户创建和加入的分组
	 */
	@ResponseBody
	@RequestMapping("/getAll")
    public Object getAllJoinGroups(ServletRequest req){
		String findId=req.getAttribute("userid").toString();
		Result result=new Result();
		return result.put(Result.RESULT_OK, "查询我的分组成功!").put("groups",groupService.findJoinGroups(findId));
		
	}
	
	
	
	
	/**
	 * 查询分组简单信息
	 */
	@ResponseBody
	@RequestMapping("/getSimple")
    public Object getSimpleGroupInfo(ServletRequest req,String groupId){
//		String findId=req.getAttribute("userid").toString();
		
		
		Result result=new Result();
		
		Group groupInfo = groupService.queryGroupInfo(groupId);
		
		
		if(groupInfo!=null)
			return result.put(Result.RESULT_OK, "查询分组信息成功!").put(groupInfo);
		else
			return result.put(Result.RESULT_FAILD, "未查询到任何分组!");
		
	}
	
	/**
	 * 查询分组详细信息
	 */
	@ResponseBody
	@RequestMapping("/get")
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
	
	
	@ResponseBody
    @RequestMapping("/join")
    public Object joinGroup(ServletRequest req,String groupId){
		String findId=req.getAttribute("userid").toString();
		
		Result result=new Result();
		groupService.joinGroup(groupId, findId);
		
		//TODO 更新分组icon
		updateGroupIcon(groupId);
		
		
		return result.put(Result.RESULT_OK, "成功加入分组!");
	}
	
	@ResponseBody
    @RequestMapping("/quit")
    public Object quitGroup(ServletRequest req,String groupId){
		String findId=req.getAttribute("userid").toString();
		
		Result result=new Result();
		groupService.exitGroup(groupId,findId);
		
		
		//TODO 更新分组icon
		updateGroupIcon(groupId);
		
		return result.put(Result.RESULT_OK, "成功退出分组!");
	}
	
	/**
	 * 更新分组头像,当成员变动时,或者初次创建时
	 */
	private void updateGroupIcon(String groupId){
		
		Group group=groupService.queryGroupInfo(groupId);
		List<UserInfo> users = groupService.findUsersByGroupId(groupId);
		
		if(TextUtils.isNotEmpty(group.icon)){
			File oldFile=new File(FileUtils.getImageAbsolutePath(group.icon));
			if(oldFile.exists()){
				System.out.println("updateGroupIcon.删除旧图:"+oldFile.getAbsolutePath());
				oldFile.delete();
			}
		}
		
		List<String> icons=new ArrayList<>();
		for(UserInfo user:users){
			String iconPath = FileUtils.getImageAbsolutePath(user.icon);
			icons.add(iconPath);
		}
		
		group.icon=IconUtil.createIcon(group, icons);
		
		System.out.println("updateGroupIcon.更新头像:"+group);
		
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
			String filePath=FileUtils.saveFile(image, groupId);
			
			Group group=new Group();
			group.id=groupId;
			group.qr=filePath;
			groupService.updateGroupInfo(group);
			
			result.put(Result.RESULT_OK, filePath);
		} catch (Exception e) {
			result.put(Result.RESULT_FILE_SAVE_ERROR, "二维码文件存储失败!");
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
		msg.fromId=me.id;
		msg.toId=openid;
		msg.type=Message.MESSAGE_TYPE_INVITE_GROUP;
		msg.content="hi~~"+he.nickname+",我是"+me.nickname+",加入分组["+groupInfo.name+"]一起记账哦^~^";
		msg.timeMiles=System.currentTimeMillis();
		msg.state=Message.STATUS_UNREAD;
		msgService.newMessage(msg);
		
		
		
		String sendResult = WxUtil.sendTemplateInviteMessage(openid, formId, me.nickname, msg.content);
		
		return result.put(Result.RESULT_FILE_SAVE_ERROR, sendResult);
		
	}
	
	
	
	
	
	
}
