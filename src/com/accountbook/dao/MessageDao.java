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
	public void updateStatus(long id,long status);
	public void updateStatusBatch(Map<String,Object> params);
	public void delete(long id);
	
	public List<Message> queryChatList(String userId);
	
	
	public Message queryMsg(long id);
	public List<Message> queryUserMsgs(Map<String,Object> params);
	public List<Message> queryInviteMsgs(Map<String,Object> params);
	public List<Message> queryAccountMsgs(String p1,String p2,String p3);
	
	public boolean isRepeatInvite(String user1Id,String user2Id);
	
	public int queryUserUnreadCount(Map<String,Object> params);
	public int queryInviteUnreadCount(String userId);
}
