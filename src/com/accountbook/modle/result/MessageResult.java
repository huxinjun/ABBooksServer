package com.accountbook.modle.result;

public class MessageResult{
	
	public int id;
	public String icon;
	public String nickname;
	public String msg;
	public String time;
	public int status;
	public MessageResult(int id,String icon, String nickname, String msg, String time, int status) {
		
		this.id = id;
		this.icon = icon;
		this.nickname = nickname;
		this.msg = msg;
		this.time = time;
		this.status = status;
	}
	
	

}
