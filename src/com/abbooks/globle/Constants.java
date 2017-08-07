package com.abbooks.globle;

public class Constants {
	
	
	public static String APP_ID="wx9d9db1b5e01578f8";
	public static String APP_SECRET="0d29e97ba582d015afd603ff6346fb93";
	
	

	/**
	 * 用户上传的，或者其他方式获取的文件都存储在这个目录下
	 */
	public static String EXTERN_FILE_DIR;
	/**PATH_IMAGE
	 * 为了避免重新发布时将上传的文件删除,所以将上传的XLSX文件存入其他盘符
	 */
	public static final String PATH_XLSX="/xlsx/";
	
	public static final String REQUEST_BASE_URL="http://localhost:8080/ABBooksServer";
	/**
	 * 图片
	 */
	public static final String PATH_IMAGE_DOWNLOAD="/img/download/";
	public static final String PATH_IMAGE_UPLOAD="/img/upload/";
}
