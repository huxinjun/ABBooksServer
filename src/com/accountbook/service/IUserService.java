package com.accountbook.service;

import java.util.List;

import com.accountbook.modle.UserInfo;

public interface IUserService {
	
	public List<UserInfo> searchUser(String nickname);
	public UserInfo findUser(String id);
	public void newUSer(UserInfo user);
	public void updateUser(UserInfo user);
	
	
}
