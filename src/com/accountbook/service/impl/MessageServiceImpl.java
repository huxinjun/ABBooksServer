package com.accountbook.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.MessageDao;
import com.accountbook.model.Message;
import com.accountbook.service.IMessageService;

@Service
public class MessageServiceImpl implements IMessageService{
	
	@Autowired
	MessageDao dao;

	@Override
	public void newMessage(Message data) {
		dao.insert(data);
	}
	
	@Override
	public Message findMessage(int id) {
		return dao.query(id);
	}
	@Override
	public List<Message> findMessage(String acceptId) {
		return dao.queryAllMsgById(acceptId);
	}
	@Override
	public List<Message> findInviteMessage(String acceptId,String type) {
		return dao.queryInviteMsgById(acceptId,type);
	}
	@Override
	public List<Message> findSystemMsgById(String acceptId) {
		return dao.querySystemMsgById(acceptId);
	}

	@Override
	public int getUnreadCount(String acceptId) {
		return dao.queryUnreadCount(acceptId);
	}
	@Override
	public int getInviteUnreadCount(String acceptId) {
		return dao.queryInviteUnreadCount(acceptId);
	}
	@Override
	public int getSystemUnreadCount(String acceptId) {
		return dao.querySystemUnreadCount(acceptId);
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

	

	

}
