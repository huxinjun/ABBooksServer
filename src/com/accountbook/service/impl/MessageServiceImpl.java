package com.accountbook.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.MessageDao;
import com.accountbook.model.Message;
import com.accountbook.service.IMessageService;
import com.accountbook.utils.CommonUtils;
import com.accountbook.utils.CommonUtils.Limit;

@Service
public class MessageServiceImpl implements IMessageService{
	
	@Autowired
	MessageDao dao;

	@Override
	public void newMessage(Message data) {
		dao.insert(data);
	}
	@Override
	public void newMessage(int type, String from, String to, String content) {
		Message msg=new Message();
		msg.fromId=from;
		msg.toId=to;
		msg.type=type;
		msg.content=content;
		msg.timeMiles=Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		msg.state=Message.STATUS_UNREAD;
		newMessage(msg);
	}

	@Override
	public void makeReaded(int id) {
		dao.updateStatus(id, Message.STATUS_READED);
	}

	@Override
	public void makeDeleted(int id) {
		dao.updateStatus(id, Message.STATUS_DELETE);
	}

	@Override
	public void makeAccepted(int id) {
		dao.updateStatus(id, Message.STATUS_INVITE_ACCEPT);
	}

	@Override
	public void makeRefused(int id) {
		dao.updateStatus(id, Message.STATUS_INVITE_REFUSE);
	}
	@Override
	public List<Message> findUserMsgs(String user1Id, String user2Id,Integer pageIndex,Integer pageSize) {
		Limit limit = CommonUtils.getLimit(pageIndex, pageSize);
		List<Message> msgs=dao.queryUserMsgs(new HashMap<String,Object>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("user1Id",user1Id);
				put("user2Id",user2Id);
				put("ls", limit.start);
				put("lc", limit.count);
			}
		});
		return msgs;
	}
	@Override
	public List<Message> findInviteMsgs(String userId) {
		return dao.queryInviteMsgs(userId);
	}
	@Override
	public int getUserUnreadCount(String user1Id, String user2Id) {
		return dao.queryUserUnreadCount(new HashMap<String,Object>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("user1Id",user1Id);
				put("user2Id",user2Id);
			}
		});
	}
	@Override
	public int getInviteUnreadCount(String userId) {
		return dao.queryInviteUnreadCount(userId);
	}
	@Override
	public Message findMessage(int id) {
		return dao.queryMsg(id);
	}

	

	

	

}
