package com.walrushz.pay.front.controller;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.security.interfaces.RSAPrivateKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walrushz.pay.common.util.HttpUtil;
import com.walrushz.pay.common.util.RSATools;
import com.walrushz.pay.common.util.RoomUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/testWeChatClient")
public class TestWeChatController {

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(Model model) {
		ObjectInputStream ois;
		try {
			/**
			 * 构建RSA私钥
			 */
			ois = new ObjectInputStream(new FileInputStream("d:/koko_pri.txt"));
			RSAPrivateKey priKey = (RSAPrivateKey) ois.readObject();
			ois.close();
			/**
			 * 构造请求参数 json格式
			 */
			JSONObject json = new JSONObject();
			json.put("body", "小苹果6");//商品名称，不能为null
			json.put("total_fee", "0.01");//支付总金额人民币元，
			json.put("order_id", RoomUtil.findNextVal());//业务系统订单唯一标识号,注意order_id 前4位必须为年份字符串如2015
			json.put("product_id", "323223");//购买者的支付宝账号，可为“”
			json.put("extra_param", "扩展参数");//公共回传参数，
			json.put("seller_trade_account", "koko@wechat.com");
			json.put("payer_busniess_account", "");
			json.put("description", "test");
			/**
			 * 支付中心回调此客户端服务器（业务系统服务器）的回调地址，回调也必须是post方式,url里不可以带任何参数
			 */
			json.put("back_url", "http://192.168.17.48:8085/testWeChatClient/callback");
			/**
			 * 使用RSA私钥队json字符串格式的 参数加密 （支付中心公钥解密）
			 */
			String data = RSATools.RSAENcode(priKey,json.toString());
			/**
			 * 发起http post请求支付中心
			 */
			String result = HttpUtil.sendPost("http://192.168.17.48:8085/front/wechat/initNative/koko",data);
			/**
			 * 从支付中心同步返回json结构字符串{"status":"200/406/500","message":"失败/成功","data":"支付宝form表单字符串"}
			 * 同步返回未加密
			 */
			if(StringUtils.isNotBlank(result)) {
				JSONObject retjson = JSONObject.fromObject(result);//构造json对象
				if(StringUtils.equals(retjson.getString("status"),"200")) {
					//将此微信生成的支付url字符串返回浏览器，再用js生成二维码
					model.addAttribute("wechat_code_url", retjson.getString("data"));
				} else {//若请求异常，失败，错误则将错误信息打印在页面（这里可以自定义处理方式）
					model.addAttribute("wechat_code_url", retjson.getString("message"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "wechat/index";
	}
	
	
	
	/**
	 * 支付中心回调测试接口
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "callback", method = RequestMethod.POST)
	public void callback(HttpServletRequest request,HttpServletResponse response) {
		
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("pragma", "no-cache");
		response.setHeader("Content-Type", "text/plain;charset=UTF-8");
		ObjectInputStream ois;
		PrintWriter out = null;
		try {
			/**
			 * 使用RSA私钥 解密支付中心异步回调的结果
			 */
			ois = new ObjectInputStream(new FileInputStream("d:/koko_pri.txt"));
			RSAPrivateKey priKey = (RSAPrivateKey) ois.readObject();
			ois.close();
			String data=request.getParameter("data");
			String result = RSATools.RSADecode(priKey,data);
			System.out.println("==client===callback====result======="+result);
			out = response.getWriter();
			out.print("success");//同步返回，无需加密，必须返回success字符串，不然支付中心会不间断异步回调，直到业务系统同步返回success字符串
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) {
				out.close();
			}
		}
	}
	
	
	/**
	 * 向支付中心发起批量查询微信支付订单状态
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "testBatchSearchApliOrders", method = RequestMethod.GET)
	public void testBatchSearchApliOrders(Model model) {
		ObjectInputStream ois;
		try {
			/**
			 * 使用RSA私钥对订单集合字符串 参数加密 （支付中心公钥解密）
			 */
			String orderIds = "20160218143358262498463146,20160218173049516491850232,20160318173049516491850232";
			ois = new ObjectInputStream(new FileInputStream("d:/koko_pri.txt"));
			RSAPrivateKey priKey = (RSAPrivateKey) ois.readObject();
			ois.close();
			String data = RSATools.RSAENcode(priKey,orderIds);
			/**
			 * 发起http post请求支付中心
			 */
			String result = HttpUtil.sendPost("http://192.168.17.48:8085/front/wechat/batchSearchApli/koko",data);
			/**
			 * 从支付中心同步返回json结构字符串{"status":"200/406/500","message":"失败/成功","data":"json集合字符串"}
			 * json集合字符串 格式：
			 * [
			 *  {"order_id":"20160201160236299665734242","total_fee":"0.010","busniessKey":"koko","trade_status":2,"extra_param":"sdfksdafs"},
	 		 *	{"order_id":"20160201160317203581734134","total_fee":"0.010","busniessKey":"koko","trade_status":3,"extra_param":"sdfksdafs"}
	 		 * ]
			 */
			System.out.println("===========result======"+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
