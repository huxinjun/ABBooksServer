package com.accountbook.modle;

/**
 * 消息
 * @author xinjun
 *
 */
public class Message {
	
	public static final int MESSAGE_TYPE_SYSTEM=0;
	public static final int MESSAGE_TYPE_INVITE=1;
	public static final int MESSAGE_TYPE_OTHER=2;
	
	
	public static final int STATUS_UNREAD=0;
	public static final int STATUS_READED=1;
	public static final int STATUS_DELETE=2;
	
	public static final int STATUS_INVITE_ACCEPT=11;
	public static final int STATUS_INVITE_REFUSE=12;

	public int id;
	public String inviteId;
	public String acceptId;
	public int type;
	public String content;
	/**
	 * 创建时间 :毫秒
	 */
	public long timeMiles;
	
	/**
	 * 普通消息:0未读,1已读,2已删除
	 * 邀请消息:0未处理,1接受,2拒绝,3删除
	 */
	public int status;
}
