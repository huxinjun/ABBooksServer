package com.accountbook.dao;


import java.util.List;


import com.accountbook.model.Friend;
import com.accountbook.model.UserInfo;

public interface FriendDao {
	public void insert(Friend data);
	public void delete(int id);
	public List<UserInfo> query(String userId);
	public Friend queryByTwoUserId(String oneId, String anotherId);
}
