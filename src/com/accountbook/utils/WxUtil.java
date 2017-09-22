package com.accountbook.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.accountbook.globle.Constants;
import com.accountbook.modle.WxAccessToken;
import com.accountbook.modle.WxTemplateInvite;
import com.accountbook.modle.WxTemplateInvite.KeyWord;
import com.alibaba.fastjson.JSON;

/**
 * 和微信相关的工具类
 * @author xinjun
 *
 */
public class WxUtil {

	
	
	/**
	 * 获取Accesstoken
	 * @return
	 */
	public static String getAccesstoken(){
		//获取accesstoken
    	Map<String,String> params=new HashMap<>();
    	params.put("appid", Constants.APP_ID);
    	params.put("secret", Constants.APP_SECRET);
    	params.put("grant_type", "client_credential");
    	String accessUrl="https://api.weixin.qq.com/cgi-bin/token";
    	String info=HttpUtils.sendGet(accessUrl,params);
		
    	
		WxAccessToken accessToken=JSON.parseObject(info, WxAccessToken.class);
		System.out.println("wx.accessToken:"+accessToken.access_token);
		return accessToken.access_token;
	}
	
	
	
	/**
	 * 获取二维码
	 * @return
	 */
	public static byte[] getQrImage(String pagePath,Map<String,String> params){
		String url="https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token="+getAccesstoken();
		
		StringBuilder sb=new StringBuilder();
		sb.append("{\"path\": \"");
		sb.append(pagePath);
		
		if(params!=null && params.size()>0){
			sb.append("?");
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			while(iterator.hasNext()){
				Entry<String, String> next = iterator.next();
				sb.append(next.getKey());
				sb.append("=");
				sb.append(next.getValue());
				sb.append("&");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("\",\"width\": 500}");
		
		System.out.println(sb.toString());
		
		byte[] rawData = HttpUtils.sendPost2(url,sb.toString());
		
		return rawData;
		
	}
	
	
	
	public static String sendTemplateInviteMessage(String openid,String formid,String inviteName,String content){
		//发送模板消息start-------------------------------------------------------------------------------------------------------
		String url="https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+getAccesstoken();
		
		WxTemplateInvite invite=new WxTemplateInvite();
		invite.touser=openid;
		invite.template_id="r63a82Qy_kap-4SxZUT9SfwS5004qb-l_i17zzUOqY4";
		invite.page="pages/msg_invite/msg_invite";
		invite.form_id=formid;
		List<KeyWord> data=new ArrayList<>();
		data.add(new KeyWord(inviteName,"#173177"));
		data.add(new KeyWord("赶紧进入小账本本!!!呲呲的^_^","#173177"));
		invite.data=data;
		
		System.out.println(invite.toString());
		
		String result=HttpUtils.sendPost(url,invite.toString());
		
		//发送模板消息end-------------------------------------------------------------------------------------------------------
		return result;
	}
	
	
	
	
	
	
	
	@SuppressWarnings("serial")
	public static void main(String[] args){
		getQrImage("pages/index/index",new HashMap<String,String>(){
			{
				put("friendId","asasasa");
				put("groupId","123456");
			}
		});
	}
	
	
	
	
}
