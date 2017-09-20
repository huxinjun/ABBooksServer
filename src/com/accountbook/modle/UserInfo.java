package com.accountbook.modle;

import com.alibaba.fastjson.annotation.JSONField;

public class UserInfo {
	
	public String id;
	@JSONField(name="nickName")
	public String nickname;
	public String icon;
	public String avatarUrl;
	public String gender;
	public String country;
	public String city;
	public String province;
	public String language;
	
	
	public boolean flag;


	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", nickname=" + nickname + ", icon=" + icon + ", avatarUrl=" + avatarUrl
				+ ", gender=" + gender + ", country=" + country + ", city=" + city + ", province=" + province
				+ ", language=" + language + ", flag=" + flag + "]";
	}
	
	
	

}
