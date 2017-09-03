package com.accountbook.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 存储上传的文件等等
 * @author xinjun
 *
 */
public class FileUtils {

	public static File saveUploadFile(String dir,CommonsMultipartFile file){
    	InputStream is;
		try {
			is = file.getInputStream();
			String fileName=UUID.randomUUID().toString();//.split("\\.")[0]
			String savePath=dir+""+fileName;
			File serverFile = save(is,savePath);
			System.out.println("存储文件:"+serverFile.getPath());
			return serverFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }
    
    /**
     * 从输入流获取数据
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inputStream) throws Exception{
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        inputStream.close();
        return outputStream.toByteArray();
    }
    
    
    
    private static File save(InputStream is, String path) {
		try {
			File file=new File(path);
			if(!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			FileOutputStream fos=new FileOutputStream(file);
			byte[] bytes=new byte[1024];
			while(-1!=is.read(bytes)){
				fos.write(bytes);
			}
			fos.close();
			is.close();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
