package com.accountbook.service;

import java.util.List;

import com.accountbook.model.TestingInfo;

public interface ITestingService {
	
	public int findMaxId();
	public TestingInfo findTestingInfo(String id);
	public TestingInfo findTestingInfoByFileUrl(String fileurl);
	public void newTestingInfo(TestingInfo testInfo);
	
	public List<TestingInfo> findRecords(int id,String packageName,Integer pageIndex,Integer pageSize);
	
	
}
