package com.abbooks.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abbooks.dao.TestDao;
import com.abbooks.modle.TestModel;
import com.abbooks.service.ITestService;

@Service
public class TestServiceImpl implements ITestService {
	
	@Autowired
	public TestDao dao;

	@Override
	public List<TestModel> getAll() {
		return dao.queryAll();
	}

}
