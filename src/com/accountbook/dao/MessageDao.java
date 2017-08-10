package com.accountbook.dao;


import com.accountbook.modle.Message;

/**
 * 消息dao
 * @author xinjun
 *
 */
public interface MessageDao {
	public void insert(Message data);
	public Message queryById(int id);
	public int queryUnreadCount();
	public void updateStatus(int id,int status);
}
