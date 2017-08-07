package com.abbooks.dao;


import com.abbooks.modle.UserInfo;

public interface UserDao {
	public UserInfo query(String id);
	public void insert(UserInfo user);
	public void update(UserInfo user);
}
