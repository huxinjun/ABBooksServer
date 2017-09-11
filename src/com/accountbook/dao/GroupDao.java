package com.accountbook.dao;


import java.util.List;
import java.util.Map;

import com.accountbook.modle.Group;
import com.accountbook.modle.UserInfo;

public interface GroupDao {
	
	/**查询组成员*/
	public List<UserInfo> queryUsers(int groupId);
	/**加入的组*/
	public List<Group> queryGroups(int userId);
	/**组中加入用户*/
	public void insertUser(Map<String,String> params);
	/**删除组中用户*/
	public void deleteUser(Map<String,String> params);
	
	/**查询组*/
	public Group queryById(int groupId);
	/**添加组*/
	public void insert(Group data);
	/**更新组*/
	public void update(Group data);
}
