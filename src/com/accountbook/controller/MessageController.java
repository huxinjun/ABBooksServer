package com.accountbook.controller;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.modle.Friend;
import com.accountbook.modle.Message;
import com.accountbook.modle.UserInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.IFriendService;
import com.accountbook.service.IMessageService;
import com.accountbook.service.ITokenService;
import com.accountbook.service.IUserService;


@Controller
@RequestMapping("/msg")
public class MessageController {
	

	@Autowired
	ITokenService tokenService;
	
	@Autowired
	IMessageService mMsgService;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	IFriendService friendService;
	
	
    
    @ResponseBody
    @RequestMapping("/invite")
    
    public Object getInviteMessage(HttpServletRequest request,HttpServletResponse response,String type/**type必须:1帐友邀请,2加入组邀请*/){
    	String findId=request.getAttribute("userid").toString();
    	
    	
    	Result result=new Result();
    	
    	
    	List<Message> msgs = mMsgService.findInviteMessage(findId,type);
    	
    	List<Result> resultMsgs=new ArrayList<>();
    	for(Message msg:msgs){
    		Date date=new Date(msg.timeMiles);
    		DateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
    		String dateStr=format.format(date);
    		UserInfo findUser = userService.findUser(msg.inviteId);
    		
    		Result msgResult=new Result();
    		
    		msgResult.put("id",msg.id);
    		msgResult.put("icon",findUser.avatarUrl);
    		msgResult.put("nickname",findUser.nickname);
    		msgResult.put("msg",msg.content);
    		msgResult.put("time",dateStr);
    		msgResult.put("state",msg.state);
    		
    		resultMsgs.add(msgResult);
    	}
    	
    	
    	return result.put(Result.RESULT_OK, "查询成功").put("datas", resultMsgs);
    	
    }
    
    @ResponseBody
    @RequestMapping("/invite/accept")
    public Object accept(HttpServletRequest request,HttpServletResponse response,int msgId){

		
		Result result=new Result();
		mMsgService.makeAccepted(msgId);
		
		Message message = mMsgService.findMessage(msgId);
		
		boolean isFriend = friendService.isFriend(message.acceptId, message.inviteId);
		if(isFriend){
			return result.put(Result.RESULT_COMMAND_INVALID, "已经是好友啦!");
		}
		
		Friend friend=new Friend();
		friend.inviteId=message.inviteId;
		friend.acceptId=message.acceptId;
		friend.time=System.currentTimeMillis();
		
		friendService.newFriend(friend);
		
		return result.put(Result.RESULT_OK, "添加好友成功!");
		
    }
    
    
    @ResponseBody
    @RequestMapping("/invite/refuse")
    public Object refuse(HttpServletRequest request,HttpServletResponse response,int msgId){

		Result result=new Result();
		Message message = mMsgService.findMessage(msgId);
		if(message.state==Message.STATUS_INVITE_ACCEPT ||message.state==Message.STATUS_INVITE_REFUSE){
			return result.put(Result.RESULT_COMMAND_INVALID, "重复操作!");
		}
		
		
		mMsgService.makeRefused(msgId);
		
		return result.put(Result.RESULT_OK, "操作成功!");
		
    }
    
    @ResponseBody
    @RequestMapping("/invite/delete")
    public Object delete(HttpServletRequest request,HttpServletResponse response,int msgId){

		Result result=new Result();
		Message message = mMsgService.findMessage(msgId);
		if(message.state==Message.STATUS_DELETE){
			return result.put(Result.RESULT_COMMAND_INVALID, "重复操作!");
		}
		
		
		mMsgService.makeDeleted(msgId);
		
		return result.put(Result.RESULT_OK, "操作成功!");
		
    }
}
