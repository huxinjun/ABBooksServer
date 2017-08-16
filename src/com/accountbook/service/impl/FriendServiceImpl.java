package com.accountbook.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.FriendDao;
import com.accountbook.modle.Friend;
import com.accountbook.service.IFriendService;
/**
 * 好友
 * @author XINJUN
 *
 */
@Service
public class FriendServiceImpl implements IFriendService {
	
	@Autowired
	FriendDao dao;

	@Override
	public List<Friend> findAll(String meId) {
		return dao.query(meId);
	}

	@Override
	public void newFriend(Friend friend) {
		dao.insert(friend);
	}

	@Override
	public void killFriend(int id) {
		dao.delete(id);
	}

	@Override
	public boolean isFriend(String oneId, String anotherId) {
		Friend friend = dao.queryByTwoUserId(oneId, anotherId);
		System.out.println("oneId:"+oneId);
		System.out.println("anotherId:"+anotherId);
		System.out.println("aaaaa:"+friend);
		if(friend==null)
			return false;
		return true;
	}

}
