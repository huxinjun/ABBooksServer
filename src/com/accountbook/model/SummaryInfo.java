package com.accountbook.model;

import java.io.Serializable;

/**
 *
 * @author EasyJson By xinjun
 *
 */
public class SummaryInfo implements Serializable{

	/**
	 * 自动生成的序列化串号
	 */
	private static final long serialVersionUID = 5615160839572736007L;
	/**
	 * 
	 */
	private float money;
	/**
	 * 
	 */
	private String name;


	//**********************************************Getter and Setter************************************************

	public float getMoney(){
		return this.money;
	}
	public void setMoney(float money){
		this.money=money;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name=name;
	}


	//**************************************************toString******************************************************

	@Override
	public String toString() {
		return "SummaryInfo [money=" + money + ", name=" + name
				+ "]";
	}


	//**************************************************equals******************************************************

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SummaryInfo other = (SummaryInfo) obj;
		if (money != other.money)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
