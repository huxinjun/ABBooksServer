package com.accountbook.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.accountbook.modle.Account;

/**
 * 账户结算
 * Created by xinjun on 2017/8/1 14:46
 */
public class AccountCalculator {

    private Account mAccount;

    public AccountCalculator(Account account) {
        this.mAccount=account;
    }

    /**
     * 计算所有可能的支付结果
     * @return
     */
    public List<Account.PayResult> calc() throws CalculatorException{

        mAccount.setPayResult(new ArrayList<Account.PayResult>());

        /**
         * 0.算出总花费
         * 1.算出所有参与者应缴金额
         * 2.排除含有特殊缴费规则的用户后其余用户AA制,并算出AA制平均缴费额度
         * 3.按照平均缴费分组,大于平均的一组,小于平均的一组
         * 4.大于平均的向小于平均的收费,直到收到多余平均值得金额后换下一个大于平均值的参与者,直到所有大于平均值得参与者收到自己多交的那部分钱
         */
        //======================step0=========================
        mAccount.setPaidIn(0);
        for (Account.Member p:mAccount.getMembers())
            mAccount.setPaidIn(mAccount.getPaidIn()+p.getPaidIn());
        System.out.println("总支付:"+mAccount.getPaidIn());


        //======================step1=========================
        double hasRuleMoney=0;
        int noRulePersonCount=0;
        for (Account.Member p:mAccount.getMembers()) {
            Account.Member.PayRule payRule = p.getPayRule();
            if(payRule == null) {
                noRulePersonCount++;
                //排除总金额中有些人给自己花了一部分钱,别人无需AA支付这个钱
                p.setShoudPay(p.getMoneyForSelf());
                hasRuleMoney+=p.getMoneyForSelf();
                continue;
            }
            //number_type   0:基于总支出的百分比的值  1:缴费为一个固定值
            switch (payRule.getType()){
                case 0:
                    if(payRule.getNum()>1)
                        throw new CalculatorException("您为"+p.getName()+"设置的支付类型是基于总支出百分比的值,取值范围[0~1]!");
                    //应缴金额
                    p.setShoudPay(mAccount.getPaidIn()*payRule.getNum());
                    break;
                case 1:
                    if(payRule.getNum()>mAccount.getPaidIn())
                        throw new CalculatorException(p.getName()+"支付的金额超过总支出!");
                    p.setShoudPay(payRule.getNum());
                    break;
            }
            hasRuleMoney+=p.getShoudPay();
        }

        //======================step2=========================
        double averageMoney=(mAccount.getPaidIn()-hasRuleMoney)/noRulePersonCount;
        for (Account.Member p:mAccount.getMembers()) {
            Account.Member.PayRule payRule = p.getPayRule();
            if(payRule == null) {
                //没有设置规则用户需要支付的金额
                p.setShoudPay(averageMoney+p.getShoudPay());
            }
        }


        //======================step3=========================
        List<Account.Member> IN=new ArrayList<>();//需要收钱的人
        List<Account.Member> OUT=new ArrayList<>();//需要支出的人

        for (Account.Member p:mAccount.getMembers()) {
            //实际支持少余平均值,需要付钱
            if(p.getPaidIn()<averageMoney)
                OUT.add(p);
            //实际支持多余平均值,需要收钱
            else if(p.getPaidIn()>averageMoney)
                IN.add(p);
        }

        System.out.println("IN:"+IN.toString());
        System.out.println("OUT:"+OUT.toString());


        //======================step4=========================
        //换位置后再计算结果
        for(int m=0;m<OUT.size()-1;m++)
            for(int n=m;n<OUT.size();n++){
                transposition(OUT,n,m);
                calcResult(IN,OUT);
            }
        if(mAccount.getPayResult().size()==0)
            calcResult(IN,OUT);

        //======================done===========================
        System.out.println(mAccount);
        return mAccount.getPayResult();
    }

    /**
     * 计算付款着和收款者整个流程
     * @param IN 收款者
     * @param OUT 付款者
     */
    private void calcResult(List<Account.Member> IN,List<Account.Member> OUT){
        Account.PayResult result=new Account.PayResult();
        result.setPayTarget(new ArrayList<Account.PayResult.PayTarget>());
        for(int i=0;i<IN.size();i++){
            //当前收钱的人
            Account.Member IN_P = IN.get(i);
            int n=0;
            //确定付款人的位置,n之前的人都已经付过了
            while (n<OUT.size() && OUT.get(n).getCalcData()>=OUT.get(n).getShoudPay())
                n++;
            Account.Member OUT_P;
            while (n<OUT.size()){
                OUT_P = OUT.get(n++);

                Account.PayResult.PayTarget payTarget=new Account.PayResult.PayTarget();
                payTarget.setPayMember(OUT_P.getId());
                payTarget.setReceiptMember(IN_P.getId());

                double needMoney=IN_P.getPaidIn()-(IN_P.getShoudPay()+IN_P.getCalcData());//还需收的钱.
                if(needMoney<0.001)
                    break;
                if(needMoney>=OUT_P.getShoudPay()-(OUT_P.getPaidIn()+OUT_P.getCalcData())){
                    //收钱的人多付的钱大于付钱的人还需付的钱,把付钱人还需付的钱全部收来
                    double pay=OUT_P.getShoudPay()-(OUT_P.getPaidIn()+OUT_P.getCalcData());
                    OUT_P.setCalcData(OUT_P.getShoudPay());
                    IN_P.setCalcData(IN_P.getCalcData()+pay);
                    payTarget.setMoney(round(pay));

                }else if(needMoney<OUT_P.getShoudPay()-(OUT_P.getPaidIn()+OUT_P.getCalcData())){
                    //收钱人多付的钱少于付钱人还需付的钱,向付钱人收自己多付的那部分
                    double pay=needMoney;
                    OUT_P.setCalcData(OUT_P.getCalcData()+pay);
                    IN_P.setCalcData(IN_P.getCalcData()+pay);
                    payTarget.setMoney(round(pay));
                }
                result.getPayTarget().add(payTarget);




            }
        }
        mAccount.getPayResult().add(result);
        //初始化
        for (Account.Member p:IN)
            p.setCalcData(0);
        for (Account.Member p:OUT)
            p.setCalcData(0);
    }

    /**
     * 数组元素换位置
     * @param target 数组
     * @param i 位置
     * @param j 位置
     */
    private void transposition(List<Account.Member> target,int i,int j){
        if(i==j)
            return;
        Account.Member temp=target.get(i);
        target.set(i,target.get(j));
        target.set(j,temp);
    }

    public double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(3, BigDecimal.ROUND_HALF_DOWN);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }


    /**
     * 计算异常
     * Created by xinjun on 2017/8/1 20:58
     */
    public class CalculatorException extends Exception{

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
         * Constructs a new exception with the specified detail message.  The
         * cause is not initialized, and may subsequently be initialized by
         * a call to {@link #initCause}.
         *
         * @param message the detail message. The detail message is saved for
         *                later retrieval by the {@link #getMessage()} method.
         */
        public CalculatorException(String message) {
            super(message);
        }
    }


}
