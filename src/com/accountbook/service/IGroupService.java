package com.accountbook.service;

import java.util.List;

import com.accountbook.model.Group;
import com.accountbook.model.UserInfo;

public interface IGroupService {
	
	/**查询组成员*/
	public List<UserInfo> findUsersByGroupId(String groupId);
	/**查询组成员个数*/
	public int findUsersCountByGroupId(String groupId);
	
	/**是否加入了某个组*/
	public boolean isGroupMember(String userId,String groupId);
	/**加入的组*/
	public List<Group> findJoinGroups(String userId);
	/**组中加入用户*/
	public void joinGroup(String groupId,String userId);
	/**删除组中用户*/
	public void exitGroup(String groupId,String userId);
	
	/**查询组*/
	public Group queryGroupInfo(String groupId);
	/**添加组*/
	public void addGroup(Group data);
	/**更新组*/
	public void updateGroupInfo(Group data);
	
	
}
