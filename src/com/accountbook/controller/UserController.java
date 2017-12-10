package com.accountbook.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.model.Message;
import com.accountbook.model.UserInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.IFriendService;
import com.accountbook.service.IMessageService;
import com.accountbook.service.ITokenService;
import com.accountbook.service.IUserService;
import com.accountbook.utils.FileUtils;
import com.accountbook.utils.ImageUtils;
import com.accountbook.utils.WxUtil;
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
    @RequestMapping("/get")
    public Object findUser(HttpServletRequest request,String userId){
		
		
		Result result=new Result();
		
		UserInfo userinfo=userService.findUser(userId);
		return result.put(Result.RESULT_OK, "查询用户信息成功!").put(userinfo);
	}
	
	
	/**
	 * 根据token查询用户的简要信息
	 */
	@ResponseBody
    @RequestMapping("/getSelfSimple")
    public Object findUser(HttpServletRequest request){
		String findId=request.getAttribute("userid").toString();
		
		
		Result result=new Result();
		
		UserInfo userinfo=userService.findUser(findId);
		
		return result.put(Result.RESULT_OK, "查询自己用户信息成功!")
				.put("id",userinfo.id)
				.put("icon",userinfo.icon)
				.put("name",userinfo.nickname);
	}
	
    
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
		String iconName = ImageUtils.download(userinfo.avatarUrl,findId);
		if(iconName!=null)
			userinfo.icon=iconName;
		
		userService.updateUser(userinfo);
		
		
		return result.put(Result.RESULT_OK, "更新用户信息成功");
		
	}
	
	/**
	 * 此接口获取token相关联id的二维码
	 */
	@SuppressWarnings("serial")
	@ResponseBody
	@RequestMapping("/qr")
	public Object qr(HttpServletRequest request,HttpServletResponse response,String userId){
//		String findId=request.getAttribute("userid").toString();
		
		
		//已经生成过的直接取
		UserInfo findUser = userService.findUser(userId);
		
		System.out.println("用户:"+findUser);
		
		if(findUser==null)
			return new Result(Result.RESULT_FAILD,"未查询到用户");
		
		if(findUser.qr!=null && !"".equals(findUser.qr))
			return new Result(Result.RESULT_OK,findUser.qr);
		
		
		//获取二维码start-------------------------------------------------------------------------------------------------------
		
		Result result=new Result();
		
		
		byte[] image=WxUtil.getQrImage("pages/index/index",new HashMap<String,String>(){
			{
				put("friendId",userId);
			}
		});
		
		try {
			String filePath=FileUtils.saveFile(image, userId);
			
			UserInfo user=new UserInfo();
			user.id=userId;
			user.qr=filePath;
			userService.updateUser(user);
			
			result.put(Result.RESULT_OK, filePath);
		} catch (Exception e) {
			result.put(Result.RESULT_FILE_SAVE_ERROR, "二维码文件存储失败!");
			e.printStackTrace();
		}
		
		
		return result;
		  
	}
	
	
	
	
	@ResponseBody
	@RequestMapping("/invite")
	public Object inviteUser(HttpServletRequest request,String code,String openid,String formId){
		String findId=request.getAttribute("userid").toString();
		
		Result result=new Result();
		
		if(findId.equals(openid))
			return result.put(Result.RESULT_COMMAND_INVALID, "我是自己的忠实粉丝！");
		
		//查询是否之前发起过请求
		if(msgService.isRepeatInvite(findId, openid))
			return result.put(Result.RESULT_COMMAND_INVALID, "重复的好友邀请");
		
		
		
		
		UserInfo me = userService.findUser(findId);
		
		
		
		//数据库中加入邀请信息
		Message msg=new Message();
		msg.fromId=findId;
		msg.toId=openid;
		msg.type=Message.MESSAGE_TYPE_INVITE_USER;
//		msg.content="hi~~"+he.nickname+",我是"+me.nickname+",我们一起记账吧^~^";
		msg.time=Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		msg.state=Message.STATUS_UNREAD;
		msgService.newMessage(msg);
		
		
		String sendResult = WxUtil.sendTemplateInviteMessage(openid, formId, me.nickname, msg.content);
		
		return result.put(Result.RESULT_OK, sendResult);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@ResponseBody
//	@RequestMapping("/search")
	@Deprecated
	//改用二维码加好友,不使用搜索方式了
	public Object searchByName(HttpServletRequest request,String nickname){
		String findId=request.getAttribute("userid").toString();
		
		Result result=new Result();
		
		System.out.println("UserController(搜索的用户昵称)："+nickname);
		
		List<UserInfo> searchUsers = userService.searchUser(nickname);
		
		for(UserInfo info:searchUsers)
			info.flag=friendService.isFriend(findId, info.id);
		
		return result.put("datas", searchUsers).put(Result.RESULT_OK, "查询成功!");
		
	}
}
