package com.accountbook.service;

import java.util.List;


import com.accountbook.model.Friend;
import com.accountbook.model.UserInfo;
/**
 * 好友
 * @author XINJUN
 *
 */
public interface IFriendService {
	public boolean isFriend(String oneId, String anotherId);
	public List<UserInfo> findAll(String meId);
	public void newFriend(Friend friend);
	public void killFriend(int id);
}
