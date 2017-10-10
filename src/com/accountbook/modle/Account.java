package com.accountbook.modle;

import java.io.Serializable;
import java.sql.Timestamp;

import com.easyjson.annotation.JSONField;
import com.easyjson.annotation.JSONClass;
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
	private static final long serialVersionUID = 3374950023831954096L;
	/**
	 * 
	 */
	private String date;
	/**
	 * 
	 */
	@JSONField("addr_lon")
	private double addrLon;
	/**
	 * 
	 */
	@JSONField("addr_lat")
	private double addrLat;
	/**
	 * 
	 */
	private String description;
	/**
	 * 
	 */
	@JSONField("book_id")
	private String bookId;
	/**
	 * 
	 */
	private int type;
	/**
	 * 
	 */
	private String icons;
	/**
	 * 
	 */
	@JSONField("date_timestamp")
	private Timestamp dateTimestamp;
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
	/**
	 * 
	 */
	@JSONField("addr_name")
	private String addrName;
	/**
	 * 
	 */
	@JSONField("create_timestamp")
	private Timestamp createTimestamp;
	/**
	 * 
	 */
	@JSONField("user_id")
	private String userId;
	/**
	 * 
	 */
	@JSONField("members")
	private ArrayList<Member> members;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private String id;
	/**
	 * 
	 */
	@JSONField("pay_id")
	private String payId;
	/**
	 * 
	 */
	private String addr;


	//**********************************************Getter and Setter************************************************

	public String getDate(){
		return this.date;
	}
	public void setDate(String date){
		this.date=date;
	}
	public ArrayList<PayResult> getPayResult(){
		return this.payResult;
	}
	public void setPayResult(ArrayList<PayResult> payResult){
		this.payResult=payResult;
	}
	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		this.description=description;
	}
	public int getType(){
		return this.type;
	}
	public void setType(int type){
		this.type=type;
	}
	public String getIcons(){
		return this.icons;
	}
	public void setIcons(String icons){
		this.icons=icons;
	}
	public double getAddrLon(){
		return this.addrLon;
	}
	public void setAddrLon(double addrLon){
		this.addrLon=addrLon;
	}
	public String getAddrName(){
		return this.addrName;
	}
	public void setAddrName(String addrName){
		this.addrName=addrName;
	}
	public String getUserId(){
		return this.userId;
	}
	public void setUserId(String userId){
		this.userId=userId;
	}
	public String getBookId(){
		return this.bookId;
	}
	public void setBookId(String bookId){
		this.bookId=bookId;
	}
	public Timestamp getCreateTimestamp(){
		return this.createTimestamp;
	}
	public void setCreateTimestamp(Timestamp createTimestamp){
		this.createTimestamp=createTimestamp;
	}
	public double getPaidIn(){
		return this.paidIn;
	}
	public void setPaidIn(double paidIn){
		this.paidIn=paidIn;
	}
	public ArrayList<Member> getMembers(){
		return this.members;
	}
	public void setMembers(ArrayList<Member> members){
		this.members=members;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name=name;
	}
	public double getAddrLat(){
		return this.addrLat;
	}
	public void setAddrLat(double addrLat){
		this.addrLat=addrLat;
	}
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id=id;
	}
	public String getPayId(){
		return this.payId;
	}
	public void setPayId(String payId){
		this.payId=payId;
	}
	public Timestamp getDateTimestamp(){
		return this.dateTimestamp;
	}
	public void setDateTimestamp(Timestamp dateTimestamp){
		this.dateTimestamp=dateTimestamp;
	}
	public String getAddr(){
		return this.addr;
	}
	public void setAddr(String addr){
		this.addr=addr;
	}


	//**************************************************toString******************************************************

	@Override
	public String toString() {
		return "Account [date=" + date + ", payResult=" + payResult
				+ ", description=" + description + ", type=" + type
				+ ", icons=" + icons + ", addrLon=" + addrLon
				+ ", addrName=" + addrName + ", userId=" + userId
				+ ", bookId=" + bookId + ", createTimestamp=" + createTimestamp
				+ ", paidIn=" + paidIn + ", members=" + members
				+ ", name=" + name + ", addrLat=" + addrLat
				+ ", id=" + id + ", payId=" + payId
				+ ", dateTimestamp=" + dateTimestamp + ", addr=" + addr
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
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (payResult == null) {
			if (other.payResult != null && other.payResult.size()!=0)
				return false;
		} else {
			for(int i=0;i<payResult.size();i++)
				if (!payResult.get(i).equals(other.payResult.get(i)))
					return false;
		}
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (type != other.type)
			return false;
		if (icons == null) {
			if (other.icons != null)
				return false;
		} else if (!icons.equals(other.icons))
			return false;
		if (addrLon != other.addrLon)
			return false;
		if (addrName == null) {
			if (other.addrName != null)
				return false;
		} else if (!addrName.equals(other.addrName))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (bookId == null) {
			if (other.bookId != null)
				return false;
		} else if (!bookId.equals(other.bookId))
			return false;
		if (createTimestamp == null) {
			if (other.createTimestamp != null)
				return false;
		} else if (!createTimestamp.equals(other.createTimestamp))
			return false;
		if (paidIn != other.paidIn)
			return false;
		if (members == null) {
			if (other.members != null && other.members.size()!=0)
				return false;
		} else {
			for(int i=0;i<members.size();i++)
				if (!members.get(i).equals(other.members.get(i)))
					return false;
		}
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (addrLat != other.addrLat)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (payId == null) {
			if (other.payId != null)
				return false;
		} else if (!payId.equals(other.payId))
			return false;
		if (dateTimestamp == null) {
			if (other.dateTimestamp != null)
				return false;
		} else if (!dateTimestamp.equals(other.dateTimestamp))
			return false;
		if (addr == null) {
			if (other.addr != null)
				return false;
		} else if (!addr.equals(other.addr))
			return false;
		return true;
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
		private static final long serialVersionUID = 3361730145213579183L;
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
			private static final long serialVersionUID = 3366687599158598863L;
			/**
			 * 
			 */
			@JSONField("pay_member")
			private String payMember;
			/**
			 * 
			 */
			private double money;
			/**
			 * 
			 */
			@JSONField("receipt_member")
			private String receiptMember;


			//**********************************************Getter and Setter************************************************

			public double getMoney(){
				return this.money;
			}
			public void setMoney(double money){
				this.money=money;
			}
			public String getReceiptMember(){
				return this.receiptMember;
			}
			public void setReceiptMember(String receiptMember){
				this.receiptMember=receiptMember;
			}
			public String getPayMember(){
				return this.payMember;
			}
			public void setPayMember(String payMember){
				this.payMember=payMember;
			}


			//**************************************************toString******************************************************

			@Override
			public String toString() {
				return "PayTarget [money=" + money + ", receiptMember=" + receiptMember
						+ ", payMember=" + payMember + "]";
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
				if (receiptMember == null) {
					if (other.receiptMember != null)
						return false;
				} else if (!receiptMember.equals(other.receiptMember))
					return false;
				if (payMember == null) {
					if (other.payMember != null)
						return false;
				} else if (!payMember.equals(other.payMember))
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
	@JSONClass("members")
	public static class Member implements Serializable{

		/**
		 * 自动生成的序列化串号
		 */
		private static final long serialVersionUID = 3720319166442792216L;
		/**
		 * 计算时的临时数据
		 */
		private double calcData;
		/**
		 * 应付
		 */
		@JSONField("shoud_pay")
		private double shoudPay;
		/**
		 * 
		 */
		private String name;
		/**
		 * 
		 */
		private String icon;
		/**
		 * 0:基于总支出的百分比的值1:缴费为一个固定值
		 */
		@JSONField("pay_rule")
		private PayRule payRule;
		/**
		 * 
		 */
		private String id;
		/**
		 * 
		 */
		private boolean isGroup;
		/**
		 * 实付
		 */
		@JSONField("paid_in")
		private double paidIn;
		/**
		 * 
		 */
		@JSONField("money_for_self")
		private double moneyForSelf;


		//**********************************************Getter and Setter************************************************

		public double getCalcData(){
			return this.calcData;
		}
		public void setCalcData(double calcData){
			this.calcData=calcData;
		}
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
		public String getIcon(){
			return this.icon;
		}
		public void setIcon(String icon){
			this.icon=icon;
		}
		public String getId(){
			return this.id;
		}
		public void setId(String id){
			this.id=id;
		}
		public boolean getIsGroup(){
			return this.isGroup;
		}
		public void setIsGroup(boolean isGroup){
			this.isGroup=isGroup;
		}


		//**************************************************toString******************************************************

		@Override
		public String toString() {
			return "Member [calcData=" + calcData + ", moneyForSelf=" + moneyForSelf
					+ ", payRule=" + payRule + ", shoudPay=" + shoudPay
					+ ", paidIn=" + paidIn + ", name=" + name
					+ ", icon=" + icon + ", id=" + id
					+ ", isGroup=" + isGroup + "]";
		}


		//**************************************************equals******************************************************

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Member other = (Member) obj;
			if (calcData != other.calcData)
				return false;
			if (moneyForSelf != other.moneyForSelf)
				return false;
			if (payRule == null) {
				if (other.payRule != null)
					return false;
			} else if (!payRule.equals(other.payRule))
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
			if (icon == null) {
				if (other.icon != null)
					return false;
			} else if (!icon.equals(other.icon))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (isGroup != other.isGroup)
				return false;
			return true;
		}

		/**
		 *
		 * 0:基于总支出的百分比的值1:缴费为一个固定值<br>
		 * @author EasyJson By xinjun
		 *
		 */
		@JSONClass("pay_rule")
		public static class PayRule implements Serializable{

			/**
			 * 自动生成的序列化串号
			 */
			private static final long serialVersionUID = 3723624132876160472L;
			/**
			 * 
			 */
			private double num;
			/**
			 * 
			 */
			private int type;


			//**********************************************Getter and Setter************************************************

			public double getNum(){
				return this.num;
			}
			public void setNum(double num){
				this.num=num;
			}
			public int getType(){
				return this.type;
			}
			public void setType(int type){
				this.type=type;
			}


			//**************************************************toString******************************************************

			@Override
			public String toString() {
				return "PayRule [num=" + num + ", type=" + type
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
				if (num != other.num)
					return false;
				if (type != other.type)
					return false;
				return true;
			}
		}
	}
}
