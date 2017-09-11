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
import com.accountbook.modle.result.ListResult;
import com.accountbook.modle.result.MessageResult;
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
    
    public Object getInviteMessage(HttpServletRequest request,HttpServletResponse response,String token,int type/**type必须:1帐友邀请,2加入组邀请*/){
    	//token检查-----------------------------------------------
    	Result tokenValidResult=tokenService.validate(token);
		if(tokenValidResult.status==Result.RESULT_TOKEN_INVALID)
			return tokenValidResult;
		String findId=tokenValidResult.msg;
		//--------------------------------------------------------
    	
    	
    	ListResult result=new ListResult();
    	
    	
    	List<Message> msgs = mMsgService.findInviteMessage(findId,type+"");
    	
    	List<MessageResult> msgResults=new ArrayList<>();
    	for(Message msg:msgs){
    		Date date=new Date(msg.timeMiles);
    		DateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
    		String dateStr=format.format(date);
    		UserInfo findUser = userService.findUser(msg.inviteId);
    		msgResults.add(new MessageResult(msg.id,findUser.avatarUrl, findUser.nickname, msg.content, dateStr, msg.status));
    	}
    	
    	
    	
    	result.status=Result.RESULT_OK;
    	result.msg="查询成功";
    	result.datas=msgResults;
    	
    	
		return result;
    	
    }
    
    @ResponseBody
    @RequestMapping("/invite/accept")
    public Object accept(HttpServletRequest request,HttpServletResponse response,String token,int msgId){
    	//token检查-----------------------------------------------
		Result tokenValidResult=tokenService.validate(token);
		if(tokenValidResult.status==Result.RESULT_TOKEN_INVALID)
			return tokenValidResult;
		//--------------------------------------------------------
		
		Result result=new Result();
		mMsgService.makeAccepted(msgId);
		
		Message message = mMsgService.findMessage(msgId);
		
		boolean isFriend = friendService.isFriend(message.acceptId, message.inviteId);
		if(isFriend){
			result.status=Result.RESULT_COMMAND_INVALID;
			result.msg="已经是好友啦!";
			return result;
		}
		
		Friend friend=new Friend();
		friend.inviteId=message.inviteId;
		friend.acceptId=message.acceptId;
		friend.time=System.currentTimeMillis();
		
		friendService.newFriend(friend);
		
		
		result.status=0;
		result.msg="添加好友成功!";
		return result;
		
    }
    
    
    @ResponseBody
    @RequestMapping("/invite/refuse")
    public Object refuse(HttpServletRequest request,HttpServletResponse response,String token,int msgId){
    	//token检查-----------------------------------------------
		Result tokenValidResult=tokenService.validate(token);
		if(tokenValidResult.status==Result.RESULT_TOKEN_INVALID)
			return tokenValidResult;
		//--------------------------------------------------------
		Result result=new Result();
		Message message = mMsgService.findMessage(msgId);
		if(message.status==Message.STATUS_INVITE_ACCEPT ||message.status==Message.STATUS_INVITE_REFUSE){
			result.status=Result.RESULT_COMMAND_INVALID;
			result.msg="重复操作";
			return result;
		}
		
		
		mMsgService.makeRefused(msgId);
		
		result.status=0;
		result.msg="操作成功!";
		return result;
		
    }
    
    @ResponseBody
    @RequestMapping("/invite/delete")
    public Object delete(HttpServletRequest request,HttpServletResponse response,String token,int msgId){
    	//token检查-----------------------------------------------
		Result tokenValidResult=tokenService.validate(token);
		if(tokenValidResult.status==Result.RESULT_TOKEN_INVALID)
			return tokenValidResult;
		//--------------------------------------------------------
		Result result=new Result();
		Message message = mMsgService.findMessage(msgId);
		if(message.status==Message.STATUS_DELETE){
			result.status=Result.RESULT_COMMAND_INVALID;
			result.msg="重复操作";
			return result;
		}
		
		
		mMsgService.makeDeleted(msgId);
		
		result.status=0;
		result.msg="操作成功!";
		return result;
		
    }
}
