package com.accountbook.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.model.AccountQueryRecord;
import com.accountbook.model.SummaryInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.IAccountService;
import com.accountbook.utils.TextUtils;

/**
 * 统计控制器
 * 
 * @author xinjun create 2017.9.25
 */
@Controller
@RequestMapping("/summary")
public class SummaryController {
	
	
	@SuppressWarnings("serial")
	private static final HashMap<String, String> TYPE_MAP=new HashMap<String, String>(){
		{
			put("cf","吃饭_#00F5FF");
			put("zf","租房_#43CD80");
			put("jt","交通_#43CD80");
			put("gw","购物_#8B864E");
			put("sh","生活_#8B658B");
			put("ls","零食_#CD5555");
			put("lx","旅行_#CD2626");
			put("yl","娱乐_#FF1493");
			put("qt","其他_#8B5F65");
			put("hk","还款_#5D478B");
			put("jk","借款_#9C9C9C");
			put("sr","收入_#32CD32");
		}
	};

	@Autowired
	IAccountService accountService;

	
	/**
	 * 统计一个用户的账本简要信息
	 */
	@ResponseBody
	@RequestMapping("/getSimple")
	public Object getSimple(ServletRequest req,String userId) {
		String findId=req.getAttribute("userid").toString();
		Result result=new Result();
		List<SummaryInfo> simpleInfo;
		if(TextUtils.isEmpty(userId))
			simpleInfo = accountService.getSummaryInfo(findId);
		else
			simpleInfo = accountService.getSummaryInfo(findId,userId);
		System.out.println(simpleInfo);
		return result.put(Result.RESULT_OK, "查询成功").put("infos",simpleInfo);
	}
	
	
	/**
	 * 统计当前用户今日记账的简要信息
	 */
	@ResponseBody
	@RequestMapping("/getToday")
	public Object getToday(ServletRequest req) {
		String findId=req.getAttribute("userid").toString();
		Result result=new Result();
		List<SummaryInfo> simpleInfo;
		simpleInfo = accountService.getSummaryInfoToday(findId);
		System.out.println("findId:"+findId+",result:"+simpleInfo);
		return result.put(Result.RESULT_OK, "查询成功").put("infos",simpleInfo);
	}
	
	/**
	 * 统计当前用户月度各种类型账单的支出金额
	 */
	@ResponseBody
	@RequestMapping("/getMonthPaid")
	public Object getMonthPaid(ServletRequest req,Integer year,Integer month) {
		String findId=req.getAttribute("userid").toString();
		Result result=new Result();
		List<SummaryInfo> infos = accountService.getSummaryInfoMonthPaid(findId,year,month);
		List<Result> results=new ArrayList<>();
		for(SummaryInfo info:infos){
			String name=TYPE_MAP.get(info.getName()).split("_")[0];
			String color=TYPE_MAP.get(info.getName()).split("_")[1];
			Result item=new Result();
			item.put("name", name);
			item.put("data",info.getNumber());
			item.put("color", color);
			item.put("count", info.getCount());
			results.add(item);
		}
		return result.put(Result.RESULT_OK, "查询成功").put("infos",results);
	}
	
	/**
	 * 统计当前用户月度其他类型账单的支出金额
	 */
	@ResponseBody
	@RequestMapping("/getMonthPaidForOther")
	public Object getMonthPaidForOther(ServletRequest req,Integer year,Integer month) {
		String findId=req.getAttribute("userid").toString();
		Result result=new Result();
		List<SummaryInfo> infos = accountService.getSummaryInfoMonthPaidForOther(findId,year,month);
		List<Result> results=new ArrayList<>();
		for(SummaryInfo info:infos){
			Result item=new Result();
			item.put("name", info.getName());
			item.put("data",info.getNumber());
			item.put("color",genRandomColorStr());
			item.put("count", info.getCount());
			results.add(item);
		}
		return result.put(Result.RESULT_OK, "查询成功").put("infos",results);
	}
	
	
	
	/**
	 * 统计当前用户年度和月度支出金额
	 */
	@ResponseBody
	@RequestMapping("/getYearMonthPaid")
	public Object getYearMonthPaid(ServletRequest req) {
		String findId=req.getAttribute("userid").toString();
		Result result=new Result();
		List<AccountQueryRecord> records = accountService.getAllPaidRecords(findId);
		
		Map<Integer,Map<Integer,Float>> maps=new HashMap<>();
		
		for(AccountQueryRecord record:records){
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(new Date(record.date.getTime()));
			
			int year=calendar.get(Calendar.YEAR);
			int month=calendar.get(Calendar.MONTH)+1;
			
			if(!maps.containsKey(year))
				maps.put(year, new HashMap<>());
			
			if(!maps.get(year).containsKey(month))
				maps.get(year).put(month, record.money);
			else
				maps.get(year).put(month, maps.get(year).get(month)+record.money);
		}
		return result.put(Result.RESULT_OK, "查询成功").put("map",maps);
	}
	
	
	/**
	 * 统计当前用户年度和月度收入金额
	 */
	@ResponseBody
	@RequestMapping("/getYearMonthReceipt")
	public Object getYearMonthReceipt(ServletRequest req) {
		String findId=req.getAttribute("userid").toString();
		Result result=new Result();
		List<AccountQueryRecord> records = accountService.getAllReceiptRecords(findId);
		
		Map<Integer,Map<Integer,Float>> maps=new HashMap<>();
		
		for(AccountQueryRecord record:records){
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(new Date(record.date.getTime()));
			
			int year=calendar.get(Calendar.YEAR);
			int month=calendar.get(Calendar.MONTH)+1;
			
			if(!maps.containsKey(year))
				maps.put(year, new HashMap<>());
			
			if(!maps.get(year).containsKey(month))
				maps.get(year).put(month, record.money);
			else
				maps.get(year).put(month, maps.get(year).get(month)+record.money);
		}
		return result.put(Result.RESULT_OK, "查询成功").put("map",maps);
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 生成一个随机颜色
	 * @return
	 */
	private static String genRandomColorStr(){
		String color="";
		int r=(int) (Math.random()*256);
		int g=(int) (Math.random()*256);
		int b=(int) (Math.random()*256);
		String rStr=Integer.toHexString(r);
		String gStr=Integer.toHexString(g);
		String bStr=Integer.toHexString(b);
		color="#"+(rStr.length()==1?"0"+rStr:rStr)+(gStr.length()==1?"0"+gStr:gStr)+(bStr.length()==1?"0"+bStr:bStr);
		
		return color;
	}
	
	public static void main(String[] args) {
		System.out.println(genRandomColorStr());
	}

}
