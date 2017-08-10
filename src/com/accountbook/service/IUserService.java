package com.accountbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.accountbook.dao.UserDao;
import com.accountbook.modle.TestModel;
import com.accountbook.modle.UserInfo;

public interface IUserService {
	
	public List<UserInfo> searchUser(String nickname);
	public UserInfo findUser(String id);
	public void newUSer(UserInfo user);
	public void updateUser(UserInfo user);
	
	
}
