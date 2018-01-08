package com.accountbook.service;

import java.util.List;

import com.accountbook.model.Message;

public interface IMessageService {
	
	public void newMessage(Message data);
	public void newMessage(int type,String from,String to,String content);
	public Message findMessage(String id);
	public void delete(String id);
	public List<Message> findAccountMsgs(String accountId);
	
	
	//下面的方法都需要连接到状态表查询或者修改
	public List<Message> findChatList(String userId);
	
	public List<Message> findUserMsgs(String user1Id,String user2Id,Integer pageIndex,Integer pageSize);
	public int getUserUnreadCount(String findId,String user1Id,String user2Id);
	
	public List<Message> findInviteMsgs(String userId,Integer pageIndex,Integer pageSize);
	public int getInviteUnreadCount(String userId);
	
	
	public void makeReaded(String findId,String msgId);
	public void makeDeleted(String findId,String msgId);
	public void makeAccepted(String findId,String msgId);
	public void makeRefused(String findId,String msgId);
	
	public void updateStatusBatch(String user1Id,String user2Id,int type,int state);
	public boolean isRepeatInvite(String user1Id,String user2Id);
	
	
}
