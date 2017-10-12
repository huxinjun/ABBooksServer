package com.accountbook.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import com.accountbook.modle.result.Result;
import com.accountbook.service.IAccountService;
import com.accountbook.utils.IDUtil;
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
	
	
	
	
	/**
	 * 记账
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor={Exception.class, RuntimeException.class})
	@ResponseBody
	@RequestMapping("/add")
    public Object newAccount(ServletRequest req,String content){
//		String findId=req.getAttribute("userid").toString();
		
		System.out.println("AccountController.newAccount");
		System.out.println(content);
		
		Account account = EasyJson.getJavaBean(content, Account.class);
		
		account.setId(IDUtil.generateNewId());
		account.setDateTimestamp(Timestamp.valueOf(account.getDate()+" 00:00:00"));
		account.setCreateTimestamp(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())));
		
		System.out.println(account);
		//记录账单
		accountService.addNewAccount(account);
		
		//记录成员
		for(Member member:account.getMembers()){
			member.setAccountId(account.getId());
			accountService.addMember(member);
		}
		
		if(account.getMembers().size()>1){
			AccountCalculator calculator=new AccountCalculator(account);
			try {
				List<PayResult> result = calculator.calc();
				System.out.println(result);
				//记录支付方案(目前只选择第一种方案)
				if(result!=null && result.size()>0 && result.get(0)!=null && result.get(0).getPayTarget()!=null)
					for(PayTarget target:result.get(0).getPayTarget()){
						target.setAccountId(account.getId());
						accountService.addPayTarget(target);
					}
						
				
			} catch (CalculatorException e) {
				e.printStackTrace();
			}
			
		}
		
		return new Result(Result.RESULT_OK, "记录账单成功!");
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
	
	
	
	
}
