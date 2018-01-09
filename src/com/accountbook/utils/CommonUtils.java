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
	
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args){
		Date date = new Date();
		date.setHours(10);
		date.setMinutes(20);
		System.out.println(getSinceTimeString(date));
	}
	
	/**
	 * 
	 */
	public static String getSinceTimeString(Date date){
		long currTime=System.currentTimeMillis();
		long time=date.getTime();
		
		int yearDis=getAllYear(currTime)-getAllYear(time);
		int monthDis=getAllMonth(currTime)-getAllMonth(time);
		int weekDis=getAllWeek(currTime)-getAllWeek(time);
		int dayDis=getAllDay(currTime)-getAllDay(time);
		int hourDis=getAllHour(currTime)-getAllHour(time);
		int minuteDis=getAllMinute(currTime)-getAllMinute(time);
		
		System.out.println(yearDis+"年\n"+monthDis+"月\n"+weekDis+"周\n"+dayDis+"日\n"+hourDis+"时\n"+minuteDis+"分\n--------------");
		if(minuteDis==0)
			return "刚刚";
		if(minuteDis>0 && hourDis==0)
			return minuteDis+"分钟前";
		if(hourDis>0 && dayDis==0)
			return hourDis+"小时前";
		if(dayDis>0 && weekDis==0)
			return dayDis+"天前";
		if(weekDis>0 && monthDis==0)
			return weekDis+"周前";
		if(monthDis>0 && yearDis==0)
			return monthDis+"月前";
		if(yearDis>0)
			return yearDis+"年前";
		return "";
		
	}
	
	/**
	 * 
	 */
	public static String getSinceTimeString2(Date date){
		
		long currTime=System.currentTimeMillis();
		long time=date.getTime();
		
		int yearDis=getAllYear(currTime)-getAllYear(time);
		int monthDis=getAllMonth(currTime)-getAllMonth(time);
		int weekDis=getAllWeek(currTime)-getAllWeek(time);
		int dayDis=getAllDay(currTime)-getAllDay(time);
		int hourDis=getAllHour(currTime)-getAllHour(time);
		int minuteDis=getAllMinute(currTime)-getAllMinute(time);
		
		
		System.out.println(yearDis+"年\n"+monthDis+"月\n"+weekDis+"周\n"+dayDis+"日\n"+hourDis+"时\n"+minuteDis+"分\n--------------");
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
		if(minuteDis==0)
			return "刚刚";
		if(minuteDis>0 && hourDis==0)
			return minuteDis+"分钟前";
		if(hourDis>0 && dayDis==0)
			return hourDis+"小时前";
		if(dayDis>0 && weekDis==0)
			return dayDis+"天前";
		if(weekDis>0 && monthDis==0)
			return weekDis+"周前";
		if(monthDis>0)
			return format.format(time);
		return "";
		
	}
	
	public static int getAllYear(long time){
		return getAllMonth(time)/12;
	}
	
	public static int getAllMonth(long time){
//		Calendar c=Calendar.getInstance();
//		c.setTime(new Date(time));
//		int totalMonth=c.get(Calendar.YEAR)*12+c.get(Calendar.MONTH);
//		return totalMonth;
		return getAllDay(time)/30;
	}
	
	public static int getAllWeek(long time){
		return getAllDay(time)/7;
	}
	
	public static int getAllDay(long time){
		return getAllHour(time)/24;
	}
	
	public static int getAllHour(long time){
		return getAllMinute(time)/60;
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
