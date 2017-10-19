package com.accountbook.model;

import com.easyjson.annotation.JSONClass;
import java.io.Serializable;
import com.easyjson.annotation.JSONField;
import java.util.ArrayList;

/**
 *
 * @author EasyJson By xinjun
 *
 */
@JSONClass("pay_result")
public class PayResult implements Serializable{

	/**
	 * 自动生成的序列化串号
	 */
	private static final long serialVersionUID = 5438936334749897067L;
	/**
	 * 付账计算结果
	 */
	@JSONField("pay_target")
	private ArrayList<PayTarget> payTarget;


	//**********************************************Getter and Setter************************************************

	public ArrayList<PayTarget> getPayTarget(){
		return this.payTarget;
	}
	public void setPayTarget(ArrayList<PayTarget> payTarget){
		this.payTarget=payTarget;
	}


	//**************************************************toString******************************************************

	@Override
	public String toString() {
		return "PayResult [payTarget=" + payTarget + "]";
	}


	//**************************************************equals******************************************************

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PayResult other = (PayResult) obj;
		if (payTarget == null) {
			if (other.payTarget != null && other.payTarget.size()!=0)
				return false;
		} else {
			for(int i=0;i<payTarget.size();i++)
				if (!payTarget.get(i).equals(other.payTarget.get(i)))
					return false;
		}
		return true;
	}
}
