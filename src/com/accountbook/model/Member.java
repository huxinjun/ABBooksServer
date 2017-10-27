package com.accountbook.model;

import com.easyjson.annotation.JSONClass;
import java.io.Serializable;
import com.easyjson.annotation.JSONField;

/**
 *
 * publicstaticfinalintRULE_TYPE_NONE=0;没有特殊规则<br>
 * @author EasyJson By xinjun
 *
 */
@JSONClass("members")
public class Member implements Serializable{
	
	public static final int RULE_TYPE_NONE=0;//没有特殊规则
	public static final int RULE_TYPE_PERCENT=1;//按百分比
	public static final int RULE_TYPE_NUMBER=2;//固定数额

	/**
	 * 自动生成的序列化串号
	 */
	private static final long serialVersionUID = 2839668470576201492L;
	/**
	 * 
	 */
	@JSONField("member_id")
	private String memberId;
	/**
	 * 应付
	 */
	@JSONField("should_pay")
	private float shouldPay;
	/**
	 * 
	 */
	@JSONField("rule_num")
	private float ruleNum;
	/**
	 * 如果此成员是组中的,那么这个属性标识父成员的id,否则该成员为账单下的,该字段为null
	 */
	private String parentMemberId;
	/**
	 * 
	 */
	@JSONField("member_icon")
	private String memberIcon;
	/**
	 * 当成员为组时,这个字段标识当前用户是否是组内的成员
	 */
	private boolean isMember;
	/**
	 * 
	 */
	@JSONField("member_name")
	private String memberName;
	/**
	 * 实付
	 */
	@JSONField("paid_in")
	private float paidIn;
	/**
	 * 
	 */
	@JSONField("is_group")
	private boolean isGroup;
	/**
	 * 
	 */
	@JSONField("money_for_self")
	private float moneyForSelf;
	/**
	 * 计算时的临时数据
	 */
	private float calcData;
	/**
	 * 
	 */
	@JSONField("account_id")
	private String accountId;
	/**
	 * 0:基于总支出的百分比的值1:缴费为一个固定值
	 */
	@JSONField("rule_type")
	private int ruleType;
	/**
	 * 
	 */
	private String id;


	//**********************************************Getter and Setter************************************************

	public float getMoneyForSelf(){
		return this.moneyForSelf;
	}
	public void setMoneyForSelf(float moneyForSelf){
		this.moneyForSelf=moneyForSelf;
	}
	public float getRuleNum(){
		return this.ruleNum;
	}
	public void setRuleNum(float ruleNum){
		this.ruleNum=ruleNum;
	}
	public float getShouldPay(){
		return this.shouldPay;
	}
	public void setShouldPay(float shouldPay){
		this.shouldPay=shouldPay;
	}
	public String getParentMemberId(){
		return this.parentMemberId;
	}
	public void setParentMemberId(String parentMemberId){
		this.parentMemberId=parentMemberId;
	}
	public String getMemberName(){
		return this.memberName;
	}
	public void setMemberName(String memberName){
		this.memberName=memberName;
	}
	public boolean getIsMember(){
		return this.isMember;
	}
	public void setIsMember(boolean isMember){
		this.isMember=isMember;
	}
	public float getCalcData(){
		return this.calcData;
	}
	public void setCalcData(float calcData){
		this.calcData=calcData;
	}
	public String getAccountId(){
		return this.accountId;
	}
	public void setAccountId(String accountId){
		this.accountId=accountId;
	}
	public float getPaidIn(){
		return this.paidIn;
	}
	public void setPaidIn(float paidIn){
		this.paidIn=paidIn;
	}
	public int getRuleType(){
		return this.ruleType;
	}
	public void setRuleType(int ruleType){
		this.ruleType=ruleType;
	}
	public String getMemberIcon(){
		return this.memberIcon;
	}
	public void setMemberIcon(String memberIcon){
		this.memberIcon=memberIcon;
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
	public String getMemberId(){
		return this.memberId;
	}
	public void setMemberId(String memberId){
		this.memberId=memberId;
	}


	//**************************************************toString******************************************************

	@Override
	public String toString() {
		return "Member [moneyForSelf=" + moneyForSelf + ", ruleNum=" + ruleNum
				+ ", shouldPay=" + shouldPay + ", parentMemberId=" + parentMemberId
				+ ", memberName=" + memberName + ", isMember=" + isMember
				+ ", calcData=" + calcData + ", accountId=" + accountId
				+ ", paidIn=" + paidIn + ", ruleType=" + ruleType
				+ ", memberIcon=" + memberIcon + ", id=" + id
				+ ", isGroup=" + isGroup + ", memberId=" + memberId
				+ "]";
	}


	//**************************************************equals******************************************************

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		if (moneyForSelf != other.moneyForSelf)
			return false;
		if (ruleNum != other.ruleNum)
			return false;
		if (shouldPay != other.shouldPay)
			return false;
		if (parentMemberId == null) {
			if (other.parentMemberId != null)
				return false;
		} else if (!parentMemberId.equals(other.parentMemberId))
			return false;
		if (memberName == null) {
			if (other.memberName != null)
				return false;
		} else if (!memberName.equals(other.memberName))
			return false;
		if (isMember != other.isMember)
			return false;
		if (calcData != other.calcData)
			return false;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (paidIn != other.paidIn)
			return false;
		if (ruleType != other.ruleType)
			return false;
		if (memberIcon == null) {
			if (other.memberIcon != null)
				return false;
		} else if (!memberIcon.equals(other.memberIcon))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isGroup != other.isGroup)
			return false;
		if (memberId == null) {
			if (other.memberId != null)
				return false;
		} else if (!memberId.equals(other.memberId))
			return false;
		return true;
	}
}
