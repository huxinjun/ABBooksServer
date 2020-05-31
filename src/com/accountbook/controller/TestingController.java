package com.accountbook.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.accountbook.model.TestingInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.ITestingService;
import com.accountbook.utils.FileUtils;
import com.accountbook.utils.QrUtil;

@Controller
@RequestMapping("/testing")
public class TestingController {

	@Autowired
	ITestingService testingService;

	@ResponseBody
	@RequestMapping(value = "/qrcode")
	public Object qrcode(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		
		int maxId = saveRecord(request);
		
		
		@SuppressWarnings("deprecation")
		String logoPath = request.getRealPath("/WEB-INF/static/images/app_icon.png");
		System.out.println("logoPath=" + logoPath);

		QrUtil.generateQRCodeImage(getQrLinkUrl(request, response, maxId), 500, 500, response.getOutputStream(),
				logoPath);
		return null;

	}

	@ResponseBody
	@RequestMapping(value = "/qrcode_b64")
	public Object qrcodeBase64(HttpServletRequest request, HttpServletResponse response)
			throws IOException, JSONException {

		int maxId = saveRecord(request);

		@SuppressWarnings("deprecation")
		String logoPath = request.getRealPath("/WEB-INF/static/images/app_icon.png");
		System.out.println("logoPath=" + logoPath);

		String relativePath = FileUtils.genarateFileRelativePathByName("temp", "testing");
		String serverPath = FileUtils.getImageAbsolutePath(relativePath);
		File serverFile = new File(serverPath);

		FileOutputStream foo = new FileOutputStream(serverFile);

		response.setContentType("image/png");
		QrUtil.generateQRCodeImage(getQrLinkUrl(request, response, maxId), 500, 500, foo, logoPath);

		byte[] bytes = new byte[(int) serverFile.length()];

		FileInputStream fis = new FileInputStream(serverFile);
		fis.read(bytes);
		fis.close();

		String encode = Base64.getEncoder().encodeToString(bytes);
		return new Result(Result.RESULT_OK, "").put("data", encode);
	}

	
	/**
	 * 存储记录到数据库
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private int saveRecord(HttpServletRequest request) throws UnsupportedEncodingException {
		String comment = request.getParameter("comment");
		String device = request.getParameter("device");
		String fileurl = request.getParameter("fileurl");

		String encodeFileUrl = URLEncoder.encode(fileurl, "utf-8");

		System.out.println("param comment=" + comment);
		System.out.println("param device=" + device);
		System.out.println("param fileurl=" + encodeFileUrl);

		int maxId = 0;
		TestingInfo findTestingInfoByFileUrl = testingService.findTestingInfoByFileUrl(fileurl);
		if (findTestingInfoByFileUrl == null) {
			// 没有此资源记录
			TestingInfo testInfo = new TestingInfo();
			testInfo.fileurl = fileurl;
			testInfo.device = device;
			testInfo.comments = comment;
			testInfo.timestamp = new Timestamp(System.currentTimeMillis());
			// 存到数据库
			testingService.newTestingInfo(testInfo);
			maxId = testingService.findMaxId();
		} else {
			maxId = findTestingInfoByFileUrl.id;

			System.out.println("testing.qrcode repeat,old id=" + maxId);
		}
		
		return maxId;
	}

	/**
	 * 二维码点击跳转的地址
	 * @param request
	 * @param response
	 * @param maxId
	 * @return
	 */
	private String getQrLinkUrl(HttpServletRequest request, HttpServletResponse response, int maxId) {
		// 本地测试详情网页地址
		String testDetailUrl = "http://192.168.1.6" + ":" + request.getServerPort() + request.getContextPath()
				+ "/testing/detail.html?id=" + maxId;
		// 线上详情网页的地址
		String releaseDetailUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/testing/detail.html?id=" + maxId;

		System.out.println("testDetailUrl=" + testDetailUrl);
		System.out.println("releaseDetailUrl=" + releaseDetailUrl);
		return releaseDetailUrl;
	}

	@ResponseBody
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public Object info(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");

		TestingInfo findTestingInfo = testingService.findTestingInfo(id);
		if (findTestingInfo == null)
			return new Result(Result.RESULT_FAILD, "查询失败!");

		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
		findTestingInfo.uploadDate = format.format(findTestingInfo.timestamp.getTime());

		return new Result(Result.RESULT_OK, "成功").put("data", findTestingInfo);
	}

	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public Object upload(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println("上传测试包文件来了:" + file.getOriginalFilename());

		String relativePath = FileUtils.saveFileByName(file.getInputStream(), file.getOriginalFilename(), "testing");
		return new Result(Result.RESULT_OK, "上传成功").put("fileurl", FileUtils.getImageUrl(request, relativePath));
	}

}
