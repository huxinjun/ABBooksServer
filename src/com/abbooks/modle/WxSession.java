package com.abbooks.modle;

public class WxSession {

	public String openid;
	public String session_key;
	public int expires_in;
	@Override
	public String toString() {
		return "WxLoginInfo [openid=" + openid + ", session_key=" + session_key + ", expires_in=" + expires_in
				+ "]";
	}
}
