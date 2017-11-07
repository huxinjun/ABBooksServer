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

import com.accountbook.model.Account;
import com.accountbook.model.Friend;
import com.accountbook.model.Message;
import com.accountbook.model.PayTarget;
import com.accountbook.model.UserInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.IAccountService;
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
	
	@Autowired
	IAccountService accountService;
	
	
	
	
	@ResponseBody
    @RequestMapping("/user")
    
    public Object getUserMessage(HttpServletRequest request,HttpServletResponse response,String userId,Integer pageIndex,Integer pageSize){
    	String findId=request.getAttribute("userid").toString();
    	SimpleDateFormat format=new SimpleDateFormat("MM月dd日");
    	
    	UserInfo me = userService.findUser(findId);
    	UserInfo he = userService.findUser(userId);
    	
    	Result result=new Result();
    	
    	
    	List<Message> msgs = mMsgService.findUserMsgs(findId, userId, pageIndex, pageSize);
    	
    	List<Result> resultMsgs=new ArrayList<>();
    	for(Message msg:msgs){
    		Result msgResult=new Result();
    		String accountId = null;
    		@SuppressWarnings("unused")
			String memberId = null;
    		String targetId=null;
    		if(msg.content.startsWith("[Create]")){
    			accountId=msg.content.split(":")[1];
    			
    			Account account = accountService.findAccount(accountId);
    			String dateStr=format.format(new Date(account.getDateTimestamp().getTime()));
    			msgResult.put("content",dateStr+account.getName()+"("+account.getDescription()+")"+"花费了"+account.getPaidIn()+"元");
    			
    		}else if(msg.content.startsWith("[CreateInner]")){
    			accountId=msg.content.split(":")[1];
    			memberId=msg.content.split(":")[2];
    			
    			Account account = accountService.findAccount(accountId);
    			String dateStr=format.format(new Date(account.getDateTimestamp().getTime()));
    			msgResult.put("content","完善了"+dateStr+account.getName()+"的账单,我们所在的组花费了"+account.getPaidIn()+"元");
    			
    			
    		}else if(msg.content.startsWith("[Settle]")){
    			accountId=msg.content.split(":")[1];
    			targetId=msg.content.split(":")[2];
    			
    			PayTarget target = accountService.findPayTarget(targetId);
    			msgResult.put("content","向你支付了"+target.getMoney()+"元");
    			
    		}
    		
    		msgResult.put("accountId", accountId);
			if(me.id.equals(msg.fromId))
				msgResult.put("icon", me.icon);
			else
				msgResult.put("icon", he.icon);
    		
    		resultMsgs.add(msgResult);
    	}
    	
    	
    	return result.put(Result.RESULT_OK, "查询成功").put("datas", resultMsgs);
    	
    }
	
	
    
    @ResponseBody
    @RequestMapping("/invite")
    
    public Object getInviteMessage(HttpServletRequest request,HttpServletResponse response,String type/**type必须:1帐友邀请,2加入组邀请*/){
    	String findId=request.getAttribute("userid").toString();
    	
    	
    	Result result=new Result();
    	
    	
    	List<Message> msgs = mMsgService.findInviteMsgs(findId);
    	
    	List<Result> resultMsgs=new ArrayList<>();
    	for(Message msg:msgs){
    		Date date=new Date(msg.timeMiles.getTime());
    		DateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
    		String dateStr=format.format(date);
    		UserInfo findUser = userService.findUser(msg.fromId);
    		
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
		
		boolean isFriend = friendService.isFriend(message.toId, message.fromId);
		if(isFriend){
			return result.put(Result.RESULT_COMMAND_INVALID, "已经是好友啦!");
		}
		
		Friend friend=new Friend();
		friend.inviteId=message.fromId;
		friend.acceptId=message.toId;
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
