package com.accountbook.service;

import java.util.List;

import com.accountbook.model.Message;

public interface IMessageService {
	
	public void newMessage(Message data);
	/**
	 * 根据数据库id查询
	 */
	public Message findMessage(int id);
	/**
	 * 根据用户id查询
	 */
	public List<Message> findMessage(String acceptId);
	public List<Message> findInviteMessage(String acceptId,String type);
	public List<Message> findSystemMsgById(String acceptId);
	
	public int getUnreadCount(String acceptId);
	public int getInviteUnreadCount(String acceptId);
	public int getSystemUnreadCount(String acceptId);
	
	public void makeReaded(int id);
	public void makeDeleted(int id);
	public void makeAccepted(int id);
	public void makeRefused(int id);
}
