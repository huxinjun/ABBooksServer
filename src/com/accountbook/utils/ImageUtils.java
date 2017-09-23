package com.accountbook.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import com.accountbook.globle.Constants;


/**
 * 下载图片的工具类
 * @author XINJUN
 *
 */
public class ImageUtils {

	public static void main(String args[]){
//		String download = download("http://cdn.atool.org/res/right.png");
		String download = download("https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eqicvuNcjOEqE68x3yVt8JpI9wuMSw3ib0LIgs7ZPqpz9TzWKS139bftAYTWWM8J0FhVyd4eqcgIdiag/0");
		System.out.println(download);
	}
	
	/**
	 * 获取图像在服务器上的路径
	 * @param filename 数据库中存储的文件名
	 * @return
	 */
	public static String getImagePath(String filename){
		return Constants.EXTERN_FILE_DIR+Constants.PATH_IMAGE_UPLOAD+(TextUtils.isEmpty(filename)?"":filename);
	}
	
	
	/**
	 * 下载别人的图片
	 * @param imgUrl
	 * @param localPath
	 * @return
	 */
	public static String download(String imgUrl) {
		String filename=UUID.randomUUID().toString();
		String filePath=ImageUtils.getImagePath(filename);
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
            
            
			
            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());  
   
            // 存放地址  
            File file = new File(filePath);
            if(!file.getParentFile().exists())
				file.getParentFile().mkdirs();
            // 生成图片  
            BufferedOutputStream out = new BufferedOutputStream(  
                    new FileOutputStream(file));  
            byte[] buf = new byte[2048];  
            int length = in.read(buf);  
            while (length != -1) {  
                out.write(buf, 0, length);  
                length = in.read(buf);  
            }  
            in.close();  
            out.close();  
            return filename;
        } catch (Exception e) {  
        	//java.io.FileNotFoundException: https://www.afp.com/sites/default/files/nfs/diff-intra/francais/journal/fra/84ba637fcbdf0cd9f63461177627d46e37b1b25a.jpg
        	//java.io.IOException: Server returned HTTP response code: 403 for URL: http://www.hurriyetdailynews.com/images/news/201705/n_112893_1.jpg
            e.printStackTrace();  
        }  
		return null;
	}
	
	/**
	 * 把本地的资源输送到输出流中去,送给客户端
	 * @param localPath
	 * @param os
	 * @return
	 */
	public static boolean send(String localPath,OutputStream os) {
		System.out.println("ImageUtils.sendServerFile:"+localPath);
		try {  
			  
			FileInputStream fis=new FileInputStream(localPath);
			byte[] buf = new byte[2048]; 
			while(-1!=fis.read(buf)){
				os.write(buf);
			}
			fis.close();
			os.flush();
			os.close();
            return true;
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		return false;
	}
	/**
	 * 把byte输送到输出流中去,送给客户端
	 * @param localPath
	 * @param os
	 * @return
	 */
	public static boolean send(byte[] bytes,OutputStream os) {
		try {  
			os.write(bytes);
			os.flush();
			os.close();
			return true;
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return false;
	}
	
	
	public static void saveBytesToTestFile(byte[] rawData){
		try {
			ImageUtils.send(rawData, new FileOutputStream("C:\\Users\\Administrator\\Desktop\\test"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
}
