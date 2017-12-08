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
import com.accountbook.utils.CommonUtils;
import com.accountbook.utils.TextUtils;


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
    		String accountId =msg.content.split(":")[1];
    		String memberId = null;
    		String targetId=null;
    		Account account = accountService.findAccount(accountId);
    		String dateStr=format.format(new Date(account.getDateTimestamp().getTime()));
    		
    		if(msg.content.startsWith("[Create]")){
    			
    			msgResult.put("id",msg.id);
    			msgResult.put("date",dateStr);
    			msgResult.put("msgType",Message.MESSAGE_TYPE_ACCOUNT_CREATE);
    			msgResult.put("type",account.getType());
    			msgResult.put("name",account.getName());
    			msgResult.put("desc",account.getDescription());
    			msgResult.put("paidIn",account.getPaidIn());
    			msgResult.put("members",account.getMembers());
    			
    			
    		}else if(msg.content.startsWith("[CreateInner]")){
    			memberId=msg.content.split(":")[2];
    			Member member=getMemberById(account.getMembers(),memberId);
    			
    			msgResult.put("id",msg.id);
    			msgResult.put("date",dateStr);
    			msgResult.put("msgType",Message.MESSAGE_TYPE_ACCOUNT_CREATE_INNER);
    			msgResult.put("memberId",memberId);
    			msgResult.put("type",account.getType());
    			msgResult.put("name",account.getName());
    			msgResult.put("desc",account.getDescription());
    			msgResult.put("paidIn",member.getPaidIn());
    			msgResult.put("shouldPay",member.getShouldPay());
    			
    			
    			List<UserInfo> users = groupService.findUsersByGroupId(memberId);
    			List<Member> groupMembers=new ArrayList<>();
    			for(UserInfo u:users)
    				for(Member m:account.getMembers())
    					if(u.id.equals(m.getMemberId()))
    						groupMembers.add(m);
    			msgResult.put("members",groupMembers);
    			
    		}else if(msg.content.startsWith("[Settle]")){
    			targetId=msg.content.split(":")[2];
    			String paidMoney=msg.content.split(":").length>3?msg.content.split(":")[3]:null;
    			PayTarget target = accountService.findPayTarget(targetId);
    			
    			msgResult.put("id",msg.id);
    			msgResult.put("date",dateStr);
    			msgResult.put("msgType",Message.MESSAGE_TYPE_ACCOUNT_SETTLE);
    			msgResult.put("targetId",targetId);
    			msgResult.put("money",TextUtils.isEmpty(paidMoney)?target.getMoney():Double.parseDouble(paidMoney));
    			msgResult.put("paidId",target.getPaidId());
    			msgResult.put("paidName",userService.findUser(target.getPaidId()).nickname);
    			msgResult.put("receiptId",target.getReceiptId());
    			msgResult.put("receiptName",userService.findUser(target.getReceiptId()).nickname);
    			
    			
    			
    		}
    		
    		msgResult.put("fromId", msg.fromId);
    		msgResult.put("toId", msg.toId);
    		msgResult.put("accountId", accountId);
			if(me.id.equals(msg.fromId))
				msgResult.put("icon", me.icon);
			else
				msgResult.put("icon", he.icon);
    		
    		resultMsgs.add(msgResult);
    		
    		//设置为已读
    		if(msg.state==Message.STATUS_UNREAD)
    			msgService.makeReaded(msg.id);
    	}
    	
    	result.put("hasNextPage",msgs.size()==0?false:true);
		result.put("pageIndex", pageIndex==null || pageIndex<=0 ? CommonUtils.PAGE_DEFAULT_INDEX : pageIndex);
		result.put("pageSize", pageSize==null || pageSize<=0 ? CommonUtils.PAGE_DEFAULT_SIZE : pageSize);
    	
    	
    	return result.put(Result.RESULT_OK, "查询成功").put("msgs", resultMsgs);
    	
    }
	
	
	private Member getMemberById(List<Member> members, String memberId) {
		for (Member member : members)
			if (member.getMemberId().equals(memberId))
				return member;
		return null;
	}
	
	@ResponseBody
    @RequestMapping("/readAll")
    public Object makeUserMsgsReaded(HttpServletRequest request,String userId){
		String findId=request.getAttribute("userid").toString();
		
		msgService.updateStatusBatch(findId, userId,Message.STATUS_READED);
		
		return new Result(Result.RESULT_OK, "已经全部标记为已读");
	}
	
	@ResponseBody
    @RequestMapping("/deleteAll")
    public Object makeUserMsgsDeleted(HttpServletRequest request,String userId){
		String findId=request.getAttribute("userid").toString();
		
		msgService.updateStatusBatch(findId, userId,Message.STATUS_DELETE);
		
		return new Result(Result.RESULT_OK, "已经全部删除");
	}
	
	@ResponseBody
    @RequestMapping("/chat")
    
    public Object getChatList(HttpServletRequest request){
    	String findId=request.getAttribute("userid").toString();
    	
    	 List<Message> chatList = msgService.findChatList(findId);
    	 List<Result> results=new ArrayList<>();
    	 System.out.println(chatList);
    	 for(Message msg:chatList){
    		 
    		//不添加重复的,因为数据库是用groupby查的,会出现重复的
    		 boolean repeat=false;
    		 for(Result res:results)
    			 if(msg.fromId.equals(res.get("userId")) || msg.toId.equals(res.get("userId"))){
    				 repeat=true;
    				 break;
    			 }
    		 if(repeat && msg.type==3)
				 continue;
    		 
    		 
    		 
    		 Result msgResult=new Result().put(msg);
    		 msgResult.remove("time");
    		 msgResult.remove("content");
    		 msgResult.remove("fromId");
    		 msgResult.remove("toId");
    		 msgResult.put("userId", findId.equals(msg.fromId)?msg.toId:msg.fromId);
    		 msgResult.put("date", CommonUtils.getSinceTimeString2(new Date(msg.time.getTime())));
    		 if(msg.type==1 ||msg.type==2)
    			 msgResult.put("unreadCount",msgService.getInviteUnreadCount(findId));
			 else if(msg.type==3)
				 msgResult.put("unreadCount",msgService.getUserUnreadCount(msg.fromId,msg.toId));
    		 
    		 UserInfo fromUser = userService.findUser(msg.fromId);
    		 UserInfo toUser = userService.findUser(msg.toId);
    		 if(fromUser==null || toUser==null)
    			 continue;
    		 UserInfo he=fromUser.id.equals(findId)?toUser:fromUser;
    		 
    		 if(he!=null){
    			 msgResult.put("name",he.nickname);
    			 msgResult.put("icon",he.icon);
    		 }
    		 
    		 
    		 if(msg.type==1 ||msg.type==2){
    			 //邀请消息
    			 msgResult.put("name","邀请消息");
    			 msgResult.put("icon","http://img4.imgtn.bdimg.com/it/u=3386476608,1677035006&fm=27&gp=0.jpg");
    		 }else if(msg.type==3){
    			 //帐友聊天
    			 String accountId =msg.content.split(":")[1];
        		 String memberId = null;
         		 String targetId=null;
        		 Account account = accountService.findAccount(accountId);
        		 String dateStr=CommonUtils.getSinceTimeString2(new Date(account.getDateTimestamp().getTime()));
         		
         		if(msg.content.startsWith("[Create]")){
         			msgResult.put("date",dateStr);
         			msgResult.put("content","[新账单]"+account.getPaidIn()+"元");
         			
         		}else if(msg.content.startsWith("[CreateInner]")){
         			memberId=msg.content.split(":")[2];
         			Member member=getMemberById(account.getMembers(),memberId);
         			
         			msgResult.put("date",dateStr);
         			msgResult.put("content","[组内账单]"+member.getShouldPay()+"元");
         			
         		}else if(msg.content.startsWith("[Settle]")){
         			targetId=msg.content.split(":")[2];
         			String paidMoney=msg.content.split(":").length>3?msg.content.split(":")[3]:null;
         			PayTarget target = accountService.findPayTarget(targetId);
         			
         			String opt=findId.equals(msg.fromId)?findId.equals(target.getPaidId())?"付款":"收款":findId.equals(target.getPaidId())?"收款":"付款";
         			
         			msgResult.put("date",dateStr);
         			msgResult.put("content","["+opt+"]"+(TextUtils.isEmpty(paidMoney)?target.getMoney():paidMoney)+"元");
         		}
    			 
    		 }
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 
    		 results.add(msgResult);
    	 }
    	
    	
    	return new Result(Result.RESULT_OK, "查询聊天列表成功").put("chats",results);
    	
	}
	
	
    
    @ResponseBody
    @RequestMapping("/invite")
    
    public Object getInviteMessage(HttpServletRequest request,HttpServletResponse response,String type/**type必须:1帐友邀请,2加入组邀请*/){
    	String findId=request.getAttribute("userid").toString();
    	
    	
    	Result result=new Result();
    	
    	
    	List<Message> msgs = msgService.findInviteMsgs(findId);
    	
    	List<Result> resultMsgs=new ArrayList<>();
    	for(Message msg:msgs){
    		Date date=new Date(msg.time.getTime());
    		DateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
    		String dateStr=format.format(date);
    		UserInfo findUser = userService.findUser(msg.fromId);
    		if(findUser!=null){
    			Result msgResult=new Result();
    			
    			msgResult.put("id",msg.id);
    			msgResult.put("icon",findUser.avatarUrl);
    			msgResult.put("nickname",findUser.nickname);
    			msgResult.put("msg",msg.content);
    			msgResult.put("time",dateStr);
    			msgResult.put("state",msg.state);
    			
    			resultMsgs.add(msgResult);
    		}
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
