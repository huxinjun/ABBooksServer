package com.accountbook.controller;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import com.accountbook.modle.result.Result;
import com.accountbook.utils.FileUtils;
import com.accountbook.utils.TextUtils;



@Controller
@RequestMapping("/image")
public class ImageController {
	
	

	@ResponseBody
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public Object upload(@RequestParam("image")CommonsMultipartFile file,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String findId=request.getAttribute("userid").toString();
		
		System.out.println("文件来了");
		String relativePath=FileUtils.saveFile(file.getInputStream(),findId);
		return new Result(Result.RESULT_OK,relativePath);
	}

	
	
	@ResponseBody
	@RequestMapping(value="/get/**",method=RequestMethod.GET)
	public void getImage(HttpServletRequest request,HttpServletResponse response){
		String urlPath=extractPathFromPattern(request);
		
		
		
		
		
//		public	所有内容都将被缓存(客户端和代理服务器都可缓存)
//		private	内容只缓存到私有缓存中(仅客户端可以缓存，代理服务器不可缓存)
//		no-cache	必须先与服务器确认返回的响应是否被更改，然后才能使用该响应来满足后续对同一个网址的请求。因此，如果存在合适的验证令牌 (ETag)，no-cache 会发起往返通信来验证缓存的响应，如果资源未被更改，可以避免下载。
//		no-store	所有内容都不会被缓存到缓存或 Internet 临时文件中
//		must-revalidation/proxy-revalidation	如果缓存的内容失效，请求必须发送到服务器/代理以进行重新验证
//		max-age=xxx (xxx is numeric)单位秒	缓存的内容将在 xxx 秒后失效, 这个选项只在HTTP 1.1可用, 并如果和Last-Modified一起使用时, 优先级较高
		
		response.addHeader("Cache-Control", "public, max-age="+60*60);
//		response.addHeader("Cache-Control", "no-cache");
		
		
		
		
		
		
		
		
		
		
		
		
		
		System.out.println(".........................img urlPath:"+urlPath);
		
		File file=new File(FileUtils.getImageAbsolutePath(urlPath));
		//让客户的使用缓存
		String modifySinceStr=request.getHeader("If-Modified-Since");
		if(TextUtils.isEmpty(modifySinceStr))
			//第一次请求，需要发送服务器的图片
			sendServerFile(response,file,urlPath);
		else{
			//判断文件是否过期
			long modifySince;
			try{
				modifySince=Long.parseLong(modifySinceStr);
			}catch(Exception ex){
				modifySince=0;
			}
			long serverFileLastModify=file.exists()?file.lastModified():0;
			if(serverFileLastModify>modifySince)
				sendServerFile(response,file,urlPath);
			else{
				//没有过期，客户端资源可用
				response.setStatus(304);
				response.addHeader("Last-Modified", String.valueOf(file.lastModified()));
				System.out.println(".........................304使用缓存");
				return;
			}
		}
	}
	
	/**
	 * 向客户端发送最新的图片资源
	 */
	private void sendServerFile(HttpServletResponse response,File file,String urlPath){
		response.setStatus(200);
		response.addHeader("Last-Modified", String.valueOf(file.lastModified()));
		try {
			FileUtils.send(FileUtils.getImageAbsolutePath(urlPath), response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/delete/**",method=RequestMethod.GET)
	public void deleteImage(HttpServletRequest request,HttpServletResponse response){
		String filepath=extractPathFromPattern(request);
		
		
		if(TextUtils.isEmpty(filepath))
			return;
		
		File file=new File(FileUtils.getImageAbsolutePath(filepath));
		if(file.exists())
			file.delete();
	}
	
	
	// 把指定URL后的字符串全部截断当成参数
	// 这么做是为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
	private String extractPathFromPattern(HttpServletRequest request)
	{
	     String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
	     String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
	     return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
	}
}
