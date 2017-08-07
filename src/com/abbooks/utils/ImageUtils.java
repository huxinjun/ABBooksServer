package com.abbooks.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 下载图片的工具类
 * @author XINJUN
 *
 */
public class ImageUtils {

	
	/**
	 * 下载别人的图片
	 * @param imgUrl
	 * @param localPath
	 * @return
	 */
	public static boolean download(String imgUrl,String localPath) {
		try {  
			  
			
            // 创建流  
            BufferedInputStream in = new BufferedInputStream(new URL(imgUrl)  
                    .openStream());  
   
            // 存放地址  
            File file = new File(localPath);
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
            return true;
        } catch (Exception e) {  
        	//java.io.FileNotFoundException: https://www.afp.com/sites/default/files/nfs/diff-intra/francais/journal/fra/84ba637fcbdf0cd9f63461177627d46e37b1b25a.jpg
        	//java.io.IOException: Server returned HTTP response code: 403 for URL: http://www.hurriyetdailynews.com/images/news/201705/n_112893_1.jpg
            e.printStackTrace();  
        }  
		return false;
	}
	
	/**
	 * 把本地的资源输送到输出流中去,送给客户端
	 * @param localPath
	 * @param os
	 * @return
	 */
	public static boolean send(String localPath,OutputStream os) {
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
}
