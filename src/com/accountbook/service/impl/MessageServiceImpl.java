package com.accountbook.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.MessageDao;
import com.accountbook.modle.Message;
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
	public List<Message> findMessage(String toId) {
		return dao.queryById(toId);
	}
	@Override
	public List<Message> findInviteMessage(String toId) {
		return dao.queryInviteMsgById(toId);
	}

	@Override
	public int getUnreadCount() {
		return dao.queryUnreadCount();
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
