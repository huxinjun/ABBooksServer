package com.accountbook.model;

import java.sql.Timestamp;

/**
 * 抵消记录
 * @author xinjun
 *
 */
public class Offset {
	

	public String accountId;
	public String type;
	public String name;
	public float paidIn;
	public Timestamp dateTimestamp;
	public String paidIcon;
	public String receiptIcon;
	public float money;
	
	
}
