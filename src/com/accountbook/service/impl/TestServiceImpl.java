package com.accountbook.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accountbook.dao.TestDao;
import com.accountbook.modle.TestModel;
import com.accountbook.service.ITestService;

@Service
public class TestServiceImpl implements ITestService {
	
	@Autowired
	public TestDao dao;

	@Override
	public List<TestModel> getAll() {
		return dao.queryAll();
	}

}
