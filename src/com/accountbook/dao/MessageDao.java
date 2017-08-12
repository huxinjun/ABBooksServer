package com.accountbook.dao;


import java.util.List;

import com.accountbook.modle.Message;

/**
 * 消息dao
 * @author xinjun
 *
 */
public interface MessageDao {
	public void insert(Message data);
	public List<Message> queryById(String toId);
	public List<Message> queryInviteMsgById(String toId);
	public int queryUnreadCount();
	public void updateStatus(int id,int status);
}
