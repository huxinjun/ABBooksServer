package com.accountbook.modle;

import java.io.Serializable;
import com.easyjson.annotation.JSONClass;
import com.easyjson.annotation.JSONField;
import java.util.ArrayList;

/**
 *
 * @author EasyJson By xinjun
 *
 */
public class Account implements Serializable{

	/**
	 * 自动生成的序列化串号
	 */
	private static final long serialVersionUID = -2299304931938128651L;
	/**
	 * 
	 */
	@JSONField("persons")
	private ArrayList<Person> persons;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	@JSONField("paid_in")
	private double paidIn;
	/**
	 * 
	 */
	@JSONField("pay_result")
	private ArrayList<PayResult> payResult;


	//**********************************************Getter and Setter************************************************

	public ArrayList<Person> getPersons(){
		return this.persons;
	}
	public void setPersons(ArrayList<Person> persons){
		this.persons=persons;
	}
	public ArrayList<PayResult> getPayResult(){
		return this.payResult;
	}
	public void setPayResult(ArrayList<PayResult> payResult){
		this.payResult=payResult;
	}
	public double getPaidIn(){
		return this.paidIn;
	}
	public void setPaidIn(double paidIn){
		this.paidIn=paidIn;
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
		return "Account [persons=" + persons + ", payResult=" + payResult
				+ ", paidIn=" + paidIn + ", name=" + name
				+ "]";
	}


	//**************************************************equals******************************************************

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (persons == null) {
			if (other.persons != null && other.persons.size()!=0)
				return false;
		} else {
			for(int i=0;i<persons.size();i++)
				if (!persons.get(i).equals(other.persons.get(i)))
					return false;
		}
		if (payResult == null) {
			if (other.payResult != null && other.payResult.size()!=0)
				return false;
		} else {
			for(int i=0;i<payResult.size();i++)
				if (!payResult.get(i).equals(other.payResult.get(i)))
					return false;
		}
		if (paidIn != other.paidIn)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 *
	 * @author EasyJson By xinjun
	 *
	 */
	@JSONClass("persons")
	public static class Person implements Serializable{

		/**
		 * 自动生成的序列化串号
		 */
		private static final long serialVersionUID = -2297652444426477227L;
		/**
		 * 应付
		 */
		@JSONField("shoud_pay")
		private double shoudPay;
		/**
		 * 
		 */
		private double tempData;
		/**
		 * 
		 */
		private String name;
		/**
		 * 
		 */
		@JSONField("pay_rule")
		private PayRule payRule;
		/**
		 * 
		 */
		private String id;
		/**
		 * 实付
		 */
		@JSONField("paid_in")
		private double paidIn;
		/**
		 * 给自己买东西的钱,其他人不会AA支付这个数额
		 */
		@JSONField("money_for_self")
		private double moneyForSelf;


		//**********************************************Getter and Setter************************************************

		public double getMoneyForSelf(){
			return this.moneyForSelf;
		}
		public void setMoneyForSelf(double moneyForSelf){
			this.moneyForSelf=moneyForSelf;
		}
		public PayRule getPayRule(){
			return this.payRule;
		}
		public void setPayRule(PayRule payRule){
			this.payRule=payRule;
		}
		public double getTempData(){
			return this.tempData;
		}
		public void setTempData(double tempData){
			this.tempData=tempData;
		}
		public double getShoudPay(){
			return this.shoudPay;
		}
		public void setShoudPay(double shoudPay){
			this.shoudPay=shoudPay;
		}
		public double getPaidIn(){
			return this.paidIn;
		}
		public void setPaidIn(double paidIn){
			this.paidIn=paidIn;
		}
		public String getName(){
			return this.name;
		}
		public void setName(String name){
			this.name=name;
		}
		public String getId(){
			return this.id;
		}
		public void setId(String id){
			this.id=id;
		}


		//**************************************************toString******************************************************

		@Override
		public String toString() {
			return "Person [moneyForSelf=" + moneyForSelf + ", payRule=" + payRule
					+ ", tempData=" + tempData + ", shoudPay=" + shoudPay
					+ ", paidIn=" + paidIn + ", name=" + name
					+ ", id=" + id + "]";
		}


		//**************************************************equals******************************************************

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Person other = (Person) obj;
			if (moneyForSelf != other.moneyForSelf)
				return false;
			if (payRule == null) {
				if (other.payRule != null)
					return false;
			} else if (!payRule.equals(other.payRule))
				return false;
			if (tempData != other.tempData)
				return false;
			if (shoudPay != other.shoudPay)
				return false;
			if (paidIn != other.paidIn)
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

		/**
		 *
		 * @author EasyJson By xinjun
		 *
		 */
		@JSONClass("pay_rule")
		public static class PayRule implements Serializable{

			/**
			 * 自动生成的序列化串号
			 */
			private static final long serialVersionUID = -2360446836725245165L;
			/**
			 * 
			 */
			private double number;
			/**
			 * 
			 */
			@JSONField("number_type")
			private int numberType;


			//**********************************************Getter and Setter************************************************

			public double getNumber(){
				return this.number;
			}
			public void setNumber(double number){
				this.number=number;
			}
			public int getNumberType(){
				return this.numberType;
			}
			public void setNumberType(int numberType){
				this.numberType=numberType;
			}


			//**************************************************toString******************************************************

			@Override
			public String toString() {
				return "PayRule [number=" + number + ", numberType=" + numberType
						+ "]";
			}


			//**************************************************equals******************************************************

			@Override
			public boolean equals(Object obj) {
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				PayRule other = (PayRule) obj;
				if (number != other.number)
					return false;
				if (numberType != other.numberType)
					return false;
				return true;
			}
		}
	}

	/**
	 *
	 * @author EasyJson By xinjun
	 *
	 */
	@JSONClass("pay_result")
	public static class PayResult implements Serializable{

		/**
		 * 自动生成的序列化串号
		 */
		private static final long serialVersionUID = -2334007092373397228L;
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

		/**
		 *
		 * 谁支付<br>
		 * @author EasyJson By xinjun
		 *
		 */
		@JSONClass("pay_target")
		public static class PayTarget implements Serializable{

			/**
			 * 自动生成的序列化串号
			 */
			private static final long serialVersionUID = -2329049642723344844L;
			/**
			 * 
			 */
			private double money;
			/**
			 * 
			 */
			@JSONField("pay_id")
			private String payId;
			/**
			 * 
			 */
			@JSONField("get_id")
			private String getId;


			//**********************************************Getter and Setter************************************************

			public double getMoney(){
				return this.money;
			}
			public void setMoney(double money){
				this.money=money;
			}
			public String getGetId(){
				return this.getId;
			}
			public void setGetId(String getId){
				this.getId=getId;
			}
			public String getPayId(){
				return this.payId;
			}
			public void setPayId(String payId){
				this.payId=payId;
			}


			//**************************************************toString******************************************************

			@Override
			public String toString() {
				return "PayTarget [money=" + money + ", getId=" + getId
						+ ", payId=" + payId + "]";
			}


			//**************************************************equals******************************************************

			@Override
			public boolean equals(Object obj) {
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				PayTarget other = (PayTarget) obj;
				if (money != other.money)
					return false;
				if (getId == null) {
					if (other.getId != null)
						return false;
				} else if (!getId.equals(other.getId))
					return false;
				if (payId == null) {
					if (other.payId != null)
						return false;
				} else if (!payId.equals(other.payId))
					return false;
				return true;
			}
		}
	}
}
