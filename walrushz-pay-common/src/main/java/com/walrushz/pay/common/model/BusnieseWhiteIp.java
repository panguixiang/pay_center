package com.walrushz.pay.common.model;

/**
 * 业务系统IP白名单对象
 * 
 * @author panguixiang
 *
 */
public class BusnieseWhiteIp {

	private int id;
	private String businessName;
	private String rsaPubKey;
	private String rsaPrivKey;
	private String whiteIp;
	private String busniessKey;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getRsaPubKey() {
		return rsaPubKey;
	}

	public void setRsaPubKey(String rsaPubKey) {
		this.rsaPubKey = rsaPubKey;
	}

	public String getRsaPrivKey() {
		return rsaPrivKey;
	}

	public void setRsaPrivKey(String rsaPrivKey) {
		this.rsaPrivKey = rsaPrivKey;
	}

	public String getWhiteIp() {
		return whiteIp;
	}

	public void setWhiteIp(String whiteIp) {
		this.whiteIp = whiteIp;
	}

	public String getBusniessKey() {
		return busniessKey;
	}

	public void setBusniessKey(String busniessKey) {
		this.busniessKey = busniessKey;
	}

}
