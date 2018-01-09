package com.accountbook.service.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.MessageDao;
import com.accountbook.model.Message;
import com.accountbook.model.MessageState;
import com.accountbook.service.IMessageService;
import com.accountbook.utils.CommonUtils;
import com.accountbook.utils.CommonUtils.Limit;
import com.accountbook.utils.IDUtil;

@Service
public class MessageServiceImpl implements IMessageService{
	
	@Autowired
	MessageDao dao;

	@Override
	public void newMessage(Message data) {
		data.id=IDUtil.generateNewId();
		dao.insert(data);
		//邀请不需要记录发起者的状态
		if(data.type!=3)
			dao.insertState(new MessageState(data.id,data.fromId,Message.STATUS_READED));
		dao.insertState(new MessageState(data.id,data.toId,Message.STATUS_UNREAD));
	}
	@Override
	public void newMessage(int type, String from, String to, String content) {
		Message msg=new Message();
		msg.id=IDUtil.generateNewId();
		msg.fromId=from;
		msg.toId=to;
		msg.type=type;
		msg.content=content;
		msg.time=Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		newMessage(msg);
		//邀请不需要记录发起者的状态
		if(msg.type!=3)
			dao.insertState(new MessageState(msg.id,msg.fromId,Message.STATUS_READED));
		dao.insertState(new MessageState(msg.id,msg.toId,Message.STATUS_UNREAD));
	}

	@Override
	public void makeReaded(String findId,String msgId) {
		dao.updateStatus(new HashMap<String,Object>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("msgId",msgId);
				put("pid",findId);
				put("state", Message.STATUS_READED);
			}
		});
	}

	@Override
	public void makeDeleted(String findId,String msgId) {
		dao.updateStatus(new HashMap<String,Object>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("msgId",msgId);
				put("pid",findId);
				put("state", Message.STATUS_DELETE);
			}
		});
	}

	@Override
	public void makeAccepted(String findId,String msgId) {
		dao.updateStatus(new HashMap<String,Object>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("msgId",msgId);
				put("pid",findId);
				put("state", Message.STATUS_INVITE_ACCEPT);
			}
		});
	}

	@Override
	public void makeRefused(String findId,String msgId) {
		dao.updateStatus(new HashMap<String,Object>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("msgId",msgId);
				put("pid",findId);
				put("state", Message.STATUS_INVITE_REFUSE);
			}
		});
	}
	@Override
	public List<Message> findUserMsgs(String user1Id, String user2Id,Integer pageIndex,Integer pageSize) {
		Limit limit = CommonUtils.getLimit(pageIndex, pageSize);
		List<Message> msgs=dao.queryUserMsgs(new HashMap<String,Object>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("user1Id",user1Id);
				put("user2Id",user2Id);
				put("ls", limit.start);
				put("lc", limit.count);
			}
		});
		return msgs;
	}
	@Override
	public List<Message> findInviteMsgs(String userId,Integer pageIndex,Integer pageSize) {
		Limit limit = CommonUtils.getLimit(pageIndex, pageSize);
		List<Message> msgs=dao.queryInviteMsgs(new HashMap<String,Object>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("userId",userId);
				put("ls", limit.start);
				put("lc", limit.count);
			}
		});
		return msgs;
	}
	@Override
	public int getUserUnreadCount(String findId,String user1Id, String user2Id) {
		return dao.queryUserUnreadCount(new HashMap<String,Object>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("findId",findId);
				put("user1Id",user1Id);
				put("user2Id",user2Id);
			}
		});
	}
	@Override
	public int getInviteUnreadCount(String userId) {
		return dao.queryInviteUnreadCount(userId);
	}
	@Override
	public Message findMessage(String id) {
		return dao.queryMsg(id);
	}
	@Override
	public List<Message> findChatList(String userId) {
		return dao.queryChatList(userId);
	}
	@Override
	public List<Message> findAccountMsgs(String accountId) {
		String p1="[Create]:"+accountId+"%";
		String p2="[CreateInner]:"+accountId+"%";
		String p3="[Settle]:"+accountId+"%";
		return dao.queryAccountMsgs(p1,p2,p3);
	}
	@Override
	public void delete(String id) {
		dao.delete(id);
		dao.deleteState(id);
	}
	@Override
	public void updateStatusBatch(String userId,int state) {
		dao.updateStatusBatchForInvite(new HashMap<String,Object>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("userId",userId);
				put("state",state);
			}
		});
		
	}
	@Override
	public void updateStatusBatch(String user1Id, String user2Id,int state) {
		dao.updateStatusBatch(new HashMap<String,Object>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("user1Id",user1Id);
				put("user2Id",user2Id);
				put("state",state);
			}
		});
		
	}
	@Override
	public boolean isRepeatInvite(String user1Id, String user2Id) {
		return dao.isRepeatInvite(user1Id, user2Id);
	}

	

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args){
		insertMsgState();
	}
	
	
	
	private static void insertMsgState(){
		
        String sql;
        String sqlBaseUrl=readJdbcProperty("url");
        String user=readJdbcProperty("username");
		String password=readJdbcProperty("password");
        String url = sqlBaseUrl + "?user="+user+"&password="+password;
 
        try {
            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
            Connection conn = DriverManager.getConnection(url);
            System.out.println("成功加载MySQL驱动程序");
            Statement stmt = conn.createStatement();
            sql = "SELECT * FROM message;";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	String msgid=rs.getString("id");
            	String fromid=rs.getString("from_id");
            	String to=rs.getString("to_id");
            	int type=rs.getInt("type");
            	
            	Statement stmt2 = conn.createStatement();
            	System.out.println("insert into message_state values(null,"+msgid+","+fromid+","+"0)");
            	if(type==3)
            		stmt2.execute("insert into message_state values(null,'"+msgid+"','"+fromid+"',"+"0)");
            	stmt2.execute("insert into message_state values(null,'"+msgid+"','"+to+"',"+"0)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 读取jdbc.properties中的配置
	 * 
	 * @return
	 */
	public static String readJdbcProperty(String key) {
		Properties jdbcProps = new Properties();
		String value = null;
		try {
			// 配置文件位于当前目录中的config目录下
			InputStream resourceAsStream = TokenServiceImpl.class.getClassLoader().getResourceAsStream("jdbc.properties");
			jdbcProps.load(resourceAsStream);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		value = jdbcProps.getProperty(key);
		return value;
	}
	
	

}
