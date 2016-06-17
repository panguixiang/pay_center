package com.walrushz.pay.common.model;

/**
 * 回调业务系统model
 * @author panguixiang
 *
 */
public class CallBackBusiness {

	private String order_id;
	private String total_fee;
	private String busniessKey;
	private Integer trade_status;//success,fail
	private String extra_param;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getBusniessKey() {
		return busniessKey;
	}

	public void setBusniessKey(String busniessKey) {
		this.busniessKey = busniessKey;
	}

	public Integer getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(Integer trade_status) {
		this.trade_status = trade_status;
	}

	public String getExtra_param() {
		return extra_param;
	}

	public void setExtra_param(String extra_param) {
		this.extra_param = extra_param;
	}
	
	
}
