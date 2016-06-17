package com.walrushz.pay.common.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
/**
 * 查询微信支付订单状态 请求参数bean
 * @author panguixiang
 *
 */
@XStreamAlias("xml")
public class WeChatOrderQuery {

	/**
	 * 为请求微信发起支付的参数 和 支付中心微信支付 流水表字段
	 */
	@XStreamAlias("appid")
	private String appid;//微信分配的公众账号ID
	
	@XStreamAlias("mch_id")
	private String mch_id;//微信支付分配的商户号
	
	@XStreamOmitField
	private String out_trade_no;//微信支付流水号
	
	@XStreamAlias("nonce_str")
	private String nonce_str;//随机字符串，不长于32位
	
	@XStreamAlias("sign")
	private String sign;

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

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
