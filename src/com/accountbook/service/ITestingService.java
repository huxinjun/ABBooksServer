package com.accountbook.service;

import com.accountbook.model.TestingInfo;

public interface ITestingService {
	
	public int findMaxId();
	public TestingInfo findTestingInfo(String id);
	public void newTestingInfo(TestingInfo testInfo);
	
	
}
