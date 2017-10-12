package com.accountbook.model;

public class WxAccessToken {
	
	//{"access_token":"QgnlB9a7NQSfn42Ntxr4IwH8kDYZM7hRTYSyMgxsJyq1VQT8JqrS-iURsyEdTZhZX2EeB9zL-jQfNiawRVrRUFRJMIpAGApD29ocRuLRXMI","expires_in":7200,"refresh_token":"LNCt2BWmQrmhwuHpfBTCClxd8nw8KiPYdmNoxfkH2Vj571d8P1O7Vxbtmf0Fp8Pj4pdP1pTzNKPS3wR1j1uc82kpZ056p1ftLZ9Aij7H4os","openid":"oCBrx0FreB-L8pIQM5_RYDGoWOKQ","scope":"snsapi_base"}


	public String access_token;
	public String refresh_token;
	public String openid;
	public String scope;
	
	public int errcode;
	public String errmsg;
	public int expires_in;
	@Override
	public String toString() {
		return "WxAccessToken [access_token=" + access_token + ", refresh_token=" + refresh_token + ", openid=" + openid
				+ ", scope=" + scope + ", errcode=" + errcode + ", errmsg=" + errmsg + ", expires_in=" + expires_in
				+ "]";
	}
	
	
}
