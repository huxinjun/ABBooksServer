package com.accountbook.service;

import java.util.List;

import com.accountbook.model.Message;

public interface IMessageService {
	
	public void newMessage(Message data);
	public void newMessage(int type,String from,String to,String content);
	public Message findMessage(String id);
	public void delete(String id);
	
	public List<Message> findChatList(String userId);//需要连接状态表
	
	public List<Message> findUserMsgs(String user1Id,String user2Id,Integer pageIndex,Integer pageSize);//需要连接状态表
	public int getUserUnreadCount(String findId,String user1Id,String user2Id);//需要连接状态表
	
	public List<Message> findInviteMsgs(String userId,Integer pageIndex,Integer pageSize);
	public int getInviteUnreadCount(String userId);
	
	public List<Message> findAccountMsgs(String accountId);
	
	public void makeReaded(String findId,String id);//需要连接状态表
	public void makeDeleted(String findId,String id);//需要连接状态表
	public void makeAccepted(String findId,String id);//需要连接状态表
	public void makeRefused(String findId,String id);//需要连接状态表
	
	public void updateStatusBatch(String user1Id,String user2Id,int state);//需要连接状态表
	public boolean isRepeatInvite(String user1Id,String user2Id);
	
	
}
