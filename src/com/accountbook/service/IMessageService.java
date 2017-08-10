package com.accountbook.service;

import com.accountbook.modle.Message;

public interface IMessageService {
	
	public void newMessage(Message data);
	public Message findMessage(int id);
	public int getUnreadCount();
	
	public void makeReaded(int id);
	public void makeDeleted(int id);
	public void makeAccepted(int id);
	public void makeRefused(int id);
}
