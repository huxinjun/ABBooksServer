package com.abbooks.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abbooks.dao.UserDao;
import com.abbooks.modle.UserInfo;
import com.abbooks.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	
	@Override
	public void newUSer(UserInfo user) {
		userDao.insert(user);
	}
	@Override
	public void updateUser(UserInfo user) {
		userDao.update(user);
	}
	@Override
	public UserInfo findUser(String id) {
		return userDao.queryById(id);
	}
	@Autowired
	UserDao userDao;
	@Override
	public List<UserInfo> searchUser(String nickname) {
		return userDao.queryByName(nickname);
	}

}
