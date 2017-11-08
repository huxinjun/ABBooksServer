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
import com.accountbook.model.Member;
import com.accountbook.model.Message;
import com.accountbook.model.PayTarget;
import com.accountbook.model.UserInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.IAccountService;
import com.accountbook.service.IFriendService;
import com.accountbook.service.IGroupService;
import com.accountbook.service.IMessageService;
import com.accountbook.service.ITokenService;
import com.accountbook.service.IUserService;


@Controller
@RequestMapping("/msg")
public class MessageController {
	

	@Autowired
	ITokenService tokenService;
	
	@Autowired
	IMessageService msgService;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	IFriendService friendService;
	
	@Autowired
	IAccountService accountService;
	
	@Autowired
	IGroupService groupService;
	
	
	
	
	@ResponseBody
    @RequestMapping("/user")
    
    public Object getUserMessage(HttpServletRequest request,HttpServletResponse response,String userId,Integer pageIndex,Integer pageSize){
    	String findId=request.getAttribute("userid").toString();
    	SimpleDateFormat format=new SimpleDateFormat("MM月dd日");
    	
    	UserInfo me = userService.findUser(findId);
    	UserInfo he = userService.findUser(userId);
    	
    	Result result=new Result();
    	
    	
    	List<Message> msgs = msgService.findUserMsgs(findId, userId, pageIndex, pageSize);
    	
    	List<Result> resultMsgs=new ArrayList<>();
    	for(Message msg:msgs){
    		Result msgResult=new Result();
    		String accountId =msg.content.split(":")[1];;
    		String memberId = null;
    		String targetId=null;
    		Account account = accountService.findAccount(accountId);
    		
    		if(msg.content.startsWith("[Create]")){
    			
    			String dateStr=format.format(new Date(account.getDateTimestamp().getTime()));
    			msgResult.put("date",dateStr);
    			msgResult.put("msgType",Message.MESSAGE_TYPE_ACCOUNT_CREATE);
    			msgResult.put("type",account.getType());
    			msgResult.put("name",account.getName());
    			msgResult.put("desc",account.getDescription());
    			msgResult.put("paidIn",account.getPaidIn());
    			msgResult.put("members",account.getMembers());
    			
    			
    		}else if(msg.content.startsWith("[CreateInner]")){
    			memberId=msg.content.split(":")[2];
    			
    			String dateStr=format.format(new Date(account.getDateTimestamp().getTime()));
    			msgResult.put("date",dateStr);
    			msgResult.put("msgType",Message.MESSAGE_TYPE_ACCOUNT_CREATE_INNER);
    			msgResult.put("memberId",memberId);
    			msgResult.put("type",account.getType());
    			msgResult.put("name",account.getName());
    			msgResult.put("desc",account.getDescription());
    			msgResult.put("paidIn",account.getPaidIn());
    			
    			
    			List<UserInfo> users = groupService.findUsersByGroupId(memberId);
    			List<Member> groupMembers=new ArrayList<>();
    			for(UserInfo u:users)
    				for(Member m:account.getMembers())
    					if(u.id.equals(m.getMemberId()))
    						groupMembers.add(m);
    			msgResult.put("members",groupMembers);
    			
    		}else if(msg.content.startsWith("[Settle]")){
    			targetId=msg.content.split(":")[2];
    			PayTarget target = accountService.findPayTarget(targetId);
    			
    			String dateStr=format.format(new Date(account.getDateTimestamp().getTime()));
    			msgResult.put("date",dateStr);
    			msgResult.put("msgType",Message.MESSAGE_TYPE_ACCOUNT_SETTLE);
    			msgResult.put("targetId",targetId);
    			msgResult.put("money",target.getMoney());
    			
    			
    		}
    		
    		msgResult.put("fromId", msg.fromId);
    		msgResult.put("toId", msg.toId);
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
    @RequestMapping("/chat")
    
    public Object getInviteMessage(HttpServletRequest request){
    	String findId=request.getAttribute("userid").toString();
    	
    	//TODO 1.处理时间显示
    	//TODO 2.加入未读个数
    	
    	
    	return new Result(Result.RESULT_OK, "查询聊天列表成功").put("chats", msgService.findChatList(findId));
    	
	}
	
	
    
    @ResponseBody
    @RequestMapping("/invite")
    
    public Object getInviteMessage(HttpServletRequest request,HttpServletResponse response,String type/**type必须:1帐友邀请,2加入组邀请*/){
    	String findId=request.getAttribute("userid").toString();
    	
    	
    	Result result=new Result();
    	
    	
    	List<Message> msgs = msgService.findInviteMsgs(findId);
    	
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
		msgService.makeAccepted(msgId);
		
		Message message = msgService.findMessage(msgId);
		
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
		Message message = msgService.findMessage(msgId);
		if(message.state==Message.STATUS_INVITE_ACCEPT ||message.state==Message.STATUS_INVITE_REFUSE){
			return result.put(Result.RESULT_COMMAND_INVALID, "重复操作!");
		}
		
		
		msgService.makeRefused(msgId);
		
		return result.put(Result.RESULT_OK, "操作成功!");
		
    }
    
    @ResponseBody
    @RequestMapping("/invite/delete")
    public Object delete(HttpServletRequest request,HttpServletResponse response,int msgId){

		Result result=new Result();
		Message message = msgService.findMessage(msgId);
		if(message.state==Message.STATUS_DELETE){
			return result.put(Result.RESULT_COMMAND_INVALID, "重复操作!");
		}
		
		
		msgService.makeDeleted(msgId);
		
		return result.put(Result.RESULT_OK, "操作成功!");
		
    }
}
