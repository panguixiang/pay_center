package com.walrushz.pay.front.controller.notice;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walrushz.pay.common.model.WeChatCallBack;
import com.walrushz.pay.common.wechat.common.Signature;
import com.walrushz.pay.common.wechat.common.WeChatConfig;
import com.walrushz.pay.common.wechat.common.XMLParser;
import com.walrushz.pay.front.service.WeChatService;
/**
 * 微信支付异步回调通知处理controller
 * @author panguixiang
 *
 */
@Controller
@RequestMapping("/notice/wechat")
public class WechatNoticeController {

	private static Logger logger = Logger.getLogger(WechatNoticeController.class);
	
	@Autowired
	private WeChatService weChatService;
	
	/**
	 * 微信支付异步通知回调入口
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "payCallBack", method = RequestMethod.POST)
	public void payCallBack(HttpServletRequest request,HttpServletResponse response) {
		
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("pragma", "no-cache");
		response.setHeader("Content-Type", "text/xml");
        ServletInputStream input = null;
        WeChatCallBack callBack = new WeChatCallBack();
		String xmlStr ="",return_code="";
		PrintWriter out = null;
		Map<String, Object> wechatMap =null;
        try {
        	out = response.getWriter();
        	input = request.getInputStream();
        	String callBackXml = IOUtils.toString(input);
        	callBackXml = callBackXml.replace("<![CDATA[", "");
        	callBackXml = callBackXml.replace("]]>", "");
            wechatMap = XMLParser.getMapFromXML(callBackXml);
            return_code = (String)wechatMap.get("return_code");
            if(StringUtils.equals(return_code.toUpperCase(), "FAIL")) {//说明微信支付服务并未受理此支付成功，无需做任何处理
            	callBack.setReturn_code("SUCCESS");
    			callBack.setReturn_msg("");
            } else {
            	//支付状态（1=初始化，2=交易完成）
            	String newSign = Signature.getSignFromResponseString(callBackXml,WeChatConfig.wechat_key);
            	if (!StringUtils.equals(newSign, (String)wechatMap.get("sign"))) {// MD5验签失败
            		callBack.setReturn_code("FAIL");
            		callBack.setReturn_msg("签名失败");
            	} else {
            		callBack.setReturn_code(weChatService.updateWeChatTradeFlow(wechatMap));
            		callBack.setReturn_msg("");
            	}
            }
		} catch (Exception e) {
			callBack.setReturn_code("FAIL");
			callBack.setReturn_msg("系统异常");
			logger.error("微信支付异步通知回调支付中心 异常 ,支付中心支付流水编号===".concat((String)wechatMap.get("out_trade_no")), e.fillInStackTrace());
		} finally {
			xmlStr = XMLParser.toXml(callBack);
			out.print(xmlStr);
			if(out!=null) {
				out.close();
			}
		}
	}
}
