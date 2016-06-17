package com.walrushz.pay.common.alipay.util;

/**
 * 支付宝交易状态枚举
 * @author panguixiang
 *
 */
public enum  AlipayStatusEnum {
	
	WAIT_BUYER_PAY("WAIT_BUYER_PAY", 1), //交易创建，等待买家付款,
	TRADE_CLOSED("TRADE_CLOSED", 3), //在指定时间段内未支付时关闭的交易,在交易完成全额退款成功时关闭的交易
	TRADE_SUCCESS("TRADE_SUCCESS", 2),//交易成功，且可对该交易做操作，如：多级分润、退款等。
	TRADE_PENDING("TRADE_PENDING", 4),//等待卖家收款（买家付款后，如果卖家账号被冻结）。
	TRADE_FINISHED("TRADE_FINISHED", 2);//交易成功且结束，即不可再做任何操作。
	// 成员变量
    private String name;
    private int index;
    
 // 构造方法
    private AlipayStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }
    
    public static int getIndex(String name) {
    	 for (AlipayStatusEnum c : AlipayStatusEnum.values()) {
    		 if(c.name.equals(name)) {
    			 return c.index;
    		 }
    	 }
    	 return 0;
    }
    
}
