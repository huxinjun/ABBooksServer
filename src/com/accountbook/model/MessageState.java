package com.accountbook.model;

/**
 * 消息状态
 * @author xinjun
 *
 */
public class MessageState {
	

	public long id;
	public String msg_id;
	public String pid;
	public int state;
	
	public MessageState(String msg_id, String pid, int state) {
		super();
		this.msg_id = msg_id;
		this.pid = pid;
		this.state = state;
	}
	
	
}
