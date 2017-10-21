package com.accountbook.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accountbook.core.AccountCalculator;
import com.accountbook.core.AccountCalculator.CalculatorException;
import com.accountbook.model.Account;
import com.accountbook.model.Member;
import com.accountbook.model.PayResult;
import com.accountbook.model.PayTarget;
import com.accountbook.model.UserInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.IAccountService;
import com.accountbook.service.IGroupService;
import com.accountbook.service.IUserService;
import com.accountbook.utils.CommonUtils;
import com.accountbook.utils.IDUtil;
import com.accountbook.utils.TextUtils;
import com.easyjson.EasyJson;

/**
 * 账单
 * @author xinjun
 * create 2017.9.25
 */
@Controller
@RequestMapping("/account")
public class AccountController {
	


	@Autowired
	IAccountService accountService;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	IGroupService groupService;
	
	
	/**
	 * 记账
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor={Exception.class, RuntimeException.class})
	@ResponseBody
	@RequestMapping("/add")
    public Object newAccount(ServletRequest req,String content){
		String findId=req.getAttribute("userid").toString();
		
		System.out.println("AccountController.newAccount");
		System.out.println(content);
		
		Account account = EasyJson.getJavaBean(content, Account.class);
		
		account.setId(IDUtil.generateNewId());
		account.setDateTimestamp(Timestamp.valueOf(account.getDate()+" 00:00:00"));
		account.setCreateTimestamp(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
		
		System.out.println(account);
		//记录账单
		accountService.addNewAccount(account);
		
		
		if(account.getMembers().size()>1){
			AccountCalculator calculator=new AccountCalculator(account);
			try {
				List<PayResult> result = calculator.calc();
				System.out.println(result);
				//记录支付方案(目前只选择第一种方案)
				if(result!=null && result.size()>0 && result.get(0)!=null && result.get(0).getPayTarget()!=null)
					for(PayTarget target:result.get(0).getPayTarget()){
						target.setAccountId(account.getId());
						target.setId(IDUtil.generateNewId());
						accountService.addPayTarget(target);
					}
						
				
			} catch (CalculatorException e) {
				e.printStackTrace();
			}
			
		}
		//只有一个成员而且是记录账单者自己时不需要在数据库中添加成员
		if(account.getMembers().size()==1 && account.getMembers().get(0).getMemberId().equals(findId))
			return new Result(Result.RESULT_OK, "记录账单成功!");
		//记录成员
		for(Member member:account.getMembers()){
			member.setId(IDUtil.generateNewId());
			member.setAccountId(account.getId());
			member.setParentMemberId(null);
			accountService.addMember(member);
			//添加组中的成员
			if(member.getIsGroup()){
				List<UserInfo> users = groupService.findUsersByGroupId(member.getMemberId());
				if(users!=null && users.size()>0){
					for (UserInfo user:users) {
						Member innerMember =new Member();
						innerMember.setId(IDUtil.generateNewId());
						innerMember.setAccountId(account.getId());
						innerMember.setMemberId(user.id);
						innerMember.setMemberName(user.nickname);
						innerMember.setMemberIcon(user.avatarUrl);
						innerMember.setParentMemberId(member.getId());
						accountService.addMember(innerMember);
					}
				}
			}
		}
		
		
		/**
		 * 最后一步重要的操作:自动完善组内的支付方案
		 * 需要遍历所有的PayTarget,如果PayTarget中的支付方或者收款方有一方是组或者是两个组
		 * 那么就需要对是组的成员做如下判断:
		 * 组中几个人?
		 * 没有人:不需要完善子账单(如果双方都是租,并且没有人,那么设置支付状态为已支付)
		 * 一个人:自动完善
		 * 两个或以上:手动完善
		 * 
		 * 支付者的状态(为组时有效)0:无需完善子账单,1:需要手动完善,2:已经完善了子账单
		 */
		try{
			ArrayList<PayTarget> targets = account.getPayResult().get(0).getPayTarget();
			for(PayTarget target:targets){
				boolean paidPersonIsGroup=isGroup(account.getMembers(), target.getPaidId());
				boolean receiptPersonIsGroup=isGroup(account.getMembers(), target.getReceiptId());
				int paidGroupPersonCount=paidPersonIsGroup?groupService.findUsersCountByGroupId(target.getPaidId()):-1;
				int receiptGroupPersonCount=receiptPersonIsGroup?groupService.findUsersCountByGroupId(target.getReceiptId()):-1;
				//-------------------------------------------------------------------------------
				//两个组都没人
				if(paidGroupPersonCount==0 && receiptGroupPersonCount==0){
					target.setPaidStatus(PayTarget.STATUS_NOT_NEED);
					target.setReceiptStatus(PayTarget.STATUS_NOT_NEED);
					target.setSettled(true);//标记为已付
				}
				//-------------------------------------------------------------------------------
				//支付组有人
				if(paidGroupPersonCount>1){
					//手动完善
					target.setPaidStatus(PayTarget.STATUS_NEED);
					accountService.updatePayTarget(target);
				}else if(paidGroupPersonCount==1){
					//自动完善,start
					UserInfo user = groupService.findUsersByGroupId(target.getPaidId()).get(0);
					PayTarget newTarget=new PayTarget();
					newTarget.setId(IDUtil.generateNewId());
					newTarget.setAccountId(account.getId());
					newTarget.setPaidId(user.id);
					newTarget.setReceiptId(target.getReceiptId());
					newTarget.setParentPayId(target.getId());
					newTarget.setMoney(target.getMoney());
					accountService.addPayTarget(newTarget);
					
					target.setPaidStatus(PayTarget.STATUS_SUCCESS);
					accountService.updatePayTarget(target);
				}
				//-------------------------------------------------------------------------------
				//收款组有人
				if(receiptGroupPersonCount>1){
					//手动完善
					target.setReceiptStatus(PayTarget.STATUS_NEED);
					accountService.updatePayTarget(target);
				}else if(receiptGroupPersonCount==1){
					//自动完善,start
					UserInfo user = groupService.findUsersByGroupId(target.getReceiptId()).get(0);
					PayTarget newTarget=new PayTarget();
					newTarget.setId(IDUtil.generateNewId());
					newTarget.setAccountId(account.getId());
					newTarget.setPaidId(target.getPaidId());
					newTarget.setReceiptId(user.id);
					newTarget.setParentPayId(target.getId());
					newTarget.setMoney(target.getMoney());
					accountService.addPayTarget(newTarget);
					
					target.setReceiptStatus(PayTarget.STATUS_SUCCESS);
					accountService.updatePayTarget(target);
				}
				
			}
			
		}catch(Exception ex){
			
		}
		
		
		
		return new Result(Result.RESULT_OK, "记录账单成功!");
	}
	
	/**
	 * 判断一个成员是不是个组
	 * @param members
	 * @param memberId
	 * @return
	 */
	private boolean isGroup(List<Member> members,String memberId){
		for(Member member:members)
			if(member.getMemberId().equals(memberId))
				if(member.getIsGroup())
					return true;
		return false;
	}
	
	
	
	
	/**
	 * 完善组内账单(成员与支付方案)
	 * @param accountId 父账单id
	 * @param membersJson 子账单成员与支付规则和金额的json字符串
	 */
	@ResponseBody
	@RequestMapping("/updateInnerAccount")
    public Object updateInnerAccount(ServletRequest req,String accountId,String membersJson){
		
		
		
		return null;
	}
	
	
	
	
	
	

	/**
	 * 查询帐友和所有相关的分组
	 */
	@ResponseBody
	@RequestMapping("/getAllMembers")
    public Object findMembers(ServletRequest req){
		String findId=req.getAttribute("userid").toString();
		
		return new Result(Result.RESULT_OK, "查询分组信息成功!").put("members", accountService.findAllMembers(findId));
	}
	
	
	/**
	 * 根据id查询账单
	 */
	@ResponseBody
	@RequestMapping("/get")
    public Object findAccount(ServletRequest req,String accountId){
		String findId=req.getAttribute("userid").toString();
		Result result = new Result();
		Account findAccount = accountService.findAccount(accountId);
		result.put(findAccount);
		result.remove("imgs");
		if(findAccount.getImgs()==null ||"".equals(findAccount.getImgs()))
			result.put("imgs", null);
		else
			result.put("imgs", findAccount.getImgs().split(","));
		
		
		//成员为组时查找当前用户是否在改组内
		if(findAccount.getMembers()!=null && findAccount.getMembers().size()>0){
			for(Member member:findAccount.getMembers()){
				if(member.getIsGroup()){
					boolean isMember=groupService.isGroupMember(findId, member.getMemberId());
					member.setIsMember(isMember);
				}
			}
		}
		
		return result.put(Result.RESULT_OK, "查询成功").put("date", new SimpleDateFormat("yyyy年MM月dd日").format(new Date(findAccount.getDateTimestamp().getTime())));
	}
	
	
	/**
	 * 查询一个用户的所有账单
	 */
	@ResponseBody
	@RequestMapping("/getAll")
    public Object findAccounts(ServletRequest req,String bookId){
//		String findId=req.getAttribute("userid").toString();
		String findId="oCBrx0FreB-L8pIQM5_RYDGoWOKQ";
		Result result = new Result();
		List<Account> results;
		if(TextUtils.isEmpty(bookId))
			results=accountService.findAccounts(findId);
		else
			results=accountService.findAccounts(findId,bookId);
		
		UserInfo findUser = userService.findUser(findId);
		
		
		//将字符串的icons替换为数组形式
		List<Result> resultsWapper = new ArrayList<>();
		for(Account account:results){
			Result put = new Result().put(account);
			put.remove("imgs");
			if(account.getImgs()==null ||"".equals(account.getImgs()))
				put.put("imgs", null);
			else
				put.put("imgs", account.getImgs().split(","));
			
			put.put("user_icon", findUser.icon);
			put.put("date", new SimpleDateFormat("yyyy年MM月dd日").format(new Date(account.getDateTimestamp().getTime())));
			put.put("dateDis", CommonUtils.getSinceTimeString(account.getCreateTimestamp()));
			
			
			//成员为组时查找当前用户是否在改组内
			if(account.getMembers()!=null && account.getMembers().size()>0){
				for(Member member:account.getMembers()){
					if(member.getIsGroup()){
						boolean isMember=groupService.isGroupMember(findId, member.getMemberId());
						member.setIsMember(isMember);
					}
				}
			}
			
			resultsWapper.add(put);
		}
		
		result.put("accounts",resultsWapper);
		
		
		
		return result.put(Result.RESULT_OK, "查询账单成功!");
	}
	
}
