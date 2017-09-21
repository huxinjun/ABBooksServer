package com.accountbook.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.globle.Constants;
import com.accountbook.modle.Message;
import com.accountbook.modle.UserInfo;
import com.accountbook.modle.WxAccessToken;
import com.accountbook.modle.WxTemplateInvite;
import com.accountbook.modle.WxTemplateInvite.KeyWord;
import com.accountbook.modle.result.ListResult;
import com.accountbook.modle.result.Result;
import com.accountbook.service.IFriendService;
import com.accountbook.service.IMessageService;
import com.accountbook.service.ITokenService;
import com.accountbook.service.IUserService;
import com.accountbook.utils.HttpUtils;
import com.accountbook.utils.ImageUtils;
import com.alibaba.fastjson.JSON;


@Controller
@RequestMapping("/user")
	
public class UserController {
	

	@Autowired
	IUserService userService;
	
	@Autowired
	ITokenService tokenService;
	
	@Autowired
	IMessageService msgService;
	
	@Autowired
	IFriendService friendService;
	
	
	
	
	
    
	@ResponseBody
    @RequestMapping("/updateInfo")
    public Object updateUserInfo(HttpServletRequest request,String info){
		String findId=request.getAttribute("userid").toString();
		
		
		Result result=new Result();
		
		UserInfo userinfo=JSON.parseObject(info, UserInfo.class);
		System.out.println(info);
		System.out.println(userinfo);
		
			
		userinfo.id=findId;
		
		//更新信息的时候下载微信头像到服务器
		String iconName = ImageUtils.download(userinfo.avatarUrl);
		if(iconName!=null)
			userinfo.icon=iconName;
		
		userService.updateUser(userinfo);
		
		
		result.status=0;
		result.msg="更新用户信息成功!";
		return result;
		
	}
	
	@RequestMapping("/qr")
	public void qrAddUser(HttpServletRequest request,HttpServletResponse response){
		
		String findId="oCBrx0FreB-L8pIQM5_RYDGoWOKQ";
		
		//获取二维码start-------------------------------------------------------------------------------------------------------
		//获取accesstoken
    	Map<String,String> params=new HashMap<>();
    	params.put("appid", Constants.APP_ID);
    	params.put("secret", Constants.APP_SECRET);
    	params.put("grant_type", "client_credential");
    	String accessUrl="https://api.weixin.qq.com/cgi-bin/token";
    	String info=HttpUtils.sendGet(accessUrl,params);
		
    	
		WxAccessToken accessToken=JSON.parseObject(info, WxAccessToken.class);
		System.out.println(accessToken);
		
		String url="https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token="+accessToken.access_token;
		byte[] result=HttpUtils.sendPost2(url,"{\"path\": \"pages/index/index?friendId="+findId+"\", \"width\": 430}");
		
		try {
			ImageUtils.send(result, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@ResponseBody
//	@RequestMapping("/search")
	@Deprecated
	//改用二维码加好友,不使用搜索方式了
	public Object searchByName(HttpServletRequest request,String nickname){
		String findId=request.getAttribute("userid").toString();
		
		ListResult result=new ListResult();
		
		System.out.println("UserController(搜索的用户昵称)："+nickname);
		
		List<UserInfo> searchUsers = userService.searchUser(nickname);
		
		for(UserInfo info:searchUsers)
			info.flag=friendService.isFriend(findId, info.id);
		
		result.datas=searchUsers;
		
		result.status=Result.RESULT_OK;
		result.msg="查询成功!";
		return result;
		
	}
	
	@ResponseBody
	@RequestMapping("/invite")
	public Object inviteUser(HttpServletRequest request,String code,String openid,String formId){
		String findId=request.getAttribute("userid").toString();
		
		Result result=new Result();
		
		
		UserInfo me = userService.findUser(findId);
		UserInfo he = userService.findUser(openid);
		
		
		
		//数据库中加入邀请信息
		Message msg=new Message();
		msg.inviteId=me.id;
		msg.acceptId=openid;
		msg.type=Message.MESSAGE_TYPE_INVITE_USER;
		msg.content="hi~~"+he.nickname+",我是"+me.nickname+",我们一起记账吧^~^";
		msg.timeMiles=System.currentTimeMillis();
		msg.status=Message.STATUS_UNREAD;
		msgService.newMessage(msg);
		
		
		//发送模板消息start-------------------------------------------------------------------------------------------------------
		//获取accesstoken
    	Map<String,String> params=new HashMap<>();
    	params.put("appid", Constants.APP_ID);
    	params.put("secret", Constants.APP_SECRET);
    	params.put("grant_type", "client_credential");
    	String accessUrl="https://api.weixin.qq.com/cgi-bin/token";
    	String info=HttpUtils.sendGet(accessUrl,params);
		
    	
		WxAccessToken accessToken=JSON.parseObject(info, WxAccessToken.class);
		System.out.println(accessToken);
		
		String url="https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+accessToken.access_token;
		
		WxTemplateInvite invite=new WxTemplateInvite();
		invite.touser=openid;
		invite.template_id="r63a82Qy_kap-4SxZUT9SfwS5004qb-l_i17zzUOqY4";
		invite.page="pages/msg_invite/msg_invite";
		invite.form_id=formId;
		List<KeyWord> data=new ArrayList<>();
		data.add(new KeyWord(me.nickname,"#173177"));
		data.add(new KeyWord("赶紧进入小账本本!!!呲呲的^_^","#173177"));
		invite.data=data;
		
		System.out.println(invite.toString());
		
		String str=HttpUtils.sendPost(url,invite.toString());
		//发送模板消息end-------------------------------------------------------------------------------------------------------
		
		result.status=Result.RESULT_OK;
		result.msg=str;
		return result;
		
	}
}
