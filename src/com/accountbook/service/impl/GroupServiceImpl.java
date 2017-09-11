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
	public List<UserInfo> findUsersByGroupId(int groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Group> findJoinGroups(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("serial")
	@Override
	public void joinGroup(int groupId, String userId) {
		dao.insertUser(new HashMap<String,String>(){
			{
				put("groupId", groupId+"");
				put("userId", userId);
			}
		});
	}

	@SuppressWarnings("serial")
	@Override
	public void exitGroup(int groupId, String userId) {
		dao.deleteUser(new HashMap<String,String>(){
			{
				put("groupId", groupId+"");
				put("userId", userId);
			}
		});
		
	}

	@Override
	public Group queryGroupInfo(int groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addGroup(Group data) {
		dao.insert(data);
	}

	@Override
	public void updateGroupInfo(Group data) {
		// TODO Auto-generated method stub
		
	}


}
