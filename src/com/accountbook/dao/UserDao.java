package com.accountbook.dao;


import java.util.List;

import com.accountbook.modle.UserInfo;

public interface UserDao {
	public List<UserInfo> queryByName(String nickname);
	public UserInfo queryById(String id);
	public void insert(UserInfo user);
	public void update(UserInfo user);
}
