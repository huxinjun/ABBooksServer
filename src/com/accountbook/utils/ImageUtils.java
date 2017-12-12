package com.accountbook.utils;

import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;


/**
 * 下载图片的工具类
 * @author XINJUN
 *
 */
public class ImageUtils {

	public static void main(String args[]){
//		String download = download("http://h.hiphotos.baidu.com/image/pic/item/7a899e510fb30f24ada23710c295d143ad4b03ed.jpg");
		String download = download("https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eqicvuNcjOEqE68x3yVt8JpI9wuMSw3ib0LIgs7ZPqpz9TzWKS139bftAYTWWM8J0FhVyd4eqcgIdiag/0");
//		String download = download("https://img.alicdn.com/tfs/TB1TK8_h_nI8KJjy0FfXXcdoVXa-750-291.jpg_Q90.jpg");
		System.out.println(download);
	}

	
	
	/**
	 * 下载别人的图片
	 * @param imgUrl
	 * @param localPath
	 * @return
	 */
	public static String download(String imgUrl,String... dirNames) {
		try {  
			  
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化 
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");    
            sslContext.init(null, null, new java.security.SecureRandom());    
              
            // 从上述SSLContext对象中得到SSLSocketFactory对象     
            SSLSocketFactory ssf = sslContext.getSocketFactory(); 
            // 创建流  
			
            URLConnection connection=new URL(imgUrl).openConnection();
            if(connection instanceof HttpsURLConnection){
            	HttpsURLConnection conn=(HttpsURLConnection) connection;
            	conn.setSSLSocketFactory(ssf);    
            	conn.setDoOutput(true);    
            	conn.setDoInput(true);    
            	conn.setUseCaches(false); 
            }
//            System.out.println("服务器告知文件大小:"+connection.getContentLength());
            String relativePath= FileUtils.saveFile(connection.getInputStream(),dirNames);
			
            return relativePath;
        } catch (Exception e) {  
        	//java.io.FileNotFoundException: https://www.afp.com/sites/default/files/nfs/diff-intra/francais/journal/fra/84ba637fcbdf0cd9f63461177627d46e37b1b25a.jpg
        	//java.io.IOException: Server returned HTTP response code: 403 for URL: http://www.hurriyetdailynews.com/images/news/201705/n_112893_1.jpg
            e.printStackTrace();  
        }  
		return null;
	}

}
