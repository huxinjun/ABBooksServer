package com.accountbook.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.TestingDao;
import com.accountbook.model.TestingInfo;
import com.accountbook.service.ITestingService;
import com.accountbook.utils.CommonUtils;
import com.accountbook.utils.CommonUtils.Limit;

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

	@Override
	public List<TestingInfo> findRecords(int id,String packageName, Integer pageIndex, Integer pageSize) {
		List<TestingInfo> results;
		Limit limit = CommonUtils.getLimit(pageIndex, pageSize);
		results = dao.queryRecordList(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("id", id);
				put("packageName", packageName);
				put("ls", limit.start);
				put("lc", limit.count);
			}
		});
		return results;
	}

}
