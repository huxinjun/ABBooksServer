package com.accountbook.service;

import java.util.List;

import com.accountbook.model.Message;

public interface IMessageService {
	
	public void newMessage(Message data);
	public void newMessage(int type,String from,String to,String content);
	/**
	 * 根据用户id查询
	 */
	public Message findMessage(int id);
	public List<Message> findUserMsgs(String user1Id,String user2Id,Integer pageIndex,Integer pageSize);
	public List<Message> findInviteMsgs(String userId);
	
	public int getUserUnreadCount(String user1Id,String user2Id);
	public int getInviteUnreadCount(String userId);
	
	public void makeReaded(int id);
	public void makeDeleted(int id);
	public void makeAccepted(int id);
	public void makeRefused(int id);
}
