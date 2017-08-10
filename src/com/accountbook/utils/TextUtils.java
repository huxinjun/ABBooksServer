package com.accountbook.utils;

/**
 * 提供字符检查转换等等
 * @author xinjun
 *
 */
public class TextUtils {

	
	public static boolean isEmpty(String str){
		if(str==null || str.equals("") ||str.trim().equals(""))
			return true;
		return false;
	}
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
}
