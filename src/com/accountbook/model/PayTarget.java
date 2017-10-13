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

	/**
	 * 自动生成的序列化串号
	 */
	private static final long serialVersionUID = 5054171166713967060L;
	/**
	 * 
	 */
	@JSONField("account_id")
	private String accountId;
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
	public String getReceiptId(){
		return this.receiptId;
	}
	public void setReceiptId(String receiptId){
		this.receiptId=receiptId;
	}


	//**************************************************toString******************************************************

	@Override
	public String toString() {
		return "PayTarget [accountId=" + accountId + ", paidId=" + paidId
				+ ", money=" + money + ", settled=" + settled
				+ ", receiptId=" + receiptId + "]";
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
		if (money != other.money)
			return false;
		if (settled != other.settled)
			return false;
		if (receiptId == null) {
			if (other.receiptId != null)
				return false;
		} else if (!receiptId.equals(other.receiptId))
			return false;
		return true;
	}
}
