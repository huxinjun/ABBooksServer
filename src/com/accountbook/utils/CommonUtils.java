package com.accountbook.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.accountbook.model.Member;
import com.accountbook.model.UserInfo;

/**
 * 乱七八糟的工具类
 * @author xinjun
 *
 */
public class CommonUtils {
	
	
	
	public static String getSinceTimeString(Date date){
		long currTime=System.currentTimeMillis();
		long time=date.getTime();
		
		int minuteDis=getAllMinute(currTime)-getAllMinute(time);
		
		if(minuteDis==0)
			return "刚刚";
		if(minuteDis>0 && minuteDis<60)
			return minuteDis+"分钟前";
		if(minuteDis>=60 && minuteDis<60*24)
			return minuteDis/60+"小时前";
		if(minuteDis>=60*24 && minuteDis<60*24*7)
			return minuteDis/60/24+"天前";
		if(minuteDis>=60*24*7 && minuteDis<60*24*7*30)
			return minuteDis/60/24/7+"周前";
		if(minuteDis>=60*24*7*30 && minuteDis<60*24*7*30*12)
			return minuteDis/60/24/7/30+"月前";
		if(minuteDis>=60*24*7*30*12)
			return minuteDis/60/24/7/30/12+"年前";
		return "";
		
	}
	
	public static String getSinceTimeString2(Date date){
		long currTime=System.currentTimeMillis();
		long time=date.getTime();
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
		int minuteDis=getAllMinute(currTime)-getAllMinute(time);
		
		if(minuteDis==0)
			return "刚刚";
		if(minuteDis>0 && minuteDis<60)
			return minuteDis+"分钟前";
		if(minuteDis>=60 && minuteDis<60*24)
			return minuteDis/60+"小时前";
		if(minuteDis>=60*24 && minuteDis<60*24*7)
			return minuteDis/60/24+"天前";
		if(minuteDis>=60*24*7 && minuteDis<60*24*7*30)
			return minuteDis/60/24/7+"周前";
		if(minuteDis>=60*24*7*30)
			return format.format(time);
		return "";
		
	}
	
	public static int getAllMinute(long time){
		return (int) (time/1000/60);
	}
	
	
	
	public static final int PAGE_DEFAULT_SIZE=10;
	public static final int PAGE_DEFAULT_INDEX=0;
	
	public static class Limit{
		public int start;
		public int count;
	}
	/**
	 * 获取分页其实结束
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static Limit getLimit(Integer pageIndex, Integer pageSize){
		if(pageIndex==null)
			pageIndex=PAGE_DEFAULT_INDEX;
		if(pageSize==null)
			pageSize=PAGE_DEFAULT_SIZE;
		
		pageIndex=pageIndex<0?PAGE_DEFAULT_INDEX:pageIndex;
		pageSize=pageSize<=0?PAGE_DEFAULT_SIZE:pageSize;
		
		Limit limit=new Limit();
		limit.start=pageIndex*pageSize;
		limit.count=pageSize;
		
		return limit;
	}
	
	
	public static Member userToMember(UserInfo user){
		Member member=new Member();
		member.setMemberId(user.id);
		member.setMemberIcon(user.icon);
		member.setMemberName(user.nickname);
		return member;
	}
	
	
}
