package com.abbooks.modle;

public class TokenInfo {
	
	public String id;
	public String token;
	public long expireTime;
	@Override
	public String toString() {
		return "TokenInfo [id=" + id + ", token=" + token + ", expireTime=" + expireTime + "]";
	}
	
	

}
