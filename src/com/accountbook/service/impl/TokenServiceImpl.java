package com.accountbook.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.TokenDao;
import com.accountbook.modle.TokenInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.ITokenService;
import com.accountbook.utils.IDUtil;

@Service
public class TokenServiceImpl implements ITokenService {
	
	private static final int EXPIRE_TIME_INTERVAL=1000*60*60*24*7;//7天过期
	
	@Autowired
	public TokenDao dao;
	
	
	@Override
	public Result validate(String token) {
		Result result = new Result();
		String id = getId(token);
		System.out.println("UserController(根据token["+token+"]查找的openid)："+id);
		if(id==null){
			result.status=1;
			result.msg="token无效";
			return result;
		}
		result.status=0;
		result.msg=id;
		return result;
	}
	
	

	@Override
	public String getId(String token) {
		TokenInfo tokenInfo = jdbcQuery(token);
		
		if(tokenInfo==null)
			return null;
		if(tokenInfo.expireTime!=-1)
			if(System.currentTimeMillis()>tokenInfo.expireTime)
				return null;
		return tokenInfo.id;
	}
	
	/**
	 * 使用原始方式查询token,因为在拦截器中处理的
	 * @param token
	 * @return
	 */
	private Connection conn = null;
	private TokenInfo jdbcQuery(String token){
		TokenInfo result = null;
		
        String sql;
        // MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值
        // 避免中文乱码要指定useUnicode和characterEncoding
        // 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，
        // 下面语句之前就要先创建javademo数据库
        String url = "jdbc:mysql://118.184.85.209:8888/accountbook?"
                + "user=root&password=root&useUnicode=true&characterEncoding=UTF8";
 
        try {
            // 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，
            // 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以
            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
            // or:
            // com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
            // or：
            // new com.mysql.jdbc.Driver();
 
            
            // 一个Connection代表一个数据库连接
            if(conn==null){
            	conn = DriverManager.getConnection(url);
            	System.out.println("成功加载MySQL驱动程序");
            }
            // Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等
            Statement stmt = conn.createStatement();
            sql = "SELECT * FROM token WHERE token='"+token+"';";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	result=new TokenInfo();
            	result.id=rs.getString("id");
            	result.token=rs.getString("token");
            	result.expireTime=rs.getLong("expire_time");
            }
        } catch (Exception e) {
            e.printStackTrace();
            conn=null;
            return jdbcQuery(token);
        }
        return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public String updateToken(String id, String token,boolean isExpire) {
		TokenInfo queryTokenById = dao.queryTokenById(id);
		if(queryTokenById==null){
			queryTokenById=new TokenInfo();
			queryTokenById.id=id;
			queryTokenById.token=token;
			if(isExpire)
				queryTokenById.expireTime=System.currentTimeMillis()+EXPIRE_TIME_INTERVAL;
			else
				queryTokenById.expireTime=-1;
			dao.insert(queryTokenById);
		}else{
			queryTokenById.id=id;
			queryTokenById.token=token;
			if(isExpire)
				queryTokenById.expireTime=System.currentTimeMillis()+EXPIRE_TIME_INTERVAL;
			else
				queryTokenById.expireTime=-1;
			System.out.println("queryTokenById:"+queryTokenById);
			dao.update(queryTokenById);
		}
		return null;
	}



	@Override
	public String generateToken() {
		return IDUtil.generateNewId();
	}
}
