package com.abbooks.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abbooks.dao.UserDao;
import com.abbooks.modle.UserInfo;
import com.abbooks.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	
	@Autowired
	UserDao userDao;
	@Override
	public UserInfo findUser(String id) {
		return userDao.query(id);
	}
	@Override
	public void newUSer(UserInfo user) {
		userDao.insert(user);
	}
	@Override
	public void updateUser(UserInfo user) {
		userDao.update(user);
	}

}
