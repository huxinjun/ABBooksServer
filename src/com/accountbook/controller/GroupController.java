package com.accountbook.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.globle.Constants;
import com.accountbook.modle.Group;
import com.accountbook.modle.Message;
import com.accountbook.modle.UserInfo;
import com.accountbook.modle.WxAccessToken;
import com.accountbook.modle.WxTemplateInvite;
import com.accountbook.modle.WxTemplateInvite.KeyWord;
import com.accountbook.modle.result.GroupResult;
import com.accountbook.modle.result.Result;
import com.accountbook.service.IGroupService;
import com.accountbook.service.IMessageService;
import com.accountbook.service.ITokenService;
import com.accountbook.service.IUserService;
import com.accountbook.utils.HttpUtils;
import com.accountbook.utils.IDUtil;
import com.accountbook.utils.IconUtil;
import com.alibaba.fastjson.JSON;


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
	public Object changeGroupInfo(ServletRequest req,String groupId,String name,String category){
		String findId=req.getAttribute("userid").toString();
		System.out.println("group name:"+name);
		
		GroupResult result=new GroupResult();
		
		result.group=groupService.queryGroupInfo(groupId);
		result.users=groupService.findUsersByGroupId(groupId);
		
		if(!result.group.adminId.equals(findId)){
			result.status=Result.RESULT_COMMAND_INVALID;
			result.msg="没有权限!";
			return result;
		}
		
		
		List<String> icons=new ArrayList<>();
		for(UserInfo user:result.users){
			String iconPath=Constants.EXTERN_FILE_DIR+Constants.PATH_IMAGE_UPLOAD+user.icon;
			
			icons.add(iconPath);
		}
		
		
		result.group.name=name;
		result.group.category=category;
		result.group.icon=IconUtil.createIcon(name, icons);
		
		System.out.println(result.group);
		
		groupService.updateGroupInfo(result.group);
		
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
		
		
		result.status=0;
		result.msg="成功加入分组!";
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
		
		
		//发送模板消息start-------------------------------------------------------------------------------------------------------
		//获取accesstoken
    	Map<String,String> params=new HashMap<>();
    	params.put("appid", Constants.APP_ID);
    	params.put("secret", Constants.APP_SECRET);
    	params.put("grant_type", "client_credential");
    	String accessUrl="https://api.weixin.qq.com/cgi-bin/token";
    	String info=HttpUtils.sendGet(accessUrl,params);
		
    	
		WxAccessToken accessToken=JSON.parseObject(info, WxAccessToken.class);
		System.out.println(accessToken);
		
		String url="https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+accessToken.access_token;
		
		WxTemplateInvite invite=new WxTemplateInvite();
		invite.touser=openid;
		invite.template_id="r63a82Qy_kap-4SxZUT9SfwS5004qb-l_i17zzUOqY4";
		invite.page="pages/msg_invite/group_invite";
		invite.form_id=formId;
		List<KeyWord> data=new ArrayList<>();
		data.add(new KeyWord(me.nickname,"#173177"));
		data.add(new KeyWord("赶紧加入小账本本!!!呲呲的^_^","#173177"));
		invite.data=data;
		
		System.out.println(invite.toString());
		
		String str=HttpUtils.sendPost(url,invite.toString());
		//发送模板消息end-------------------------------------------------------------------------------------------------------
		
		result.status=Result.RESULT_OK;
		result.msg=str;
		return result;
		
	}
	
	
	
	
	
	
}
