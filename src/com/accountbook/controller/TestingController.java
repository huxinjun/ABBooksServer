package com.accountbook.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.accountbook.model.TestingInfo;
import com.accountbook.model.TestingAppInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.ITestingService;
import com.accountbook.utils.CommonUtils;
import com.accountbook.utils.FileUtils;
import com.accountbook.utils.QrUtil;
import com.accountbook.utils.TextUtils;

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

		QrUtil.generateQRCodeImage(getQrLinkUrl(request, maxId), 500, 500, response.getOutputStream(), logoPath);
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
		QrUtil.generateQRCodeImage(getQrLinkUrl(request, maxId), 500, 500, foo, logoPath);

		byte[] bytes = new byte[(int) serverFile.length()];

		FileInputStream fis = new FileInputStream(serverFile);
		fis.read(bytes);
		fis.close();

		String encode = Base64.getEncoder().encodeToString(bytes);
		return new Result(Result.RESULT_OK, "").put("data", encode);
	}

	/**
	 * 存储记录到数据库
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private int saveRecord(HttpServletRequest request) throws UnsupportedEncodingException {
		String comment = request.getParameter("comment");
		String device = request.getParameter("device");
		String fileurl = request.getParameter("fileurl");
		String appinfo = request.getParameter("appinfo");

		String encodeFileUrl = URLEncoder.encode(fileurl, "utf-8");

		System.out.println("param comment=" + comment);
		System.out.println("param device=" + device);
		System.out.println("param fileurl=" + encodeFileUrl);
		System.out.println("param appInfo=" + appinfo);

		int maxId = 0;
		TestingInfo findTestingInfoByFileUrl = testingService.findTestingInfoByFileUrl(fileurl);
		if (findTestingInfoByFileUrl == null) {
			// 没有此资源记录
			TestingInfo testInfo = new TestingInfo();
			testInfo.fileurl = fileurl;
			testInfo.device = device;
			testInfo.comments = comment;
			testInfo.appinfo = appinfo;
			testInfo.packageName = getPackageName(device, appinfo);
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
	 */
	private String getQrLinkUrl(HttpServletRequest request, int id) {

		String protocol = request.getServerPort() == 443 ? "https://" : "http://";

		// 本地测试详情网页地址
		String testDetailUrl = protocol + "192.168.1.6" + ":" + request.getServerPort() + request.getContextPath()
				+ "/testing/detail.html?id=" + id;
		// 线上详情网页的地址
		String releaseDetailUrl = protocol + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/testing/detail.html?id=" + id;

		System.out.println("testDetailUrl=" + testDetailUrl);
		System.out.println("releaseDetailUrl=" + releaseDetailUrl);
		return releaseDetailUrl;
	}

	@ResponseBody
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public Object info(HttpServletRequest request, Integer pageIndex, Integer pageSize) throws IOException {
		String id = request.getParameter("id");

		TestingInfo findTestingInfo = testingService.findTestingInfo(id);
		if (findTestingInfo == null)
			return new Result(Result.RESULT_FAILD, "查询失败!");

		return assembleInfoResult(request, findTestingInfo, pageIndex, pageSize);
	}

	private String getPackageName(String device, String appinfo) {

		String result = "other";
		if (TextUtils.isEmpty(appinfo))
			return result;
		if (TextUtils.isEmpty(device))
			return result;
		try {

			JSONObject infoObj = new JSONObject(appinfo);
			switch (device.toLowerCase()) {
			case "android":
				result = infoObj.optString("package");
				break;
			case "ios":
				result = infoObj.optString("CFBundleIdentifier");
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	private Result assembleInfoResult(HttpServletRequest request, TestingInfo findTestingInfo, Integer pageIndex,
			Integer pageSize) {
		Result result = new Result();

		try {

			result.put("comment", findTestingInfo.comments);

			TestingAppInfo parseAppInfo = parseAppInfo(findTestingInfo);

			result.put("iconBase64", parseAppInfo.iconBase64);

			Map<String, String> info = new LinkedHashMap<>();
			result.put("info", info);

			info.put("名称", parseAppInfo.appName);
			info.put("版本号", parseAppInfo.versionName);
			info.put("包名", parseAppInfo.packageName);
			info.put("类型", parseAppInfo.fileType);
			info.put("文件大小", parseAppInfo.fileSizeStr);
			info.put("上传时间", parseAppInfo.uploadDate);

			List<TestingInfo> findRecords = testingService.findRecords(findTestingInfo.id,findTestingInfo.packageName, pageIndex,
					pageSize);
			List<Map<String, String>> recordResults = new ArrayList<Map<String, String>>();
			if (findRecords != null && findRecords.size() > 0) {
				for (TestingInfo record : findRecords) {

					TestingAppInfo recordAppInfo = parseAppInfo(record);
					Map<String, String> recordInfo = new LinkedHashMap<>();
					recordInfo.put("名称", recordAppInfo.appName);
					recordInfo.put("版本号", recordAppInfo.versionName);
					recordInfo.put("文件大小", recordAppInfo.fileSizeStr);
					recordInfo.put("上传时间", recordAppInfo.uploadDate);
					recordInfo.put("url", getQrLinkUrl(request, recordAppInfo.id));

					recordResults.add(recordInfo);

				}

			}

			result.put("hasmore",
					findRecords != null && findRecords.size() == CommonUtils.getLimit(pageIndex, pageSize).count);
			result.put("records", recordResults);

			return result.put(Result.RESULT_OK, "成功");

		} catch (Exception e) {
			e.printStackTrace();
			return result.put(Result.RESULT_FAILD, "数据有误!");
		}

	}

	private TestingAppInfo parseAppInfo(TestingInfo findTestingInfo) {

		TestingAppInfo record = new TestingAppInfo();

		try {
			record.id = findTestingInfo.id;

			JSONObject infoObj = new JSONObject(findTestingInfo.appinfo);
			record.iconBase64 = infoObj.optString("icon");

			switch (findTestingInfo.device.toLowerCase()) {
			case "android":

				record.appName = infoObj.getJSONObject("application").getJSONArray("label").get(0).toString();

				record.versionName = infoObj.optString("versionName");

				record.packageName = infoObj.optString("package");

				break;

			case "ios":

				record.appName = infoObj.optString("CFBundleName");

				record.versionName = infoObj.optString("CFBundleShortVersionString");

				record.packageName = infoObj.optString("CFBundleIdentifier");

				break;

			default:
				break;
			}

			record.fileType = infoObj.optString("filetype");

			record.fileSizeStr = FileUtils.size2str(infoObj.optString("filesize"));

			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
			record.uploadDate = format.format(findTestingInfo.timestamp.getTime());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return record;

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
