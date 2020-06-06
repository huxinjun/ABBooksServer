package com.accountbook.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.accountbook.model.TestingAppInfo;
import com.accountbook.model.TestingInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.ITestingService;
import com.accountbook.utils.CommonUtils;
import com.accountbook.utils.CommonUtils.Limit;
import com.accountbook.utils.FileUtils;
import com.accountbook.utils.QrUtil;
import com.accountbook.utils.TextUtils;

@Controller
@RequestMapping("/testing")
public class TestingController {

	private static final int QrcodeSize = 2000;

	@Autowired
	ITestingService testingService;

	@ResponseBody
	@RequestMapping(value = "/qrcode")
	public Object qrcode(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {

		TestingInfo testingInfo = saveRecord(request);

		TestingAppInfo parseAppInfo = parseAppInfo(testingInfo);
		if (TextUtils.isEmpty(parseAppInfo.iconBase64)) {
			@SuppressWarnings("deprecation")
			String logoPath = request.getRealPath("/WEB-INF/static/testing/images/unknow_file.png");
			System.out.println("logoPath=" + logoPath);
			QrUtil.generateQRCodeImage(getQrLinkUrl(request, testingInfo.id), QrcodeSize, response.getOutputStream(),
					logoPath, parseAppInfo);
		} else {
			ByteArrayInputStream logoInputStream = new ByteArrayInputStream(
					Base64.getDecoder().decode(parseAppInfo.iconBase64.split(",")[1]));
			QrUtil.generateQRCodeImage(getQrLinkUrl(request, testingInfo.id), QrcodeSize, response.getOutputStream(),
					logoInputStream, parseAppInfo);
		}

		return null;

	}

	@ResponseBody
	@RequestMapping(value = "/qrcode_b64")
	public Object qrcodeBase64(HttpServletRequest request, HttpServletResponse response)
			throws IOException, JSONException {

		TestingInfo testingInfo = saveRecord(request);

		String relativePath = FileUtils.genarateFileRelativePathByName("temp_" + testingInfo.id, "testing");
		String serverPath = FileUtils.getImageAbsolutePath(relativePath);
		File serverFile = new File(serverPath);
		FileOutputStream foo = new FileOutputStream(serverFile);

		TestingAppInfo parseAppInfo = parseAppInfo(testingInfo);
		System.out.println("iconBase64=" + parseAppInfo.iconBase64);
		if (TextUtils.isEmpty(parseAppInfo.iconBase64)) {
			@SuppressWarnings("deprecation")
			String logoPath = request.getRealPath("/WEB-INF/static/testing/images/unknow_file.png");
			System.out.println("logoPath=" + logoPath);
			QrUtil.generateQRCodeImage(getQrLinkUrl(request, testingInfo.id), QrcodeSize, foo, logoPath, parseAppInfo);
		} else {
			InputStream logoInputStream = QrUtil.getImageStreamByBase64(parseAppInfo.iconBase64);
			QrUtil.generateQRCodeImage(getQrLinkUrl(request, testingInfo.id), QrcodeSize, foo, logoInputStream,
					parseAppInfo);
		}

		byte[] bytes = new byte[(int) serverFile.length()];

		FileInputStream fis = new FileInputStream(serverFile);
		fis.read(bytes);
		fis.close();
		
		boolean delete = serverFile.delete();
		System.out.println("temp file="+serverFile.getPath()+",is delete="+delete);

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
	private TestingInfo saveRecord(HttpServletRequest request) throws UnsupportedEncodingException {
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
		TestingInfo testingInfo = testingService.findTestingInfoByFileUrl(fileurl);
		if (testingInfo == null) {
			// 没有此资源记录
			testingInfo = new TestingInfo();
			testingInfo.fileurl = fileurl;
			testingInfo.device = device;
			testingInfo.comments = comment;
			testingInfo.appinfo = appinfo;
			testingInfo.packageName = getPackageName(device, appinfo);
			testingInfo.timestamp = new Timestamp(System.currentTimeMillis());
			// 存到数据库
			testingService.newTestingInfo(testingInfo);
			testingInfo.id = testingService.findMaxId();
		} else {
			System.out.println("testing.qrcode repeat,old id=" + maxId);
		}

		return testingInfo;
	}

	/**
	 * 二维码点击跳转的地址
	 */
	private String getQrLinkUrl(HttpServletRequest request, int id) {
		String serverName = request.getServerName();
		boolean isTestMode = serverName.startsWith("localhost") || serverName.startsWith("192")
				|| serverName.startsWith("127");

		String linkUrl = "";
		if (isTestMode) {
			// 测试
			linkUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
					+ "/testing/detail.html?id=" + id;
		} else {
			// 线上
			linkUrl = "https://" + request.getServerName() + request.getContextPath() + "/testing/detail.html?id=" + id;
		}
		System.out.println("linkUrl=" + linkUrl);
		return linkUrl;
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

	/**
	 * 解析包名,如果文件不是apk,或者ipa就默认为other
	 */
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

	/**
	 * 封装测试包详情展示页面需要的数据
	 */
	private Result assembleInfoResult(HttpServletRequest request, TestingInfo findTestingInfo, Integer pageIndex,
			Integer pageSize) {
		Result result = new Result();

		try {

			result.put("comment", findTestingInfo.comments);
			result.put("fileUrl", findTestingInfo.fileurl);
			result.put("device", findTestingInfo.device);
			result.put("id", findTestingInfo.id);

			TestingAppInfo parseAppInfo = parseAppInfo(findTestingInfo);

			result.put("iconBase64", parseAppInfo.iconBase64);

			Map<String, String> info = new LinkedHashMap<>();
			result.put("info", info);

			info.put("平台", findTestingInfo.device);
			info.put("名称", parseAppInfo.appName);
			info.put("版本号", parseAppInfo.versionName);
			info.put("包名", parseAppInfo.packageName);
			info.put("文件类型", parseAppInfo.fileType);
			info.put("发布类型", parseAppInfo.developType);
			info.put("文件大小", parseAppInfo.fileSizeStr);
			info.put("上传时间", parseAppInfo.uploadDate);

			List<TestingInfo> findRecords = testingService.findRecords(findTestingInfo.id, findTestingInfo.packageName,
					findTestingInfo.device, pageIndex, pageSize);
			List<Map<String, String>> recordResults = new ArrayList<Map<String, String>>();
			if (findRecords != null && findRecords.size() > 0) {
				for (TestingInfo record : findRecords) {

					TestingAppInfo recordAppInfo = parseAppInfo(record);
					Map<String, String> recordInfo = new LinkedHashMap<>();
					recordInfo.put("appname", recordAppInfo.appName);
					recordInfo.put("version", recordAppInfo.versionName);
					recordInfo.put("size", recordAppInfo.fileSizeStr);
					recordInfo.put("uploadDate", recordAppInfo.uploadDate);
					recordInfo.put("comment", record.comments);
					recordInfo.put("id", record.id + "");

					recordResults.add(recordInfo);

				}

			}
			Limit limit = CommonUtils.getLimit(pageIndex, pageSize);

			result.put("hasmore", findRecords != null && findRecords.size() == limit.count);
			result.put("nextPage", pageIndex == null ? 1 : pageIndex + 1);
			result.put("records", recordResults);

			return result.put(Result.RESULT_OK, "成功");

		} catch (Exception e) {
			e.printStackTrace();
			return result.put(Result.RESULT_FAILD, "数据有误!");
		}

	}

	/**
	 * 解析包信息
	 */
	private TestingAppInfo parseAppInfo(TestingInfo findTestingInfo) {

		TestingAppInfo record = new TestingAppInfo();

		try {
			record.id = findTestingInfo.id;
			record.device = findTestingInfo.device;

			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
			record.uploadDate = format.format(findTestingInfo.timestamp.getTime());

			JSONObject infoObj = new JSONObject(findTestingInfo.appinfo);
			record.iconBase64 = infoObj.optString("icon", "");
			if ("null".equals(record.iconBase64))
				record.iconBase64 = null;

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
				record.appName = "未知";
				record.versionName = "未知";
				record.packageName = "未知";
				break;
			}

			record.fileType = infoObj.optString("filetype");
			record.developType = infoObj.optString("developType");
			record.fileSizeStr = FileUtils.size2str(infoObj.optString("filesize"));

		} catch (Exception e) {
			System.out.println("parse app info error!!!");
			record.appName = "未知";
			record.versionName = "未知";
			record.packageName = "未知";

			record.fileType = "未知";
			record.developType = "未知";
			record.fileSizeStr = "未知";
		}
		return record;

	}

	@ResponseBody
	@RequestMapping(value = "/ios/install_{id}.plist")
	public void upload(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "id") String id)
			throws IOException {

		System.out.println("/ios/install.plist,id=" + id);

		TestingInfo findTestingInfo = testingService.findTestingInfo(id);
		if (findTestingInfo == null)
			return;

		TestingAppInfo parseAppInfo = parseAppInfo(findTestingInfo);

		@SuppressWarnings("deprecation")
		String plist = request.getRealPath("/WEB-INF/static/testing/IOSInstallProfile.plist");
		File plistFile = new File(plist);

		FileInputStream inputStream = new FileInputStream(plistFile);
		byte[] buf = new byte[(int) plistFile.length()];
		int length = inputStream.read(buf);
		inputStream.close();

		// 将字节数组中指定位置的字节转码成对应的字符串
		String content = new String(buf, 0, length, Charset.forName("UTF-8"));

		content = content.replace("#{url}#", findTestingInfo.fileurl)
				.replace("#{packageName}#", parseAppInfo.packageName).replace("#{version}#", parseAppInfo.versionName)
				.replace("#{appName}#", parseAppInfo.appName);

		byte[] bytes = content.getBytes("UTF-8");

		OutputStream os = response.getOutputStream();
		os.write(bytes, 0, bytes.length);
		os.flush();
		os.close();
	}

	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public Object upload(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println("上传测试包文件来了:" + file.getOriginalFilename());

		String relativePath = FileUtils.saveFileByName(file.getInputStream(), file.getOriginalFilename(), "testing");
		return FileUtils.getImageUrl(request, relativePath);
		// return new Result(Result.RESULT_OK, "上传成功").put("fileurl",
		// FileUtils.getImageUrl(request, relativePath));
	}

}
