package com.abbooks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.abbooks.dao.UserDao;
import com.abbooks.modle.TestModel;
import com.abbooks.modle.UserInfo;

public interface IUserService {
	
	public List<UserInfo> searchUser(String name);
	public UserInfo findUser(String id);
	public void newUSer(UserInfo user);
	public void updateUser(UserInfo user);
	
	
}
