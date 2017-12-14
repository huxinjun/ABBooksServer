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
	private static final long serialVersionUID = -2687683106585438731L;
	/**
	 * 
	 */
	private float number;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private int count;


	//**********************************************Getter and Setter************************************************

	public float getNumber(){
		return this.number;
	}
	public void setNumber(float number){
		this.number=number;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name=name;
	}
	public int getCount(){
		return this.count;
	}
	public void setCount(int count){
		this.count=count;
	}


	//**************************************************toString******************************************************

	@Override
	public String toString() {
		return "SummaryInfo [number=" + number + ", name=" + name
				+ ", count=" + count + "]";
	}


	//**************************************************equals******************************************************

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SummaryInfo other = (SummaryInfo) obj;
		if (number != other.number)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (count != other.count)
			return false;
		return true;
	}
}
