package com.accountbook.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.modle.TestModel;
import com.accountbook.modle.result.TestAllResult;
import com.accountbook.service.ITestService;


@Controller
@RequestMapping("/test")
	
public class TestController {
	

	
	
	@Autowired
	public  ITestService mTestService;
	
	
    
    @ResponseBody
    @RequestMapping("/all")
    public TestAllResult getSimpleMapNeedInfo(HttpServletRequest request,HttpServletResponse response){
//    	response.addHeader("Access-Control-Allow-Origin", "*");
    	
    	TestAllResult result=new TestAllResult();
    	result.status=1;

    	List<TestModel> all = mTestService.getAll();
    	result.results=all;
    	
    	System.out.println(all.toString());
    	
		return result;
    	
    }
}
