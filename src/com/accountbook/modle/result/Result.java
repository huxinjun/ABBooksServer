package com.accountbook.modle.result;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.accountbook.model.Message;

/**
 * 通用的结果对象
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class Result extends HashMap<String,Object>{
	
	public static final int RESULT_NOT_INIT=-1;
	public static final int RESULT_OK=0;
	public static final int RESULT_TOKEN_INVALID=1;
	public static final int RESULT_COMMAND_INVALID=2;
	public static final int RESULT_USERINFO_INVALID=3;
	public static final int RESULT_FILE_SAVE_ERROR=4;
	
	
	public static final int RESULT_FAILD=99;
	
//	{
//		put("status",RESULT_NOT_INIT);
//		put("msg","");
//	}
	
	public Result(){
	}
	
	public Result(int status,String msg){
		super.put("status",status);
		super.put("msg",msg);
	}
	
	
	public Result put(int status,String msg){
		super.put("status",status);
		super.put("msg",msg);
		return this;
	}
	
	public Result put(String key,Object value){
		super.put(key, value);
		return this;
	}
	
	/**
	 * 这种会把对象的属性拿出来装在map中
	 */
	public Result put(Object value){
		if(value==null)
			return this;
		
		Class<? extends Object> clazz = value.getClass();
		Field[] declaredFields = clazz.getDeclaredFields();
		if(declaredFields!=null){
			
			for(Field field:declaredFields){
				field.setAccessible(true);
				boolean isStatic = Modifier.isStatic(field.getModifiers());
				try {
					if(!isStatic){
						Object v=field.get(value);
						if(v!=null)
							super.put(field.getName(),v);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		return this;
	}
	
	/**
	 * 将对象列表转换为Result列表,目的为了去除空属性,而且可以动态删除或添加属性
	 * @param list
	 * @return
	 */
	public static List<Result> convert(List<? extends Object> list){
		if(list==null)
			return null;
		List<Result> results=new ArrayList<>();
		for(Object o:list)
			results.add(new Result().put(o));
		return results;
	}
	
	
	public static void main(String[] args){
		Result r=new Result(1,"");
		
		
		r.put(new Message());
		
		System.out.println(r);
	}

}
