package com.accountbook.model;

import java.sql.Timestamp;

/**
 * 统计接口查出的支付记录
 * @author xinjun
 *
 */
public class PaidRecord {
	
	public String accountId;
	public float money;
	public String type;
	public Timestamp date;
	@Override
	public String toString() {
		return "PaidRecord [accountId=" + accountId + ", money=" + money + ", type=" + type + ", date=" + date + "]";
	}
	
	

}
