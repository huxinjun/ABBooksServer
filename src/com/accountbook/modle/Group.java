package com.accountbook.modle;

/**
 * 分组
 *
 */
public class Group {
	
	public String id;
	public String name;
	public String adminId;
	public String icon;
	public String desc;
	public long time;
	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", adminId=" + adminId + ", icon=" + icon + ", desc=" + desc
				+ ", time=" + time + "]";
	}
	
	

}
