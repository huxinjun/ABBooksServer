package com.accountbook.modle.result;

import java.util.List;

import com.accountbook.modle.Group;
import com.accountbook.modle.UserInfo;

/**
 * 分组详情
 * @author XINJUN
 *
 */
public class GroupResult extends Result{

	/**是否是管理员*/
	public boolean isAdmin;
	/**是否是组员*/
	public boolean isMember;
	public Group group;
	public List<UserInfo> users;
}
