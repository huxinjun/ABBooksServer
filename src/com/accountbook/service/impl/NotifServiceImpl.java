package com.accountbook.service.impl;


import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.NotifDao;
import com.accountbook.model.Form;
import com.accountbook.service.INotifService;

@Service
public class NotifServiceImpl implements INotifService {

	@Autowired
	NotifDao dao;
	
	@Override
	public void switcher(String id, boolean isOpen) {
		dao.switcher(new HashMap<String,Object>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("id", id);
				put("isOpen", isOpen);
			}
		});
	}

	@Override
	public void add(Form form) {
		dao.insert(form);
	}

	@Override
	public Form getOne(String userId) {
		//每次拿之前先删除过期的form
		dao.deleteInvalid(userId);
		return dao.queryOne(userId);
	}

	@Override
	public int getValidCount(String userId) {
		
		return dao.queryValidCount(userId);
	}

	@Override
	public void deleteInvalid(String userId) {
		dao.deleteInvalid(userId);
	}

	@Override
	public boolean isNotifOpen(String userId) {
		return dao.isNotifOpen(userId);
	}

	@Override
	public void delete(long id) {
		dao.delete(id);
	}

}
