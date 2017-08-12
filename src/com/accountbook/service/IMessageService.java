package com.accountbook.service;

import java.util.List;

import com.accountbook.modle.Message;

public interface IMessageService {
	
	public void newMessage(Message data);
	public List<Message> findMessage(String id);
	public List<Message> findInviteMessage(String toId);
	public int getUnreadCount();
	
	public void makeReaded(int id);
	public void makeDeleted(int id);
	public void makeAccepted(int id);
	public void makeRefused(int id);
}
