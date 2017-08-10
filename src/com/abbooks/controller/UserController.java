package com.abbooks.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.mapping.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abbooks.globle.Constants;
import com.abbooks.modle.TestModel;
import com.abbooks.modle.UserInfo;
import com.abbooks.modle.WxAccessToken;
import com.abbooks.modle.WxTemplateInvite;
import com.abbooks.modle.WxTemplateInvite.KeyWord;
import com.abbooks.modle.result.Result;
import com.abbooks.modle.result.TestAllResult;
import com.abbooks.modle.result.UserSearchResult;
import com.abbooks.service.ITestService;
import com.abbooks.service.ITokenService;
import com.abbooks.service.IUserService;
import com.abbooks.utils.HttpUtils;
import com.alibaba.fastjson.JSON;


@Controller
@RequestMapping("/user")
	
public class UserController {
	

	@Autowired
	IUserService userService;
	
	@Autowired
	ITokenService tokenService;
	
	
    
	@ResponseBody
    @RequestMapping("/updateInfo")
    public Result updateUserInfo(String token,String info){
		Result result=new Result();
		
		UserInfo userinfo=JSON.parseObject(info, UserInfo.class);
		System.out.println(info);
		System.out.println(userinfo);
		
		String id = tokenService.getId(token);
		if(id==null){
			result.status=1;
			result.msg="token无效";
			return result;
		}
			
		userinfo.id=id;
		
		userService.updateUser(userinfo);
		
		
		result.status=0;
		result.msg="更新用户信息成功!";
		return result;
		
	}
	
	@ResponseBody
	@RequestMapping("/search")
	public Result searchByName(String token,String nickname){
		UserSearchResult result=new UserSearchResult();
		
		System.out.println(token);
		System.out.println(nickname);
		
		String id = tokenService.getId(token);
		System.out.println(id);
		if(id==null){
			result.status=1;
			result.msg="token无效";
			return result;
		}
		
		
		result.users = userService.searchUser(nickname);
		
		
		result.status=0;
		result.msg="查询成功!";
		return result;
		
	}
	
	@ResponseBody
	@RequestMapping("/invite")
	public Result inviteUser(String token,String code,String openid,String formId){
		Result result=new Result();
		String id = tokenService.getId(token);
		System.out.println(id);
		if(id==null){
			result.status=1;
			result.msg="token无效";
			return result;
		}
		UserInfo me = userService.findUser(id);
		
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
		invite.page="pages/index/index";
		invite.form_id=formId;
		List<KeyWord> data=new ArrayList<>();
		data.add(new KeyWord(me.nickname,"#173177"));
		data.add(new KeyWord("赶紧加入小账本本!!!呲呲的^_^","#173177"));
		invite.data=data;
		
		System.out.println(invite.toString());
		
		String str=HttpUtils.sendPost(url,invite.toString());
		
		result.status=0;
		result.msg=str;
		return result;
		
	}
}
