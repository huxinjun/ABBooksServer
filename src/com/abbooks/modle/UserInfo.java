package com.abbooks.modle;

public class UserInfo {
	
	public String id;
	public String nickName;
	public String avatarUrl;
	public String gender;
	public String country;
	public String city;
	public String province;
	public String language;
	
	
	
	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", nickName=" + nickName + ", avatarUrl=" + avatarUrl + ", gender=" + gender
				+ ", country=" + country + ", city=" + city + ", province=" + province + ", language=" + language + "]";
	}
	
	

}
