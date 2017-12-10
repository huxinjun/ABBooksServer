package com.accountbook.dao;


import java.util.List;
import java.util.Map;

import com.accountbook.model.UserInfo;

public interface UserDao {
	public List<UserInfo> queryByName(String nickname);
	public UserInfo queryById(String id);
	public void insert(UserInfo user);
	public void update(UserInfo user);
	public void openOrCloseNotif(Map<String,Object> params);
}
