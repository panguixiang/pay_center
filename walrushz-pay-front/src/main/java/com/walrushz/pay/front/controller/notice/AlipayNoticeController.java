package com.walrushz.pay.front.controller.notice;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walrushz.pay.common.alipay.util.AlipayNotify;
import com.walrushz.pay.front.service.AlipayService;

/**
 * 支付宝异步回调通知处理controller
 * @author panguixiang
 *
 */
@Controller
@RequestMapping("/notice/alipay")
public class AlipayNoticeController {
	
	private static Logger logger = Logger.getLogger(AlipayNoticeController.class);
	
	@Autowired
	private AlipayService alipayService;

	/**
	 * 支付宝异步通知回调入口
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "payCallBack", method = RequestMethod.POST)
	public void payCallBack(HttpServletRequest request,HttpServletResponse response) {
		
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("pragma", "no-cache");
		response.setHeader("Content-Type", "text/plain;charset=UTF-8");
		// 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String key = enu.nextElement();
            params.put(key, request.getParameter(key));
        }
        PrintWriter out = null;
        try {
        	out = response.getWriter();
        	//支付状态（1=初始化，2=交易完成但未得到业务系统的确认，3=交易完成，4=交易关闭,5=等待卖家收款（买家付款后，如果卖家账号被冻结））
        	if (!AlipayNotify.verify(params)) {// MD5验签失败
        		out.print("fail");
        	} else {
        		out.print(alipayService.updateAlipayTradeFlow(request));
        	}
		} catch (Exception e) {
			logger.error("支付宝异步通知回调支付中心 异常 ,支付中心支付流水编号===".concat(request.getParameter("out_trade_no")), e.fillInStackTrace());
			out.print("fail");
		} finally {
			if(out!=null) {
				out.close();
			}
		}
	}
	
}
