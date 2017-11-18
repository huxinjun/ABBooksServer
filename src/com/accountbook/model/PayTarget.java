package com.accountbook.model;

import com.easyjson.annotation.JSONClass;
import java.io.Serializable;
import com.easyjson.annotation.JSONField;

/**
 *
 * publicstaticfinalintSTATUS_NOT_NEED=0;无需完善<br>
 * @author EasyJson By xinjun
 *
 */
@JSONClass("pay_target")
public class PayTarget implements Serializable{
	
	public static final int STATUS_NOT_NEED=0;//无需完善
	public static final int STATUS_NEED=1;//需要手动完善组内账单
	public static final int STATUS_COMPLETED=2;//已经完善账单

	/**
	 * 自动生成的序列化串号
	 */
	private static final long serialVersionUID = -7366029436643710024L;
	/**
	 * 
	 */
	@JSONField("receipt_status")
	private int receiptStatus;
	/**
	 * 
	 */
	@JSONField("account_id")
	private String accountId;
	/**
	 * 
	 */
	@JSONField("paid_status")
	private int paidStatus;
	/**
	 * 
	 */
	private float money;
	/**
	 * 
	 */
	@JSONField("offset_money")
	private float offsetMoney;
	/**
	 * 
	 */
	@JSONField("offset_count")
	private int offsetCount;
	/**
	 * 
	 */
	@JSONField("paid_id")
	private String paidId;
	/**
	 * 
	 */
	@JSONField("receipt_id")
	private String receiptId;
	/**
	 * 
	 */
	private float waitPaidMoney;
	/**
	 * 
	 */
	private String id;


	//**********************************************Getter and Setter************************************************

	public String getAccountId(){
		return this.accountId;
	}
	public void setAccountId(String accountId){
		this.accountId=accountId;
	}
	public String getPaidId(){
		return this.paidId;
	}
	public void setPaidId(String paidId){
		this.paidId=paidId;
	}
	public int getPaidStatus(){
		return this.paidStatus;
	}
	public void setPaidStatus(int paidStatus){
		this.paidStatus=paidStatus;
	}
	public float getMoney(){
		return this.money;
	}
	public void setMoney(float money){
		this.money=money;
	}
	public float getWaitPaidMoney(){
		return this.waitPaidMoney;
	}
	public void setWaitPaidMoney(float waitPaidMoney){
		this.waitPaidMoney=waitPaidMoney;
	}
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id=id;
	}
	public float getOffsetMoney(){
		return this.offsetMoney;
	}
	public void setOffsetMoney(float offsetMoney){
		this.offsetMoney=offsetMoney;
	}
	public String getReceiptId(){
		return this.receiptId;
	}
	public void setReceiptId(String receiptId){
		this.receiptId=receiptId;
	}
	public int getReceiptStatus(){
		return this.receiptStatus;
	}
	public void setReceiptStatus(int receiptStatus){
		this.receiptStatus=receiptStatus;
	}
	public int getOffsetCount(){
		return this.offsetCount;
	}
	public void setOffsetCount(int offsetCount){
		this.offsetCount=offsetCount;
	}


	//**************************************************toString******************************************************

	@Override
	public String toString() {
		return "PayTarget [accountId=" + accountId + ", paidId=" + paidId
				+ ", paidStatus=" + paidStatus + ", money=" + money
				+ ", waitPaidMoney=" + waitPaidMoney + ", id=" + id
				+ ", offsetMoney=" + offsetMoney + ", receiptId=" + receiptId
				+ ", receiptStatus=" + receiptStatus + ", offsetCount=" + offsetCount
				+ "]";
	}


	//**************************************************equals******************************************************

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PayTarget other = (PayTarget) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (paidId == null) {
			if (other.paidId != null)
				return false;
		} else if (!paidId.equals(other.paidId))
			return false;
		if (paidStatus != other.paidStatus)
			return false;
		if (money != other.money)
			return false;
		if (waitPaidMoney != other.waitPaidMoney)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (offsetMoney != other.offsetMoney)
			return false;
		if (receiptId == null) {
			if (other.receiptId != null)
				return false;
		} else if (!receiptId.equals(other.receiptId))
			return false;
		if (receiptStatus != other.receiptStatus)
			return false;
		if (offsetCount != other.offsetCount)
			return false;
		return true;
	}
}
