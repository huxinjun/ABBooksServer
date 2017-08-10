package com.accountbook.modle;

import com.alibaba.fastjson.annotation.JSONField;

public class UserInfo {
	
	public String id;
	@JSONField(name="nickName")
	public String nickname;
	public String avatarUrl;
	public String gender;
	public String country;
	public String city;
	public String province;
	public String language;
	
	
	
	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", nickname=" + nickname + ", avatarUrl=" + avatarUrl + ", gender=" + gender
				+ ", country=" + country + ", city=" + city + ", province=" + province + ", language=" + language + "]";
	}
	
	

}
