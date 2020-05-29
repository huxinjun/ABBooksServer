package com.accountbook.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.model.TestingInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.ITestingService;
import com.accountbook.utils.QrUtil;

@Controller
@RequestMapping("/testing")
public class TestingController {

	@Autowired
	ITestingService testingService;

	@ResponseBody
	@RequestMapping(value = "/qrcode", method = RequestMethod.GET)
	public Object qrcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String comment = request.getParameter("comment");
		String device = request.getParameter("device");
		String fileurl = request.getParameter("fileurl");

		TestingInfo testInfo = new TestingInfo();
		testInfo.fileurl = fileurl;
		testInfo.device = device;
		testInfo.comments = comment;
		testInfo.timestamp = System.currentTimeMillis();
		testingService.newTestingInfo(testInfo);

		int findMaxId = testingService.findMaxId();

		// 存到数据库

		String text = "https://www.xzbenben.cn/AccountBook/testing.html?id=" + findMaxId;
		
		@SuppressWarnings("deprecation")
		String logoPath = request.getRealPath("/WEB-INF/static/images/app_icon.png");
		System.out.println("logoPath="+logoPath);
		
		QrUtil.generateQRCodeImage(text, 500, 500, response.getOutputStream(),logoPath);
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public Object info(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");

		TestingInfo findTestingInfo = testingService.findTestingInfo(id);
		if (findTestingInfo == null)
			return new Result(Result.RESULT_FAILD, "查询失败!");

		return new Result(Result.RESULT_OK, "成功").put("data",findTestingInfo);
	}

}
