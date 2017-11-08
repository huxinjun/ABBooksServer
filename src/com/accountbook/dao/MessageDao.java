package com.accountbook.dao;


import java.util.List;
import java.util.Map;

import com.accountbook.model.Message;

/**
 * 消息dao
 * @author xinjun
 *
 */
public interface MessageDao {
	public void insert(Message data);
	public void updateStatus(int id,int status);
	
	public List<Message> queryChatList(String userId);
	
	
	public Message queryMsg(int id);
	public List<Message> queryUserMsgs(Map<String,Object> params);
	public List<Message> queryInviteMsgs(String userId);
	
	public int queryUserUnreadCount(Map<String,Object> params);
	public int queryInviteUnreadCount(String userId);
}
