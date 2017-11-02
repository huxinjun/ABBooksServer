package com.accountbook.utils;

import java.util.Calendar;
import java.util.Date;

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
	public static String getSinceTimeString(Date time){
		Calendar currCalendar = Calendar.getInstance();
		currCalendar.setTime(new Date());
		
		Calendar otherCalendar = Calendar.getInstance();
		otherCalendar.setTime(time);
		
		
		int yearDis=currCalendar.get(Calendar.YEAR)-otherCalendar.get(Calendar.YEAR);
		int monthDis=getAllMonth(currCalendar)-getAllMonth(otherCalendar);
		int weekDis=getAllWeek(currCalendar)-getAllWeek(otherCalendar);
		int dayDis=getAllDay(currCalendar)-getAllDay(otherCalendar);
		int hourDis=getAllHour(currCalendar)-getAllHour(otherCalendar);
		int minuteDis=getAllMinute(currCalendar)-getAllMinute(otherCalendar);
		
//		System.out.println(yearDis+"年"+monthDis+"月"+weekDis+"周"+dayDis+"日"+hourDis+"时"+minuteDis+"分");
		if(yearDis>0)
			return yearDis+"年前";
		if(monthDis>0)
			return monthDis+"月前";
		if(weekDis>0)
			return weekDis+"周前";
		if(dayDis>0)
			return dayDis+"天前";
		if(hourDis>0)
			return hourDis+"小时前";
		if(minuteDis>0)
			return minuteDis+"分钟前";
		return "刚刚";
		
	}
	
	public static int getAllMonth(Calendar c){
		int year=c.get(Calendar.YEAR);
		int month=c.get(Calendar.MONTH);
		return year*12+month;
	}
	
	public static int getAllWeek(Calendar c){
		int year=c.get(Calendar.YEAR);
		int weekOfYear=c.get(Calendar.WEEK_OF_YEAR);
		return year*12+weekOfYear;
	}
	
	public static int getAllDay(Calendar c){
		int year=c.get(Calendar.YEAR);
		int dayOfYear=c.get(Calendar.DAY_OF_YEAR);
		return year*12+dayOfYear;
	}
	
	public static int getAllHour(Calendar c){
		int hour=c.get(Calendar.HOUR_OF_DAY);
		return getAllDay(c)*24+hour;
	}
	
	public static int getAllMinute(Calendar c){
		int minute=c.get(Calendar.MINUTE);
		return getAllHour(c)*60+minute;
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
	
	
	
}
