package com.accountbook.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

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

	
	
	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping(value="/get/**",method=RequestMethod.GET)
	public void getImage(HttpServletRequest request,HttpServletResponse response){
		String filepath=extractPathFromPattern(request);
		System.out.println("........................."+filepath);
		
		if(TextUtils.isEmpty(filepath))
			return;
		try {
			//让客户的使用缓存
			if(TextUtils.isNotEmpty(request.getHeader("If-Modified-Since")))
				response.setStatus(304);
			else
				response.setStatus(200);
			response.addHeader("Cache-Control", "public, max-age=31536000");
			response.addHeader("Last-Modified", new Date(new File(FileUtils.getImageAbsolutePath(filepath)).lastModified()).toGMTString());
			FileUtils.send(FileUtils.getImageAbsolutePath(filepath), response.getOutputStream());
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
