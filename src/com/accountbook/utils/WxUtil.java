package com.accountbook.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.accountbook.globle.Constants;
import com.accountbook.model.Account;
import com.accountbook.model.Form;
import com.accountbook.model.Member;
import com.accountbook.model.UserInfo;
import com.accountbook.model.WxAccessToken;
import com.accountbook.model.WxTemplate;
import com.accountbook.model.WxTemplate.KeyWord;
import com.accountbook.service.IGroupService;
import com.accountbook.service.INotifService;
import com.accountbook.service.IUserService;
import com.alibaba.fastjson.JSON;

/**
 * 和微信相关的工具类
 * @author xinjun
 *
 */
public class WxUtil {

	private static final String TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token";
	private static final String TEMPLETE_URL="https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=";
	private static final String QR_IMAGE_URL="https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=";
	
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
    	String info=HttpUtils.sendGet(TOKEN_URL,params);
		
    	
		WxAccessToken accessToken=JSON.parseObject(info, WxAccessToken.class);
		System.out.println("wx.accessToken:"+accessToken.access_token);
		return accessToken.access_token;
	}
	
	/**
	 * 获取发送模板的接口地址
	 * @return
	 */
	public static String getTempleteUrl(){
		return TEMPLETE_URL+getAccesstoken();
	}
	/**
	 * 获取发送模板的接口地址
	 * @return
	 */
	public static String getQrImageUrl(){
		return QR_IMAGE_URL+getAccesstoken();
	}
	
	
	
	/**
	 * 获取二维码
	 * @return
	 */
	public static byte[] getQrImage(String pagePath,Map<String,String> params){
		
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
		
		System.out.println("WxUtil.getQrImage:"+sb.toString());
		
		byte[] rawData = HttpUtils.sendPost2(getQrImageUrl(),sb.toString());
//		FileUtils.saveFile(rawData, new File("C:\\Users\\XINJUN\\Desktop\\WORK\\temp"));
		return rawData;
		
	}
	
	
	/**
	 * 邀请好友模板，向被邀请者发送
	 */
	public static String sendTemplateInviteMessage(INotifService service,String friendId,String inviteName,String content,boolean isGroup,String groupName){
		//发送模板消息start-------------------------------------------------------------------------------------------------------
		//从数据库获取formid
		if(!service.isNotifOpen(friendId)){
			return "接受消息的用户未开启微信服务提醒:"+friendId;
		}
		Form form=service.getOne(friendId);
		if(form==null){
			return "接受消息的用户没有可用表单ID:"+friendId;
		}
		
		System.out.println("!!!sendTemplateInviteMessage:"+form);
		//查询到可以使用的formId了,首先删除数据库中的记录
		service.delete(form.id);
		String formId=form.formId;
			
		
		WxTemplate invite=new WxTemplate();
		invite.touser=friendId;
		invite.template_id="r63a82Qy_kap-4SxZUT9SfwS5004qb-l_i17zzUOqY4";
		invite.page="pages/msg_invite/msg_invite";
		invite.form_id=formId;
		List<KeyWord> data=new ArrayList<>();
		/*
		发送人
		{{keyword1.DATA}}
		申请信息
		{{keyword2.DATA}}
		 */
		data.add(new KeyWord(inviteName,"#173177"));
		data.add(new KeyWord(isGroup?"我想加入["+groupName+"]":"赶紧进入小账本本!!!呲呲的^_^","#173177"));
		invite.data=data;
		
		System.out.println(invite.toString());
		
		String result=HttpUtils.sendPost(getTempleteUrl(),invite.toString());
		
		//发送模板消息end-------------------------------------------------------------------------------------------------------
		return result;
	}
	
	
	
	/**
	 * 加帐友或者进组模板
	 * 如果是组的话向组内成员发送
	 * 如果是加帐友的话向邀请者发送
	 */
	public static String sendTemplateInviteResultMessage(INotifService service,String openid,String name,boolean success,Date date,boolean isGroup,String groupId){
		//发送模板消息start-------------------------------------------------------------------------------------------------------
		//从数据库获取formid
		if(!service.isNotifOpen(openid)){
			return "接受消息的用户未开启微信服务提醒:"+openid;
		}
		Form form=service.getOne(openid);
		if(form==null){
			return "接受消息的用户没有可用表单ID:"+openid;
		}
		//查询到可以使用的formId了,首先删除数据库中的记录
		service.delete(form.id);
		String formId=form.formId;
		
		
		WxTemplate invite=new WxTemplate();
		invite.touser=openid;
		invite.template_id="LI1orCp7eFVDmek1jCh15b48nx9wJDiF8QI7NmKQNcA";
		invite.page=isGroup?"pages/group_edit/group_edit?groupId="+encode(groupId):"pages/friend/friend";
		invite.form_id=formId;
		List<KeyWord> data=new ArrayList<>();
		/*
		姓名
		{{keyword1.DATA}}
		申请结果
		{{keyword2.DATA}}
		申请时间
		{{keyword3.DATA}}
		申请类型
		{{keyword4.DATA}}
		 */
		data.add(new KeyWord(name,"#173177"));
		data.add(new KeyWord(success?"接受":"拒绝","#ff0000"));
		SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		data.add(new KeyWord(format.format(date),"#173177"));
		data.add(new KeyWord(isGroup?"加入分组":"添加帐友","#173177"));
		invite.data=data;
		
		System.out.println(invite.toString());
		
		String result=HttpUtils.sendPost(getTempleteUrl(),invite.toString());
		
		//发送模板消息end-------------------------------------------------------------------------------------------------------
		return result;
	}
	
	private static String encode(String str){
		return str.replaceAll("=", "!XJ!");
	}
	
	
	
	
	/**
	 * 新账单模板，向所有user(非组)发送
	 */
	public static String sendTemplateNewAccountMessage(INotifService notifService,IUserService userService,IGroupService groupService,Account account){
		StringBuffer resultSb=new StringBuffer();
		
		ArrayList<Member> members = account.getMembers();
		UserInfo createAccUser = userService.findUser(account.getUserId());
		for(Member member:members){
			if(member.getIsGroup()){
				List<UserInfo> usersByGroupId = groupService.findUsersByGroupId(member.getMemberId());
				if(usersByGroupId==null || usersByGroupId.size()==0)
					continue;
				for(UserInfo user:usersByGroupId)
					if(!user.id.equals(createAccUser.id))
						resultSb.append(sendTemplateNewAccountMessage(
								notifService,
								user.id,
								account.getId(),
								createAccUser.nickname,
								new SimpleDateFormat("yyyy年MM月dd日").format(new Date(account.getDateTimestamp().getTime())),
								account.getPaidIn(),
								members.size()
								));
			}
		}
		
		return resultSb.toString();
	}
	
	/**
	 * 新账单模板，向某个user发送
	 */
	public static String sendTemplateNewAccountMessage(INotifService service,String friendId,String accountId,String createUserName,String date,float money,int memberCount){
		//发送模板消息start-------------------------------------------------------------------------------------------------------
		//从数据库获取formid
		if(!service.isNotifOpen(friendId)){
			return "接受消息的用户未开启微信服务提醒:"+friendId;
		}
		Form form=service.getOne(friendId);
		if(form==null){
			return "接受消息的用户没有可用表单ID:"+friendId;
		}
		
		System.out.println("!!!sendTemplateInviteMessage:"+form);
		//查询到可以使用的formId了,首先删除数据库中的记录
		service.delete(form.id);
		String formId=form.formId;
			
		
		WxTemplate invite=new WxTemplate();
		invite.touser=friendId;
		invite.template_id="naRxFwdCcjNs3p-rQS2XF6RkjkwVL0XpstnxEEsVPxg";
		invite.page="pages/account/account?accountId="+encode(accountId);
		invite.form_id=formId;
		List<KeyWord> data=new ArrayList<>();
		/*
		 累计消费
		{{keyword1.DATA}}
		付费人数
		{{keyword2.DATA}}
		创建日期
		{{keyword3.DATA}}
		备注
		{{keyword4.DATA}}
		*/
		data.add(new KeyWord(String.valueOf(money),"#00CD66"));
		data.add(new KeyWord(memberCount+"位成员","#173177"));
		data.add(new KeyWord(date,"#173177"));
		data.add(new KeyWord(createUserName,"#173177"));
		invite.data=data;
		
		System.out.println(invite.toString());
		
		String result=HttpUtils.sendPost(getTempleteUrl(),invite.toString());
		
		//发送模板消息end-------------------------------------------------------------------------------------------------------
		return result;
	}
	
	
	
	
	
	
	
	@SuppressWarnings("serial")
	public static void main(String[] args){
		getQrImage("pages/index/index",new HashMap<String,String>(){
			{
				put("friendId","oCBrx0FreB-L8pIQM5_RYDGoWOKQ");
				put("groupId","123456");
			}
		});
	}
	
	
	
	
}
