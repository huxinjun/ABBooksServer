package com.accountbook.dao;


import java.util.List;
import java.util.Map;

import com.accountbook.model.Message;
import com.accountbook.model.MessageState;

/**
 * 消息dao
 * @author xinjun
 *
 */
public interface MessageDao {
	public void insert(Message data);
	public void insertState(MessageState data);
	public void delete(String id);
	public void deleteState(String id);
	public Message queryMsg(String id);
	public List<Message> queryAccountMsgs(String p1,String p2,String p3);
	
	
	//下面的方法都需要连接到状态表查询或者修改
	public List<Message> queryChatList(String userId);
	
	public List<Message> queryUserMsgs(Map<String,Object> params);
	public int queryUserUnreadCount(Map<String,Object> params);
	
	public List<Message> queryInviteMsgs(Map<String,Object> params);
	public int queryInviteUnreadCount(String userId);
	public boolean isRepeatInvite(String user1Id,String user2Id);
	
	public void updateStatus(Map<String,Object> params);
	public void updateStatusBatch(Map<String,Object> params);
	public void updateStatusBatchForInvite(Map<String,Object> params);
}
