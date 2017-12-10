package com.accountbook.service;

import java.util.List;

import com.accountbook.model.Message;

public interface IMessageService {
	
	public void newMessage(Message data);
	public void newMessage(int type,String from,String to,String content);
	
	public List<Message> findChatList(String userId);
	/**
	 * 根据用户id查询
	 */
	public Message findMessage(long id);
	public List<Message> findUserMsgs(String user1Id,String user2Id,Integer pageIndex,Integer pageSize);
	public List<Message> findInviteMsgs(String userId,Integer pageIndex,Integer pageSize);
	
	public int getUserUnreadCount(String user1Id,String user2Id);
	public int getInviteUnreadCount(String userId);
	public List<Message> findAccountMsgs(String accountId);
	
	public void makeReaded(long id);
	public void makeDeleted(long id);
	public void makeAccepted(long id);
	public void makeRefused(long id);
	public void updateStatusBatch(String user1Id,String user2Id,int state);
	
	public boolean isRepeatInvite(String user1Id,String user2Id);
	
	public void delete(long id);
}
