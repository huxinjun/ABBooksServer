package com.accountbook.model;

import com.easyjson.annotation.JSONClass;
import java.io.Serializable;
import com.easyjson.annotation.JSONField;

/**
 *
 * @author EasyJson By xinjun
 *
 */
@JSONClass("pay_target")
public class PayTarget implements Serializable{
	
	
	
	public static final int STATUS_NOT_NEED=0;//无需完善组内账单
	public static final int STATUS_NEED=1;//需要手动完善
	public static final int STATUS_SUCCESS=2;//已经完成

	/**
	 * 自动生成的序列化串号
	 */
	private static final long serialVersionUID = -5458542787310028971L;
	/**
	 * 收款者状态
	 */
	@JSONField("receipt_status")
	private int receiptStatus;
	/**
	 * 
	 */
	@JSONField("account_id")
	private String accountId;
	/**
	 * 支付者状态
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
	/**
	 * 如果此支付方案是针对一个组下成员的,那么这个属性标识所属的父支付方案id
	 */
	private String parentPayId;


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
	public String getParentPayId(){
		return this.parentPayId;
	}
	public void setParentPayId(String parentPayId){
		this.parentPayId=parentPayId;
	}


	//**************************************************toString******************************************************

	@Override
	public String toString() {
		return "PayTarget [accountId=" + accountId + ", paidId=" + paidId
				+ ", paidStatus=" + paidStatus + ", money=" + money
				+ ", settled=" + settled + ", id=" + id
				+ ", receiptId=" + receiptId + ", receiptStatus=" + receiptStatus
				+ ", parentPayId=" + parentPayId + "]";
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
		if (parentPayId == null) {
			if (other.parentPayId != null)
				return false;
		} else if (!parentPayId.equals(other.parentPayId))
			return false;
		return true;
	}
}
