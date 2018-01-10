package com.accountbook.model;

/**
 * 消息状态
 * @author xinjun
 *
 */
public class MessageState {
	

	public long id;
	public String msgId;
	public String pid;
	public int state;
	
	public MessageState(String msgId, String pid, int state) {
		super();
		this.msgId = msgId;
		this.pid = pid;
		this.state = state;
	}
	
	
}
