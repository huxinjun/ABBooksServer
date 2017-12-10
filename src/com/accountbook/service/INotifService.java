package com.accountbook.service;

import com.accountbook.model.Form;

public interface INotifService {
	
	
	
	public void add(Form form);
	public Form getOne(String userId);
	public int getValidCount(String userId);
	
	public void delete(long id);
	public void deleteInvalid(String userId);
	
	public void switcher(String id,boolean isOpen);
	public boolean isNotifOpen(String userId);
	
	
}
