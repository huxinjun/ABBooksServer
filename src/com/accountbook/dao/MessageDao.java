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
	public Message query(int id);
	public List<Message> queryAllMsgById(String acceptId);
	public List<Message> queryInviteMsgById(String acceptId,String type);
	public List<Message> querySystemMsgById(String acceptId);
	public int queryUnreadCount(String acceptId);
	public int queryInviteUnreadCount(String acceptId);
	public int querySystemUnreadCount(String acceptId);
	public void updateStatus(int id,int status);
}
