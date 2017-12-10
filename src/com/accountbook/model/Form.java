package com.accountbook.model;

import java.sql.Timestamp;


public class Form {
	
	public long id;
	public String userId;
	public String formId;
	public Timestamp time;
	@Override
	public String toString() {
		return "Form [id=" + id + ", userId=" + userId + ", formId=" + formId + ", time=" + time + "]";
	}


	

}
