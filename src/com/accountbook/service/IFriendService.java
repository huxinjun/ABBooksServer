package com.accountbook.service;

import java.util.List;

import com.accountbook.modle.Friend;
/**
 * 好友
 * @author XINJUN
 *
 */
public interface IFriendService {
	public boolean isFriend(String oneId, String anotherId);
	public List<Friend> findAll(String meId);
	public void newFriend(Friend friend);
	public void killFriend(int id);
}
