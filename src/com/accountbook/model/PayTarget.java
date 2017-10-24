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
	private static final long serialVersionUID = -1182512269534249348L;
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
	private boolean settled;
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
	public boolean getSettled(){
		return this.settled;
	}
	public void setSettled(boolean settled){
		this.settled=settled;
	}
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id=id;
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


	//**************************************************toString******************************************************

	@Override
	public String toString() {
		return "PayTarget [accountId=" + accountId + ", paidId=" + paidId
				+ ", paidStatus=" + paidStatus + ", money=" + money
				+ ", settled=" + settled + ", id=" + id
				+ ", receiptId=" + receiptId + ", receiptStatus=" + receiptStatus
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
		if (settled != other.settled)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (receiptId == null) {
			if (other.receiptId != null)
				return false;
		} else if (!receiptId.equals(other.receiptId))
			return false;
		if (receiptStatus != other.receiptStatus)
			return false;
		return true;
	}
}
