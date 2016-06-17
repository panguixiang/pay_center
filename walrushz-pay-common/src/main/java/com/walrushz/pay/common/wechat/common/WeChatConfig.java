package com.walrushz.pay.common.wechat.common;

public class WeChatConfig {

	public final static String wechat_OpenID="";
	public final static String wechat_Appid="wx400670b9798940c2";
	public final static String wechat_Mch_id="1311862201";
	
	public final static String wechat_key = "6a154d2cbb854217b84d94a085af32c4";
	//统一下单 url
	public final static String wechat_init_pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	public final static String wechat_tradeType_JSAPI = "JSAPI";//--公众号支付、
	
	public final static String wechat_tradeType_NATIVE = "NATIVE";//NATIVE--原生扫码支付、
	
	public final static String wechat_tradeType_APP = "APP";//APP--app支付
	
	public final static String wechat_tradeType_MICROPAY = "MICROPAY";//MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口
	
	public final static String wechat_orderquery = "https://api.mch.weixin.qq.com/pay/orderquery"; //微信订单查询接口


	
}
