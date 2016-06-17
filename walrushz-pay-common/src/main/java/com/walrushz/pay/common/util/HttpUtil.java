package com.walrushz.pay.common.util;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.walrushz.pay.common.context.CommonConstant;

public class HttpUtil {

	/**
	 * httpclient发送post请求
	 * @param url
	 * @param data
	 * @return
	 */
	public static String sendPost(String url, String data) throws Exception{
		HttpClient httpclient = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.setRequestHeader("Connection", "close");
        method.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(4000);//请求超时时长
		try {
			NameValuePair[] param = {
						new NameValuePair("data", data)};
		    method.setRequestBody(param);
			httpclient.executeMethod(method);
			byte[] b  = method.getResponseBody();
			String resutl = new String(b,CommonConstant.ENCODE_UTF_8);
			return resutl;
		} catch (Exception e) {
			throw new Exception("发送http=== "+url+" , post请求发生系统异常===",e);
		} finally {
			if(method!=null) {
				method.abort();
				method.releaseConnection();
			}
		}
	}
	
	/**
	 * httpclient发送post请求
	 * @param url
	 * @param data
	 * @return
	 */
	public static String sendGet(String url, String data) throws Exception{
		HttpClient httpclient = new HttpClient();
		url+="?data="+data;
        GetMethod method = new GetMethod(url);
        method.setRequestHeader("Connection", "close");
        method.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(4000);//请求超时时长
		try {
			httpclient.executeMethod(method);
			byte[] b  = method.getResponseBody();
			String resutl = new String(b,CommonConstant.ENCODE_UTF_8);
			return resutl;
		} catch (Exception e) {
			throw new Exception("发送http=== "+url+" , post请求发生系统异常===",e);
		} finally {
			if(method!=null) {
				method.abort();
				method.releaseConnection();
			}
		}
	}
}
