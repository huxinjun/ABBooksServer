package com.accountbook.model;

import java.io.Serializable;
import java.sql.Timestamp;

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
	private static final long serialVersionUID = 4141825816553944756L;
	/**
	 * 
	 */
	private String date;
	/**
	 * 
	 */
	@JSONField("is_private")
	private boolean isPrivate;
	/**
	 * 
	 */
	private String imgs;
	/**
	 * 
	 */
	@JSONField("addr_lon")
	private float addrLon;
	/**
	 * 
	 */
	@JSONField("addr_lat")
	private float addrLat;
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
	@JSONField("date_timestamp")
	private Timestamp dateTimestamp;
	/**
	 * 
	 */
	@JSONField("paid_in")
	private float paidIn;
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
	private String addr;


	//**********************************************Getter and Setter************************************************

	public String getDate(){
		return this.date;
	}
	public void setDate(String date){
		this.date=date;
	}
	public String getImgs(){
		return this.imgs;
	}
	public void setImgs(String imgs){
		this.imgs=imgs;
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
	public boolean getIsPrivate(){
		return this.isPrivate;
	}
	public void setIsPrivate(boolean isPrivate){
		this.isPrivate=isPrivate;
	}
	public int getType(){
		return this.type;
	}
	public void setType(int type){
		this.type=type;
	}
	public float getAddrLon(){
		return this.addrLon;
	}
	public void setAddrLon(float addrLon){
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
	public float getPaidIn(){
		return this.paidIn;
	}
	public void setPaidIn(float paidIn){
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
	public float getAddrLat(){
		return this.addrLat;
	}
	public void setAddrLat(float addrLat){
		this.addrLat=addrLat;
	}
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id=id;
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
		return "Account [date=" + date + ", imgs=" + imgs
				+ ", payResult=" + payResult + ", description=" + description
				+ ", isPrivate=" + isPrivate + ", type=" + type
				+ ", addrLon=" + addrLon + ", addrName=" + addrName
				+ ", userId=" + userId + ", bookId=" + bookId
				+ ", createTimestamp=" + createTimestamp + ", paidIn=" + paidIn
				+ ", members=" + members + ", name=" + name
				+ ", addrLat=" + addrLat + ", id=" + id
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
		if (imgs == null) {
			if (other.imgs != null)
				return false;
		} else if (!imgs.equals(other.imgs))
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
		if (isPrivate != other.isPrivate)
			return false;
		if (type != other.type)
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
}
