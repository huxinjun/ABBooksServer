package com.accountbook.dao;


import com.accountbook.model.TestingInfo;

public interface TestingDao {
	
	public void insert(TestingInfo testingInfo);
	public TestingInfo queryTestingInfo(String id);
	public int queryMaxId();
}
