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
	public String qr;
	public String category;
	public long time;
	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", adminId=" + adminId + ", icon=" + icon + ", qr=" + qr
				+ ", category=" + category + ", time=" + time + "]";
	}
	
	

}
