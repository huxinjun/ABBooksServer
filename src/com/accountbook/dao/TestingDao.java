package com.accountbook.dao;


import java.util.List;
import java.util.Map;

import com.accountbook.model.TestingInfo;

public interface TestingDao {
	
	public void insert(TestingInfo testingInfo);
	public TestingInfo queryTestingInfo(String id);
	public TestingInfo queryTestingInfoByFileUrl(String fileurl);
	public int queryMaxId();
	public List<TestingInfo> queryRecordList(Map<String,Object> map);
}
