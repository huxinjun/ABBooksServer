package com.accountbook.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.accountbook.globle.Constants;

/**
 * 存储上传的文件等等
 * @author xinjun
 *
 */
public class FileUtils {
	
	/**
	 * 获取文件在服务器上的路径
	 * @param filename 数据库中存储的文件名
	 * @return
	 */
	public static String getImageAbsolutePath(String relativePath){
		return Constants.EXTERN_FILE_DIR+Constants.PATH_IMAGE+relativePath;
	}
	
	/**
	 * 生成一个服务器相对文件路径
	 * @param dirNames 文件夹名称列表
	 * @return 相对路径
	 */
	public static String genarateFileRelativePath(String... dirNames){
		String fileName=MD5.getMD5(UUID.randomUUID().toString())+"XzBB";
		//服务器上的相对路径
		String relativePath;
		StringBuilder sb=new StringBuilder();
		if(dirNames==null || dirNames.length==0)
			relativePath=fileName;
		else{
			for(String dirName:dirNames)
				sb.append(dirName+"/");
			sb.append(fileName);
			relativePath=sb.toString();
		}
		File file=new File(getImageAbsolutePath(relativePath));
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		return relativePath;
	}
	

	public static String saveFile(InputStream is,String... dirNames){
		String relativePath=genarateFileRelativePath(dirNames);
		String serverPath=getImageAbsolutePath(relativePath);
		File serverFile = new File(serverPath);
		try {
			FileOutputStream fos=new FileOutputStream(serverFile);
			byte[] bytes=new byte[1024];
			while(-1!=is.read(bytes)){
				fos.write(bytes);
			}
			fos.close();
			is.close();
			System.out.println("存储文件:"+serverFile.getPath());
			return relativePath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
	
	public static String saveFile(byte[] bytes,String... dirNames){
		String relativePath=genarateFileRelativePath(dirNames);
		String serverPath=getImageAbsolutePath(relativePath);
		File serverFile = new File(serverPath);
		try {
			if(!serverFile.getParentFile().exists())
				serverFile.getParentFile().mkdirs();
			
			FileOutputStream fos=new FileOutputStream(serverFile);
			fos.write(bytes);
			fos.close();
			System.out.println("存储文件:"+serverFile.getPath());
			return relativePath;
		} catch (Exception e) {
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
    
    
    
    
    /**
	 * 把本地的资源输送到输出流中去,送给客户端
	 * @param localPath
	 * @param os
	 * @return
	 */
	public static boolean send(String localPath,OutputStream os) {
		System.out.println("FileUtils.sendServerFile:"+localPath);
		try {  
			File file=new File(localPath);
			if(!file.exists())
				localPath="D:\\accountbook\\img\\image.png";
			  
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
