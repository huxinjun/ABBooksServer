package com.accountbook.utils;

import java.util.UUID;

/**
 * 生成一个不重复的新ID
 * @author Administrator
 *
 */
public class IDUtil {

	
	public static String generateNewId() {
		UUID uuid = UUID.randomUUID();
    	return MD5.getMD5(uuid.toString());
    }
}
