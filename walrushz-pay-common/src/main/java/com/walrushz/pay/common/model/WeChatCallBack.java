package com.walrushz.pay.common.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 返回给微信回调接口 的数据model xml
 * @author panguixiang
 *
 */
@XStreamAlias("xml")
public class WeChatCallBack {

	@XStreamAlias("return_code")
	private String return_code;//SUCCESS/FAIL SUCCESS表示商户接收通知成功并校验成功
	
	@XStreamAlias("return_msg") //返回信息，如非空，为错误原因：签名失败,异常，格式错误等
	private String return_msg;

	public String getReturn_code() {
		return "<![CDATA["+return_code+"]]>";
	}

	public void setReturn_code(String return_code) {
		this.return_code = "<![CDATA["+return_code+"]]>";
	}

	public String getReturn_msg() {
		return "<![CDATA["+return_msg+"]]>";
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = "<![CDATA["+return_msg+"]]>";
	}
	
}
