package com.walrushz.pay.common.model;

public class WebChatVo {

	private String order_id;
	private String extra_param;
	private String business_key;
	private String back_url;
	private int is_notice_client;
	
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

	public String getBusiness_key() {
		return business_key;
	}

	public void setBusiness_key(String business_key) {
		this.business_key = business_key;
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
