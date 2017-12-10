package com.accountbook.model;

import com.alibaba.fastjson.annotation.JSONField;

public class UserInfo {
	
	public String id;
	@JSONField(name="nickName")
	public String nickname;
	public String icon;
	public String avatarUrl;
	public String qr;
	public String gender;
	public String country;
	public String city;
	public String province;
	public String language;
	public boolean notifOpen;
	
	
	public boolean flag;


	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", nickname=" + nickname + ", icon=" + icon + ", avatarUrl=" + avatarUrl + ", qr="
				+ qr + ", gender=" + gender + ", country=" + country + ", city=" + city + ", province=" + province
				+ ", language=" + language + ", notifOpen=" + notifOpen + ", flag=" + flag + "]";
	}

	

}
