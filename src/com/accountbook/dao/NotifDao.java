package com.accountbook.dao;


import java.util.Map;

import com.accountbook.model.Form;

public interface NotifDao {
	
	public void insert(Form form);
	public Form queryOne(String userId);
	public void delete(long id);
	public void deleteInvalid(String userId);
	public int queryValidCount(String userId);
	public void switcher(Map<String,Object> params);
	public boolean isNotifOpen(String userId);
}
