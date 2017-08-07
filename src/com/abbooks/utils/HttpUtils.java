package com.abbooks.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HTTP工具
 * @author xinjun
 *
 */
public class HttpUtils {
	
	private static final String ENCODEING="utf-8";
	
	
    /**
     * 发一个http请求
     * @param urlStr
     * @param map
     * @param encoding
     * @return
     */
    public static String send(String urlStr, Map<String,String> map){  
        String body="";  
        StringBuffer sbuf = new StringBuffer();  
        if(map!=null){  
            for (Entry<String, String> entry : map.entrySet()) {  
                sbuf.append(entry.getKey()).append("=").append(entry.getValue()).append("&");  
            }  
            if(sbuf.length()>0){  
                sbuf.deleteCharAt(sbuf.length()-1);  
            }  
        }  
         // 1、重新对请求报文进行 GBK 编码  
        byte[] postData = null;  
        try {  
            postData = sbuf.toString().getBytes(ENCODEING);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
  
        // 2、发送 HTTP(S) 请求  
        OutputStream reqStream = null;  
        InputStream resStream = null;  
        URLConnection request = null;  
        try {  
            System.out.println("请求地址：" + urlStr);  
            System.out.println("参数：" + sbuf.toString());  
              
            //A、与服务器建立 HTTP(S) 连接  
            URL url = null;  
            try {  
//                Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1", 8087));  
                url = new URL(urlStr);  
                request = url.openConnection();  
                request.setDoInput(true);  
                request.setDoOutput(true);  
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
              
            //B、指定报文头【Content-type】、【Content-length】 与 【Keep-alive】  
            request.setRequestProperty("Content-type", "application/x-www-form-urlencoded");  
            request.setRequestProperty("Content-length", String.valueOf(postData.length));  
            request.setRequestProperty("Keep-alive", "false");  
            request.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
              
            //C、发送报文至服务器  
            reqStream = request.getOutputStream();  
            reqStream.write(postData);  
            reqStream.close();  
              
            //D、接收服务器返回结果  
            ByteArrayOutputStream ms = null;  
            resStream = request.getInputStream();  
            ms = new ByteArrayOutputStream();  
            byte[] buf = new byte[4096];  
            int count;  
            while ((count = resStream.read(buf, 0, buf.length)) > 0) {  
                ms.write(buf, 0, count);  
            }  
            resStream.close();  
            body = new String(ms.toByteArray(), ENCODEING); 
            
        } catch (UnknownHostException e) {  
            System.err.println( "服务器不可达【" + e.getMessage() + "】");  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (reqStream != null)  
                    reqStream.close();  
                if (resStream != null)  
                    resStream.close();  
            } catch (Exception ex) {  
            }  
        }  
  
        System.out.println("响应结果：");  
        System.out.println(body+"\n\n");  
        return body;  
    }


}
