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
import com.accountbook.model.Message;
import com.accountbook.model.Offset;
import com.accountbook.model.PayOffset;
import com.accountbook.model.PayResult;
import com.accountbook.model.PayTarget;
import com.accountbook.model.SummaryInfo;
import com.accountbook.model.UserInfo;
import com.accountbook.modle.result.Result;
import com.accountbook.service.IAccountService;
import com.accountbook.service.IGroupService;
import com.accountbook.service.IMessageService;
import com.accountbook.service.IUserService;
import com.accountbook.utils.CommonUtils;
import com.accountbook.utils.IDUtil;
import com.accountbook.utils.TextUtils;
import com.easyjson.EasyJson;

/**
 * 账单
 * 
 * @author xinjun create 2017.9.25
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
	
	@Autowired
	IMessageService msgService;

	/**
	 * 记账
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class, RuntimeException.class })
	@ResponseBody
	@RequestMapping("/add")
	public Object newAccount(ServletRequest req, String content) {
		String findId = req.getAttribute("userid").toString();

		System.out.println("AccountController.newAccount");
		System.out.println(content);

		Account account = EasyJson.getJavaBean(content, Account.class);
		System.out.println("parse account:"+account);
		//如果是借款账单,需要处理借款人的规则
		if(account.getType()==9){
			for(Member member:account.getMembers())
				if(member.getPaidIn()==0){
					member.setRuleType(Member.RULE_TYPE_NUMBER);
					member.setRuleNum(account.getPaidIn());
				}
					
		}
		
		
		// 检查成员是否重复
		List<Member> allMembers = new ArrayList<>();
		List<Member> allUsers = new ArrayList<>();
		for (Member member : account.getMembers()) {
			if (isContains(allMembers, member.getMemberId()))
				return new Result(Result.RESULT_FAILD, "有重复的成员!");
			else
				allMembers.add(member);
			// 添加组中的成员
			if (member.getIsGroup()) {
				List<UserInfo> users = groupService.findUsersByGroupId(member.getMemberId());
				if (users != null && users.size() > 0) {
					for (UserInfo user : users) {
						allUsers.add(CommonUtils.userToMember(user));
						Member innerMember = new Member();
						innerMember.setMemberId(user.id);
						if (isContains(allMembers, innerMember.getMemberId()))
							return new Result(Result.RESULT_FAILD, "有重复的成员!");
						else
							allMembers.add(innerMember);
					}
				}
			}else
				allUsers.add(member);
		}

		account.setId(IDUtil.generateNewId());
		account.setDateTimestamp(Timestamp.valueOf(account.getDate() + " 00:00:00"));
		account.setCreateTimestamp(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));

		
		
		
		
		// 只有一个成员而且是记录账单者自己时不需要在数据库中添加成员
		if (account.getMembers().size() == 1 && account.getMembers().get(0).getMemberId().equals(findId)){
			account.setIsPrivate(true);
			accountService.addNewAccount(account);
			return new Result(Result.RESULT_OK, "记录账单成功!");
		}
		//多人账单
		accountService.addNewAccount(account);
		if (account.getMembers().size() > 1) {
			AccountCalculator calculator = new AccountCalculator(account);
			try {
				List<PayResult> result = calculator.calc(0);
				System.out.println(result);
				// 记录支付方案(目前只选择第一种方案)
				if (result != null && result.size() > 0 && result.get(0) != null
						&& result.get(0).getPayTarget() != null)
					for (PayTarget target : result.get(0).getPayTarget()) {
						target.setAccountId(account.getId());
						target.setId(IDUtil.generateNewId());
						//初始值为全款未付
						target.setWaitPaidMoney(target.getMoney());
						accountService.addPayTarget(target);
					}

			} catch (CalculatorException e) {
				e.printStackTrace();
			}

		}
		

		// 记录成员
		for (Member member : account.getMembers()) {
			member.setId(IDUtil.generateNewId());
			member.setAccountId(account.getId());
			member.setParentMemberId(null);
			accountService.addMember(member);
		}

		/**
		 * 最后一步重要的操作:自动完善组内的支付方案
		 * 需要遍历所有的PayTarget,如果PayTarget中的支付方或者收款方有一方是组或者是两个组 那么就需要对是组的成员做如下判断:
		 * 组中几个人? 没有人:不需要完善子账单(如果双方都是租,并且没有人,那么设置支付状态为已支付) 一个人:自动完善 两个或以上:手动完善
		 * 
		 * 支付者的状态(为组时有效)0:无需完善子账单,1:需要手动完善,2:已经完善了子账单
		 */
		try {
			ArrayList<PayTarget> targets = account.getPayResult().get(0).getPayTarget();
			for (PayTarget target : targets) {
				boolean paidPersonIsGroup = isGroup(account.getMembers(), target.getPaidId());
				boolean receiptPersonIsGroup = isGroup(account.getMembers(), target.getReceiptId());
				int paidGroupPersonCount = paidPersonIsGroup ? groupService.findUsersCountByGroupId(target.getPaidId())
						: -1;
				int receiptGroupPersonCount = receiptPersonIsGroup
						? groupService.findUsersCountByGroupId(target.getReceiptId()) : -1;
				
				// -------------------------------------------------------------------------------
				// 两个组都没人
				if (paidGroupPersonCount == 0 && receiptGroupPersonCount == 0) {
					target.setPaidStatus(PayTarget.STATUS_NOT_NEED);
					target.setReceiptStatus(PayTarget.STATUS_NOT_NEED);
					target.setWaitPaidMoney(0);// 标记为已付
				}
				// -------------------------------------------------------------------------------
				// 支付组有人
				if (paidGroupPersonCount > 1) {
					// 手动完善
					target.setPaidStatus(PayTarget.STATUS_NEED);
					accountService.updatePayTarget(target);
				} else if (paidGroupPersonCount == 1) {
					// 自动完善,start
					UserInfo user = groupService.findUsersByGroupId(target.getPaidId()).get(0);
					PayTarget newTarget = new PayTarget();
					newTarget.setId(IDUtil.generateNewId());
					newTarget.setAccountId(account.getId());
					newTarget.setPaidId(user.id);
					newTarget.setReceiptId(target.getReceiptId());
					newTarget.setMoney(target.getMoney());
					accountService.addPayTarget(newTarget);

					accountService.deletePayTarget(target.getId());
				}
				// -------------------------------------------------------------------------------
				// 收款组有人
				if (receiptGroupPersonCount > 1) {
					// 手动完善
					target.setReceiptStatus(PayTarget.STATUS_NEED);
					accountService.updatePayTarget(target);
				} else if (receiptGroupPersonCount == 1) {
					// 自动完善,start
					UserInfo user = groupService.findUsersByGroupId(target.getReceiptId()).get(0);
					PayTarget newTarget = new PayTarget();
					newTarget.setId(IDUtil.generateNewId());
					newTarget.setAccountId(account.getId());
					newTarget.setPaidId(target.getPaidId());
					newTarget.setReceiptId(user.id);
					newTarget.setMoney(target.getMoney());
					accountService.addPayTarget(newTarget);

					accountService.deletePayTarget(target.getId());
				}

			}

		} catch (Exception ex) {

		}
		//重要操作:抵消账单
		offsetTarget(account.getId());
		
		
		
		//最后给所有的用户发送消息
		for(Member user:allUsers)
			if(!user.getMemberId().equals(findId))
				msgService.newMessage(Message.MESSAGE_TYPE_ACCOUNT, findId, user.getMemberId(), "[Create]:"+account.getId());

		return new Result(Result.RESULT_OK, "记录账单成功!");
	}
	
	/**
	 * 支付方案的抵消操作,比如有两笔账
	 * 第一笔A向B支付10元
	 * 第二笔B向A支付5元
	 * 那么第二笔可以抵消第一笔中的5元
	 * 结果:A向B支付5元
	 * @param accountId
	 */
	private void offsetTarget(String accountId){
		Account findAccount = accountService.findAccount(accountId);
		/**
		 * 1.遍历所有Paytarget(遍历Paytarget),找支付者和收款者非组的
		 * 2.查询以往记录中支付者需要给收款者支付的钱
		 * 		如果大于等于0:无法抵消账单,因为以往都是欠款
		 * 		如果小于0:说明收款者需要向支付者掏钱,进入抵消账单的流程
		 * 3.抵消账单:
		 * 		3.1 查询时间最远的一笔waitPaidMoney大于0并且支付者与付款者与上面位置相反的PayTarget(查询Paytarget)
		 * 		3.2 查看查出的PayTarget的waitPaidMoney是否大于遍历Paytarget的money
		 * 			如果大于:说明本次的收款者向支付者在以往欠了比本次支付者更多的钱
		 * 					生成一个抵消记录类,将遍历Paytarget的waitPaidMoney置为0
		 * 					break跳出循环!!!
		 * 			如果小于:说明本次的收款者向支付者在以往欠的钱不足以完全抵消,
		 * 					那么:生成一个抵消记录类,然后设置查询Paytarget的waitPaidMoney为0
		 * 					goto3.1
		 * 		
		 */
		if(findAccount.getPayResult()==null || findAccount.getPayResult().size()==0
				|| findAccount.getPayResult().get(0).getPayTarget()==null
				|| findAccount.getPayResult().get(0).getPayTarget().size()==0)
			return;
		ArrayList<PayTarget> findPayTargets = findAccount.getPayResult().get(0).getPayTarget();
		System.out.println("抵消findPayTargets.size():"+findPayTargets.size());
		for(PayTarget target:findPayTargets){
			System.out.println("抵消target:"+target);
			if(isNotGroup(findAccount.getMembers(), target.getPaidId())
					&& isNotGroup(findAccount.getMembers(), target.getReceiptId())){
				double waitPaidMoney = accountService.getWaitPaidMoney(target.getPaidId(), target.getReceiptId(),target.getId());
				System.out.println("抵消waitPaidMoney:"+waitPaidMoney);
				if(waitPaidMoney<0){
					while(true){
						//符合抵消条件(以往账单中本次的收款者欠了支付者的钱)
						PayTarget notSettledTarget = accountService.findEarliestNotSettledTarget(target.getReceiptId(), target.getPaidId(),target.getId());
						System.out.println("抵消notSettledTarget:"+notSettledTarget);
						if(notSettledTarget==null)
							break;
						if(notSettledTarget.getWaitPaidMoney()>=target.getWaitPaidMoney()){
							//足以抵消
							System.out.println("足以抵消");
							//记录抵消信息到数据库
							PayOffset offset=new PayOffset();
							offset.originPayId=target.getId();
							offset.targetPayId=notSettledTarget.getId();
							offset.money=target.getWaitPaidMoney();
							accountService.addPayOffset(offset);
							
							target.setWaitPaidMoney(0);
							notSettledTarget.setWaitPaidMoney(notSettledTarget.getWaitPaidMoney()-offset.money);//抵消的部分需要扣除
							target.setOffsetCount(target.getOffsetCount()+1);
							notSettledTarget.setOffsetCount(notSettledTarget.getOffsetCount()+1);
							
							accountService.updatePayTarget(target);
							accountService.updatePayTarget(notSettledTarget);
							break;
							
						}else{
							//不够抵消
							System.out.println("不够抵消");
							
							//记录抵消信息到数据库
							PayOffset offset=new PayOffset();
							offset.originPayId=target.getId();
							offset.targetPayId=notSettledTarget.getId();
							offset.money=notSettledTarget.getWaitPaidMoney();
							accountService.addPayOffset(offset);
							
							notSettledTarget.setWaitPaidMoney(0);
							target.setWaitPaidMoney(target.getWaitPaidMoney()-offset.money);//抵消的部分需要扣除
							target.setOffsetCount(target.getOffsetCount()+1);
							notSettledTarget.setOffsetCount(notSettledTarget.getOffsetCount()+1);
							
							accountService.updatePayTarget(notSettledTarget);
							accountService.updatePayTarget(target);
						}
						
					}
					
				}
			}
		}
		
	}

	/**
	 * 判断一个成员是不是个组
	 * 
	 * @param members
	 * @param memberId
	 * @return
	 */
	private boolean isGroup(List<Member> members, String memberId) {
		for (Member member : members)
			if (member.getMemberId().equals(memberId))
				if (member.getIsGroup())
					return true;
		return false;
	}
	/**
	 * 判断一个成员是不是个组
	 * 
	 * @param members
	 * @param memberId
	 * @return
	 */
	private boolean isNotGroup(List<Member> members, String memberId) {
		for (Member member : members)
			if (member.getMemberId().equals(memberId))
				if (member.getIsGroup())
					return false;
		return true;
	}

	/**
	 * 判断一个成员是不是在组中
	 * 
	 * @param members
	 * @param memberId
	 * @return
	 */
	private boolean isContains(List<Member> members, String memberId) {
		for (Member member : members)
			if (member.getMemberId().equals(memberId))
				return true;
		return false;
	}
	
	private Member getMemberById(List<Member> members, String memberId) {
		for (Member member : members)
			if (member.getMemberId().equals(memberId))
				return member;
		return null;
	}
	
	private PayTarget getTargetById(List<PayTarget> targets, String targetId) {
		for (PayTarget target : targets)
			if (target.getId().equals(targetId))
				return target;
		return null;
	}

	/**
	 * 完善组内账单(成员与支付方案)
	 * 
	 * @param accountId
	 *            父账单id
	 * @param membersJson
	 *            子账单成员与支付规则和金额的json字符串
	 * @throws CalculatorException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class, RuntimeException.class })
	@ResponseBody
	@RequestMapping("/updateInnerAccount")
	public Object updateInnerAccount(ServletRequest req, String accountId, String memberId,String targetId, String membersJson) {
		String findId = req.getAttribute("userid").toString();
		Account findAccount = accountService.findAccount(accountId);
		Account account = EasyJson.getJavaBean("{\"members\":" + membersJson + "}", Account.class);
		account.setId(accountId);
		// 0.记录旧的支付方案中的支付状态
		List<PayResult> oldResult = findAccount.getPayResult();
		List<String> settleMembers = new ArrayList<>();// 这里面记录的成员id都是和将要完善账单的组已经结清的,之后这个里面的id和我们组分解出来的成员之间也都是结清的状态
		if (oldResult != null && oldResult.size() > 0 && oldResult.get(0) != null && oldResult.get(0).getPayTarget() != null)
			for (PayTarget target : oldResult.get(0).getPayTarget()) {
				if (target.getWaitPaidMoney()==0) {
					if (target.getPaidId().equals(memberId))
						settleMembers.add(target.getReceiptId());
					else
						settleMembers.add(target.getPaidId());

				}
			}
		// 1.找出那个组成员
		Member groupMember = null;
		for (Member member : findAccount.getMembers())
			if (member.getMemberId().equals(memberId)) {
				groupMember = member;
				break;
			}
		
		List<UserInfo> allGroupUsers=groupService.findUsersByGroupId(groupMember.getMemberId());
		//最后给组内所有的其他用户发送消息
		for(UserInfo user:allGroupUsers)
			if(!user.id.equals(findId))
				msgService.newMessage(Message.MESSAGE_TYPE_ACCOUNT, findId, user.id, "[CreateInner]:"+accountId+":"+memberId);
		
		// 2.看这个组需要付款还是需要收款,收款和付款完善账单时的逻辑不一样
		boolean groupNeedPay=(groupMember.getPaidIn()-groupMember.getShouldPay())<0;
		try
		{
			if(groupNeedPay){
				//下面是付款逻辑------------------------------------------------------------------------------------------------------------
				List<PayTarget> waitToInsertTargets=new ArrayList<>();//所有需要新加的PayTarget
				
				//1.找出targetId对应的target
				PayTarget target = getTargetById(oldResult.get(0).getPayTarget(), targetId);
				
				//2.计算组员应付款
				AccountCalculator calculator = new AccountCalculator(account);
				calculator.calcShouldPay(account.getMembers(), target.getMoney());
				System.out.println("组员应付款:"+account.getMembers());
				
				//3.计算组内成员之间的支付方案
				List<Member> IN=new ArrayList<>();//需要收钱的人
		        List<Member> OUT=new ArrayList<>();//需要支出的人
		        
				for(Member innerMember:account.getMembers()){
					boolean needPay=innerMember.getPaidIn()<innerMember.getShouldPay();
					if(needPay)
						OUT.add(innerMember);
					else
						IN.add(innerMember);
				}
				PayResult calcResult = calculator.calcResult(IN, OUT);
				if(calcResult.getPayTarget()!=null && calcResult.getPayTarget().size()>0){
					for(PayTarget payTarget:calcResult.getPayTarget()){
						payTarget.setAccountId(accountId);
						payTarget.setId(IDUtil.generateNewId());
						waitToInsertTargets.add(payTarget);
					}
				}
				
				System.out.println("组内支付方案:"+waitToInsertTargets);
				
				//4.生成组内成员向外部成员的PayTarget(标记为已付)
				for(Member innerMember:account.getMembers()){
					if(innerMember.getPaidIn()==0)
						continue;
					PayTarget payTarget=new PayTarget();
					payTarget.setId(IDUtil.generateNewId());
					payTarget.setPaidId(innerMember.getMemberId());
					payTarget.setReceiptId(target.getReceiptId());
					payTarget.setAccountId(accountId);
					payTarget.setMoney(innerMember.getPaidIn());
					payTarget.setWaitPaidMoney(0);
					payTarget.setPaidStatus(PayTarget.STATUS_NOT_NEED);
					//检查收款者是否是个组,是组的话需要为其设置receit_status
					boolean receiptPersonIsGroup = isGroup(account.getMembers(), target.getReceiptId());
					int receiptGroupPersonCount = receiptPersonIsGroup? groupService.findUsersCountByGroupId(target.getReceiptId()) : -1;
					
					// 收款人是组---并且有人
					if (receiptGroupPersonCount > 1) {
						// 需要手动完善
						payTarget.setReceiptStatus(PayTarget.STATUS_NEED);
						accountService.updatePayTarget(payTarget);
					}
					waitToInsertTargets.add(payTarget);
				}
				
				//5.支付方案去重(因为完善好几个付款的账单后,组内成员之间的支付方案可能会重复有好几个)
				List<PayTarget> oldTargets=oldResult.get(0).getPayTarget();
				for(PayTarget oldTarget:oldTargets)
					for(PayTarget newTarget:waitToInsertTargets)
						if(oldTarget.getPaidId().equals(newTarget.getPaidId())
								&& oldTarget.getReceiptId().equals(newTarget.getReceiptId())){
							newTarget.setMoney(oldTarget.getMoney()+newTarget.getMoney());
							accountService.deletePayTarget(oldTarget.getId());
						}
							
				//6.成员去重后:记录组内成员到数据库
				for (Member newMember : account.getMembers()) 
					if(!isContains(findAccount.getMembers(), newMember.getMemberId())){
						newMember.setId(IDUtil.generateNewId());
						newMember.setAccountId(account.getId());
						newMember.setParentMemberId(memberId);
						accountService.addMember(newMember);
					}else{
						Member oldMember=getMemberById(findAccount.getMembers(), newMember.getMemberId());
						accountService.deleteMember(oldMember.getId());
						oldMember.setPaidIn(oldMember.getPaidIn()+newMember.getPaidIn());
						oldMember.setShouldPay(oldMember.getShouldPay()+oldMember.getShouldPay());
						accountService.addMember(oldMember);
					}
			
				
				//7.录入数据库
				accountService.deletePayTarget(target.getId());
				for(PayTarget payTarget:waitToInsertTargets)
					accountService.addPayTarget(payTarget);
				
				//抵消
				offsetTarget(accountId);
				
				return new Result(Result.RESULT_OK, "完善账单成功!");
			}else{
				//下面是收款逻辑------------------------------------------------------------------------------------------------------------
				
				List<Member> IN=new ArrayList<>();//需要收钱的人
		        List<Member> OUT=new ArrayList<>();//需要支出的人
		        List<PayTarget> mTargets=new ArrayList<>();
		        
				//1.找出所有和本组相关的paytarget，再将那个需要付款的成员加入到out数组
				if (oldResult != null && oldResult.size() > 0 && oldResult.get(0) != null && oldResult.get(0).getPayTarget() != null)
					for (PayTarget target : oldResult.get(0).getPayTarget()) {
						if (target.getReceiptId().equals(memberId)){
							mTargets.add(target);//和我这个组相关的支付
							OUT.add(getMemberById(findAccount.getMembers(), target.getPaidId()));
						}
					}
				
				//2.根据组成员的应付款,计算组内成员的应付款
				AccountCalculator calculator = new AccountCalculator(account);
				calculator.calcShouldPay(account.getMembers(), groupMember.getShouldPay());
				
				//3.记录组内成员到数据库
				for (Member member : account.getMembers()) {
					member.setId(IDUtil.generateNewId());
					member.setAccountId(account.getId());
					member.setParentMemberId(memberId);
					accountService.addMember(member);
				}
				
				//4.遍历本组成员，计算成员应收还是应付，再决定将其加入out或是in
				for(Member innerMember:account.getMembers()){
					boolean needPay=innerMember.getPaidIn()<innerMember.getShouldPay();
					if(needPay)
						OUT.add(innerMember);
					else
						IN.add(innerMember);
				}
				
				//5.计算出新的paytarget后将数据库中和本组相关的条目删除，并在数据库中插入新计算的paytarget，注意恢复之前支付状态
				PayResult calcResult = calculator.calcResult(IN, OUT);
				//删除
				for(PayTarget target:mTargets)
					accountService.deletePayTarget(target.getId());
				//记录
				if (calcResult != null && calcResult.getPayTarget() != null)
					for (PayTarget target : calcResult.getPayTarget()) {
						target.setAccountId(account.getId());
						target.setId(IDUtil.generateNewId());
						// 恢复支付状态
						if (settleMembers.contains(target.getPaidId()))
							target.setWaitPaidMoney(0);
						target.setReceiptStatus(PayTarget.STATUS_COMPLETED);
						accountService.addPayTarget(target);
					}
				
				System.out.println("----------------------------------");
				System.out.println(calcResult);
				
				//抵消
				offsetTarget(accountId);
				
				return new Result(Result.RESULT_OK, "完善账单成功!");
			}
		} catch (CalculatorException e) {
			return new Result(Result.RESULT_FAILD, e.getMessage());
		}
		
		

	}

	/**
	 * 查询帐友和所有相关的分组
	 */
	@ResponseBody
	@RequestMapping("/getAllMembers")
	public Object findMembers(ServletRequest req) {
		String findId = req.getAttribute("userid").toString();

		return new Result(Result.RESULT_OK, "查询分组信息成功!").put("members", accountService.findAllMembers(findId));
	}
	
	
	/**
	 * 付款或者收款
	 */
	@ResponseBody
	@RequestMapping("/settle")
	public Object updatePayTargetSettle(ServletRequest req,String accountId,String targetId) {
		String findId = req.getAttribute("userid").toString();
		PayTarget findPayTarget = accountService.findPayTarget(targetId);
		float oldWaitPaidMoney = findPayTarget.getWaitPaidMoney();
		findPayTarget.setWaitPaidMoney(0);
		accountService.updatePayTarget(findPayTarget);
		
		String toId=findPayTarget.getPaidId().equals(findId)?findPayTarget.getReceiptId():findPayTarget.getPaidId();
		
		String paidMoneyStr=oldWaitPaidMoney==findPayTarget.getMoney()?"":":"+oldWaitPaidMoney;
		msgService.newMessage(Message.MESSAGE_TYPE_ACCOUNT, findId,toId, "[Settle]:"+accountId+":"+targetId+paidMoneyStr);
		

		return new Result(Result.RESULT_OK, "更新付款状态成功!");
	}
	
	
	

	/**
	 * 根据id查询账单
	 */
	@ResponseBody
	@RequestMapping("/get")
	public Object findAccount(ServletRequest req, String accountId) {
		String findId = req.getAttribute("userid").toString();
		Result result = new Result();
		Account findAccount = accountService.findAccount(accountId);
		result.put(findAccount);
		result.remove("imgs");
		if (findAccount.getImgs() == null || "".equals(findAccount.getImgs()))
			result.put("imgs", null);
		else
			result.put("imgs", findAccount.getImgs().split(","));
		
		UserInfo createUser = userService.findUser(findAccount.getUserId());
		result.put("user_icon", createUser.icon);

		// 成员为组时查找当前用户是否在改组内
		if (findAccount.getMembers() != null && findAccount.getMembers().size() > 0) {
			for (Member member : findAccount.getMembers()) {
				if (member.getIsGroup()) {
					boolean isMember = groupService.isGroupMember(findId, member.getMemberId());
					member.setIsMember(isMember);
				}
			}
		}
		
		//每个支付target都需要添加其对应的抵消记录
		List<PayResult> results = findAccount.getPayResult();
		if (results != null && results.size() > 0 && results.get(0) != null && results.get(0).getPayTarget() != null){
			List<Result> newResults = new ArrayList<>();
			List<Result> newTargets = new ArrayList<>();
			newResults.add(new Result().put("payTarget", newTargets));
			
			for(PayTarget target:results.get(0).getPayTarget()){
				Result r=new Result();
				r.put(target);
				
				List<Offset> offsets = accountService.findOffsets(target.getId());
				List<Result> resultOffsets = new ArrayList<>();
				for(Offset o:offsets){
					Result r1=new Result();
					r1.put(o);
					r1.remove("dateTimestamp");
					r1.put("date",new SimpleDateFormat("yyyy/MM/dd").format(new Date(o.dateTimestamp.getTime())));
					resultOffsets.add(r1);
				}
				r.put("offsets", resultOffsets);
				newTargets.add(r);
			}
			
			result.put("payResult", newResults);
		}
		
		result.put("dateDis", CommonUtils.getSinceTimeString(findAccount.getCreateTimestamp()));

		return result.put(Result.RESULT_OK, "查询成功").put("date",
				new SimpleDateFormat("yyyy年MM月dd日").format(new Date(findAccount.getDateTimestamp().getTime())));
	}

	/**
	 * 查询一个用户的所有账单
	 */
	@ResponseBody
	@RequestMapping("/getAll")
	public Object findAccounts(ServletRequest req, String bookId,String userId,Integer pageIndex,Integer pageSize) {
		String findId=req.getAttribute("userid").toString();
		Result result = new Result();
		List<Account> results;
		if(!TextUtils.isEmpty(userId))
			results=accountService.findAccounts2P(findId, userId,pageIndex,pageSize);
		else{
			if (TextUtils.isEmpty(bookId))
				results = accountService.findAccounts(findId,pageIndex,pageSize);
			else
				results = accountService.findAccounts(findId, bookId,pageIndex,pageSize);
		}


		// 将字符串的icons替换为数组形式
		List<Result> resultsWapper = new ArrayList<>();
		for (Account account : results) {
			Result put = new Result().put(account);
			put.remove("imgs");
			if (account.getImgs() == null || "".equals(account.getImgs()))
				put.put("imgs", null);
			else
				put.put("imgs", account.getImgs().split(","));

			UserInfo createUser = userService.findUser(account.getUserId());
			put.put("user_icon", createUser.icon);
			put.put("date", new SimpleDateFormat("yyyy年MM月dd日").format(new Date(account.getDateTimestamp().getTime())));
			put.put("dateDis", CommonUtils.getSinceTimeString(account.getCreateTimestamp()));

			// 成员为组时查找当前用户是否在改组内
			if (account.getMembers() != null && account.getMembers().size() > 0) {
				for (Member member : account.getMembers()) {
					if (member.getIsGroup()) {
						boolean isMember = groupService.isGroupMember(findId, member.getMemberId());
						member.setIsMember(isMember);
					}
				}
			}

			resultsWapper.add(put);
		}

		result.put("accounts", resultsWapper);
		result.put("hasNextPage",results.size()==0?false:true);
		result.put("pageIndex", pageIndex==null || pageIndex<=0 ? CommonUtils.PAGE_DEFAULT_INDEX : pageIndex);
		result.put("pageSize", pageSize==null || pageSize<=0 ? CommonUtils.PAGE_DEFAULT_SIZE : pageSize);

		return result.put(Result.RESULT_OK, "查询账单成功!");
	}
	
	
	
	/**
	 * 删除account
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(ServletRequest req,String accountId) {
//		String findId=req.getAttribute("userid").toString();
		Account findAccount = accountService.findAccount(accountId);
		/**
		 * 1.首先修改涉及到的paytarget
		 * 		1.1遍历所有paytarget
		 * 		1.2查找到和这个partarget相关的payoffset记录
		 * 		1.3遍历这个payoffset集合,一个payoffset会与两个paytarget产生关系
		 * 		1.4这个offset的origin_target是用来抵消target_pay_id的
		 * 			找出和遍历的partarget不同的target(可能是origin_pay_id,可能是target_pay_id)
		 * 		         然后将offset的抵消金额加给查找出partarget的waitpaidmoney,再置查找出partarget的offset_count减掉1就可以
		 * 		1.5删除这个offset
		 *      1.6删除个paytarget
		 * 
		 * 2.删除相关的member
		 * 
		 * 3.删除account
		 * 
		 * 4.将message中相关的消息标记为删除
		 * 
		 */
		boolean noTargets=false;
		if(findAccount.getPayResult()==null || findAccount.getPayResult().size()==0
				|| findAccount.getPayResult().get(0).getPayTarget()==null
				|| findAccount.getPayResult().get(0).getPayTarget().size()==0)
			noTargets=true;
		if(!noTargets){
			ArrayList<PayTarget> findPayTargets = findAccount.getPayResult().get(0).getPayTarget();
			System.out.println("删除findPayTargets.size():"+findPayTargets.size());
			for(PayTarget target:findPayTargets){
				List<PayOffset> findOffsets = accountService.queryOriginOffsets(target.getId());
				if(findOffsets!=null && findOffsets.size()>0){
					for(PayOffset offset:findOffsets){
						//找出需要更新的target,即在payoffset中与targetid不同的那个target
						PayTarget findPayTarget=accountService.findPayTarget(target.getId().equals(offset.targetPayId)?offset.originPayId:offset.targetPayId);
						//更新target
						findPayTarget.setWaitPaidMoney(findPayTarget.getWaitPaidMoney()+offset.money);
						findPayTarget.setOffsetCount(findPayTarget.getOffsetCount()-1);
						accountService.updatePayTarget(findPayTarget);
						
						//删除offset
						accountService.deleteOffset(String.valueOf(offset.id));
						
					}
				}
				
				//删除taget
				accountService.deletePayTarget(target.getId());
			}
		}
		//删除member
		for(Member member:findAccount.getMembers())
			accountService.deleteMember(member.getId());
		
		//删除account
		accountService.deleteAccount(findAccount.getId());
		
		//标记message为删除
		List<Message> accountMsgs = msgService.findAccountMsgs(findAccount.getId());
		System.out.println("AcoountId查找的消息:"+accountMsgs);
		if(accountMsgs!=null && accountMsgs.size()>0)
			for(Message msg:accountMsgs){
				msgService.makeDeleted(msg.id);
			}
		
		return new Result(Result.RESULT_OK, "删除成功");
	}
	
	
	
	
	
	
	/**
	 * 统计一个用户的账本简要信息
	 */
	@ResponseBody
	@RequestMapping("/getSummaryInfo")
	public Object getSummarySimpleInfo(ServletRequest req,String userId) {
		String findId=req.getAttribute("userid").toString();
		Result result=new Result();
		List<SummaryInfo> simpleInfo;
		if(TextUtils.isEmpty(userId))
			simpleInfo = accountService.getSummaryInfo(findId);
		else
			simpleInfo = accountService.getSummaryInfo(findId,userId);
		return result.put(Result.RESULT_OK, "查询成功").put("infos",simpleInfo);
	}

}
