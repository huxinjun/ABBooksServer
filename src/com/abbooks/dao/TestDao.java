package com.abbooks.dao;


import java.util.List;

import com.abbooks.modle.TestModel;

public interface TestDao {
	public void insert(TestModel model);
	public TestModel query(int id);
	public List<TestModel> queryAll();
	public void update(TestModel model);
	public void delete(int id);
}
