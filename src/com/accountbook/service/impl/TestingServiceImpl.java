package com.accountbook.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.TestingDao;
import com.accountbook.model.TestingInfo;
import com.accountbook.service.ITestingService;

@Service
public class TestingServiceImpl implements ITestingService {

	@Autowired
	TestingDao dao;
	@Override
	public TestingInfo findTestingInfo(String id) {
		return dao.queryTestingInfo(id);
	}
	@Override
	public void newTestingInfo(TestingInfo testInfo) {
		dao.insert(testInfo);
	}
	@Override
	public int findMaxId() {
		return dao.queryMaxId();
	}
	@Override
	public TestingInfo findTestingInfoByFileUrl(String fileurl) {
		return dao.queryTestingInfoByFileUrl(fileurl);
	}

}
