package com.accountbook.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.accountbook.modle.result.Result;
import com.accountbook.utils.FileUtils;
import com.accountbook.utils.ImageUtils;



@Controller
@RequestMapping("/image")
public class ImageController {
	
	

	@ResponseBody
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public Object upload(@RequestParam("image")CommonsMultipartFile file,HttpServletRequest request,HttpServletResponse response) throws IOException{
		System.out.println("文件来了");
		File serverFile=FileUtils.saveUploadFile(ImageUtils.getImagePath(null),file);
		return new Result(Result.RESULT_OK,serverFile.getName());
	}

	
	
	@ResponseBody
	@RequestMapping(value="/get/{filename}",method=RequestMethod.GET)
	public void getImage(@PathVariable String filename,HttpServletRequest request,HttpServletResponse response){
		if(filename==null || "".equals(filename) || "null".equals(filename))
			return;
		try {
			ImageUtils.send(ImageUtils.getImagePath(filename), response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/delete/{filename}",method=RequestMethod.GET)
	public void deleteImage(@PathVariable String filename,HttpServletResponse response){
		File file=new File(ImageUtils.getImagePath(filename));
		if(file.exists())
			file.delete();
	}
	
}
