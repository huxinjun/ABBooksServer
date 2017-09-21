package com.accountbook.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.GroupDao;
import com.accountbook.modle.Group;
import com.accountbook.modle.UserInfo;
import com.accountbook.service.IGroupService;
/**
 * 分组
 * @author XINJUN
 *
 */
@Service
public class GroupServiceImpl implements IGroupService {
	
	@Autowired
	GroupDao dao;

	@Override
	public List<UserInfo> findUsersByGroupId(String groupId) {
		return dao.queryUsers(groupId);
	}
	@Override
	public int findUsersCountByGroupId(String groupId) {
		return dao.queryUsersCount(groupId);
	}
	

	@Override
	public List<Group> findJoinGroups(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("serial")
	@Override
	public void joinGroup(String groupId, String userId) {
		dao.insertUser(new HashMap<String,String>(){
			{
				put("groupId", groupId+"");
				put("userId", userId);
			}
		});
	}

	@SuppressWarnings("serial")
	@Override
	public void exitGroup(String groupId, String userId) {
		dao.deleteUser(new HashMap<String,String>(){
			{
				put("groupId", groupId+"");
				put("userId", userId);
			}
		});
		
	}

	@Override
	public Group queryGroupInfo(String groupId) {
		return dao.queryById(groupId);
	}

	@Override
	public void addGroup(Group data) {
		dao.insert(data);
	}

	@Override
	public void updateGroupInfo(Group data) {
		dao.update(data);
	}

	


}
