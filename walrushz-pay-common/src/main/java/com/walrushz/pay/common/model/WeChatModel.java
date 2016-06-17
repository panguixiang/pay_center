package com.walrushz.pay.common.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 微信预支付订单生成，请求参数bean
 * 
 * @author panguixiang
 *
 */
@XStreamAlias("xml")
public class WeChatModel {
	/**
	 * 为请求微信发起支付的参数 和 支付中心微信支付 流水表字段
	 */
	@XStreamAlias("appid")
	private String appid;//微信分配的公众账号ID
	
	@XStreamAlias("mch_id")
	private String mch_id;//微信支付分配的商户号
	
	@XStreamAlias("nonce_str")
	private String nonce_str;//随机字符串，不长于32位
	
	@XStreamAlias("body")
	private String body;//商品或支付单简要描述
	
	@XStreamAlias("attach")
	private String attach;//附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
	
	@XStreamAlias("out_trade_no")
	private String out_trade_no;//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
	
	@XStreamAlias("total_fee")
	private long total_fee;//订单总金额，单位为分 整型
	
	@XStreamAlias("spbill_create_ip")
	private String spbill_create_ip;//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
	
	@XStreamAlias("time_start")
	private String time_start;//订单生成时间，格式为yyyyMMddHHmmss
	
	@XStreamAlias("notify_url")
	private String notify_url;//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	
	@XStreamAlias("trade_type")
	private String trade_type;// JSAPI，NATIVE，APP
								// JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
								// MICROPAY--刷卡支付
	
	@XStreamAlias("product_id")
	private String product_id;//trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
	
	@XStreamAlias("openid")
    private String openid;//trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
	
	@XStreamAlias("sign")
	private String sign;
	
	
	@XStreamOmitField
	private String order_id;//业务系统订单号
	
	@XStreamOmitField
	private String extra_param;//业务系统扩展字段
	
	@XStreamOmitField
	private String payer_busniess_account;//业务系统账号
	
	@XStreamOmitField
	private String seller_trade_account;//收款方微信账号
	
	@XStreamOmitField
	private String back_url;//业务系统异步回调通知url
	
	@XStreamOmitField
	private String description;//业务系统描述
	
	@XStreamOmitField
	private String business_key;// 业务系统key
	
	@XStreamOmitField
	private Integer trade_status;//支付状态
	
	@XStreamOmitField
	private Integer is_notice_client;//是否完成业务系统支付结果通知
	
	@XStreamOmitField
	private String wechat_err_code_des;//错误代码描述
	@XStreamOmitField
	private String wechat_err_code;//错误代码
	@XStreamOmitField
	private String wechat_result_code;//返回状态码 SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	@XStreamOmitField
	private String wechat_return_msg;//返回信息 如非空，为错误原因 如：签名失败
	@XStreamOmitField
	private String wechat_return_code;//业务结果 SUCCESS/FAIL 当wechat_result_code与此都为success时表示成功
	@XStreamOmitField
	private String code_url;//二维码链接 trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付
	@XStreamOmitField
	private String transaction_id;//微信支付订单号，微信生成
	
	@XStreamOmitField
	private String paramError;//不做微信请求参数也不做数据入库参数，仅封装业务系统请求参数错误信息

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public long getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(long total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getExtra_param() {
		return extra_param;
	}

	public void setExtra_param(String extra_param) {
		this.extra_param = extra_param;
	}

	public String getPayer_busniess_account() {
		return payer_busniess_account;
	}

	public void setPayer_busniess_account(String payer_busniess_account) {
		this.payer_busniess_account = payer_busniess_account;
	}

	public String getSeller_trade_account() {
		return seller_trade_account;
	}

	public void setSeller_trade_account(String seller_trade_account) {
		this.seller_trade_account = seller_trade_account;
	}

	public String getBack_url() {
		return back_url;
	}

	public void setBack_url(String back_url) {
		this.back_url = back_url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBusiness_key() {
		return business_key;
	}

	public void setBusiness_key(String business_key) {
		this.business_key = business_key;
	}

	public Integer getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(Integer trade_status) {
		this.trade_status = trade_status;
	}

	public Integer getIs_notice_client() {
		return is_notice_client;
	}

	public void setIs_notice_client(Integer is_notice_client) {
		this.is_notice_client = is_notice_client;
	}

	public String getWechat_err_code_des() {
		return wechat_err_code_des;
	}

	public void setWechat_err_code_des(String wechat_err_code_des) {
		this.wechat_err_code_des = wechat_err_code_des;
	}

	public String getWechat_err_code() {
		return wechat_err_code;
	}

	public void setWechat_err_code(String wechat_err_code) {
		this.wechat_err_code = wechat_err_code;
	}

	public String getWechat_result_code() {
		return wechat_result_code;
	}

	public void setWechat_result_code(String wechat_result_code) {
		this.wechat_result_code = wechat_result_code;
	}

	public String getWechat_return_msg() {
		return wechat_return_msg;
	}

	public void setWechat_return_msg(String wechat_return_msg) {
		this.wechat_return_msg = wechat_return_msg;
	}

	public String getWechat_return_code() {
		return wechat_return_code;
	}

	public void setWechat_return_code(String wechat_return_code) {
		this.wechat_return_code = wechat_return_code;
	}

	public String getCode_url() {
		return code_url;
	}

	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getParamError() {
		return paramError;
	}

	public void setParamError(String paramError) {
		this.paramError = paramError;
	}
	
}
