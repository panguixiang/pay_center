package com.walrushz.pay.common.model;

/**
 * 
 * @author panguixiang
 *
 */
public class AlipayTradeFlow {

	private String tradeFlowNo;// 支付中心交易流水，唯一标识
	private String orderId;// 业务系统订单编号
	private String businessKey;// 业务系统key
	private String tradeStatus;// 支付状态（1=初始化，2=交易完成但未得到业务系统的确认，3=交易完成，4=交易关闭,5=等待卖家收款（买家付款后，如果卖家账号被冻结））
	private String back_url;//回调通知业务系统 的url
	private String extraParam;
	private int is_notice_client;

	public String getTradeFlowNo() {
		return tradeFlowNo;
	}

	public void setTradeFlowNo(String tradeFlowNo) {
		this.tradeFlowNo = tradeFlowNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getExtraParam() {
		return extraParam;
	}

	public void setExtraParam(String extraParam) {
		this.extraParam = extraParam;
	}

	public String getBack_url() {
		return back_url;
	}

	public void setBack_url(String back_url) {
		this.back_url = back_url;
	}

	public int getIs_notice_client() {
		return is_notice_client;
	}

	public void setIs_notice_client(int is_notice_client) {
		this.is_notice_client = is_notice_client;
	}

}
