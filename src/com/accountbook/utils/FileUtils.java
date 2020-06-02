package com.accountbook.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.accountbook.globle.Constants;

/**
 * 存储上传的文件等等
 * 
 * @author xinjun
 *
 */
public class FileUtils {

	/**
	 * 获取文件在服务器上的路径
	 * 
	 * @param filename
	 *            数据库中存储的文件名
	 * @return
	 */
	public static String getImageAbsolutePath(String relativePath) {
		return Constants.EXTERN_FILE_DIR + Constants.PATH_IMAGE + relativePath;
	}

	/**
	 * 获取对外资源url 可以通过ImageController下载该资源
	 * 
	 * @param request
	 *            当前request
	 * @param relativePath
	 *            genarateFileRelativePath方法生成的服务器相对路径
	 * @return
	 */
	public static String getImageUrl(HttpServletRequest request, String relativePath) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
				+ "/image/get/" + relativePath;
	}

	/**
	 * 生成一个服务器相对文件路径
	 * 
	 * @param dirNames
	 *            文件夹名称列表
	 * @return 相对路径
	 */
	public static String genarateFileRelativePath(String... dirNames) {
		return genarateFileRelativePathByName(null, dirNames);
	}

	public static String genarateFileRelativePathByName(String fileName, String... dirNames) {
		if (TextUtils.isEmpty(fileName))
			fileName = MD5.getMD5(UUID.randomUUID().toString()) + "XzBB";
		// 服务器上的相对路径
		String relativePath;
		StringBuilder sb = new StringBuilder();
		if (dirNames == null || dirNames.length == 0)
			relativePath = fileName;
		else {
			for (String dirName : dirNames)
				sb.append(dirName + "/");
			sb.append(fileName);
			relativePath = sb.toString();
		}
		File file = new File(getImageAbsolutePath(relativePath));
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		return relativePath;
	}

	public static String saveFile(InputStream is, String... dirNames) {
		return saveFileByName(is, null, dirNames);
	}

	public static String saveFileByName(InputStream is, String fileName, String... dirNames) {
		String relativePath = genarateFileRelativePathByName(fileName, dirNames);
		String serverPath = getImageAbsolutePath(relativePath);
		File serverFile = new File(serverPath);
		try {
			FileOutputStream fos = new FileOutputStream(serverFile);
			byte[] bytes = new byte[1024];
			int n;
			while ((n = is.read(bytes)) != -1) {
				fos.write(bytes, 0, n);
			}
			fos.close();
			is.close();
			// System.out.println("存储文件:"+serverFile.getAbsolutePath()+",读取文件大小:"+serverFile.length());
			return relativePath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String saveFile(byte[] bytes, String... dirNames) {
		String relativePath = genarateFileRelativePath(dirNames);
		String serverPath = getImageAbsolutePath(relativePath);
		File serverFile = new File(serverPath);
		try {
			if (!serverFile.getParentFile().exists())
				serverFile.getParentFile().mkdirs();

			FileOutputStream fos = new FileOutputStream(serverFile);
			fos.write(bytes);
			fos.close();
			System.out.println("存储文件:" + serverFile.getPath());
			return relativePath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String saveFile(InputStream is, File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] bytes = new byte[1024];
			int n;
			while ((n = is.read(bytes)) != -1) {
				fos.write(bytes, 0, n);
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String saveFile(byte[] bytes, File file) {
		try {

			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
			System.out.println("存储文件:" + file.getPath());
			return file.getPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从输入流获取数据
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inputStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		outputStream.close();
		inputStream.close();
		return outputStream.toByteArray();
	}

	/**
	 * 把本地的资源输送到输出流中去,送给客户端
	 * 
	 * @param localPath
	 * @param os
	 * @return
	 */
	public static boolean send(String localPath, OutputStream os) {
		System.out.println("FileUtils.sendServerFile:" + localPath);
		try {
			File file = new File(localPath);
			if (!file.exists())
				localPath = getDefaultImagePath();

			FileInputStream fis = new FileInputStream(localPath);
			byte[] buf = new byte[2048];
			while (-1 != fis.read(buf)) {
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

	public static String getDefaultImagePath() {
		return Constants.EXTERN_FILE_DIR + "\\img\\image.png";
	}
	
	
	
	public static String size2str(String sizeStr) {
		int size = 0;
		try {

			size = Integer.parseInt(sizeStr);
		} catch (Exception e) {

		}

		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

}
