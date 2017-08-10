package com.accountbook.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class MD5 {

	/**
	 * 利用MD5进行加密
	 * 
	 * @param str
	 *            待加密的字符串
	 * @return 加密后的字符串
	 * @throws NoSuchAlgorithmException
	 *             没有这种产生消息摘要的算法
	 * @throws UnsupportedEncodingException
	 */
	public static String getMD5(String str){
		// 确定计算方法
		MessageDigest md5;
		String newstr="";
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte[] digest = md5.digest(str.getBytes());
			BASE64Encoder base64en = new BASE64Encoder();
			// 加密后的字符串
			newstr = base64en.encode(digest).replace("/", "_");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newstr;
	}

	/**
	 * 
	 * 对文件全文生成MD5摘要
	 *
	 * 
	 * 
	 * @param file
	 * 
	 *            要加密的文件
	 * 
	 * @return MD5摘要码
	 * 
	 */

	static char hexdigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',

			'9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String getMD5(File file) {

		FileInputStream fis = null;

		try {

			MessageDigest md = MessageDigest.getInstance("MD5");

			fis = new FileInputStream(file);

			byte[] buffer = new byte[2048];

			int length = -1;

			long s = System.currentTimeMillis();

			while ((length = fis.read(buffer)) != -1) {

				md.update(buffer, 0, length);

			}

			byte[] digest = md.digest();
			BASE64Encoder base64en = new BASE64Encoder();
			// 加密后的字符串
			String newstr = base64en.encode(digest);
			return newstr.replace("/", "_");

		} catch (Exception ex) {

			ex.printStackTrace();

			return null;

		} finally {

			try {

				fis.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}

}
