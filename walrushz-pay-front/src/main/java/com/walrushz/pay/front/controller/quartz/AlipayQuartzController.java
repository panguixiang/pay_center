package com.walrushz.pay.front.controller.quartz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walrushz.pay.front.context.ApplicationContextHelper;
import com.walrushz.pay.front.service.AlipayQuartzSerivce;
/**
 * 接收任务中心发起的http执行任务指令
 * 定时通知业务系统支付宝交易流水接口
 * @author panguixiang
 *
 */
@Controller
@RequestMapping("/quartz/alipay")
public class AlipayQuartzController {
	
	private static Logger logger = Logger.getLogger(AlipayQuartzController.class);

	/**
	 * 定时通知业务系统支付宝交易流水接口
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "timingCallBack", method = RequestMethod.GET)
	public void payCallBack(HttpServletRequest request,HttpServletResponse response) {
		
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("pragma", "no-cache");
		response.setHeader("Content-Type", "text/plain;charset=UTF-8");
		/**
		 * 接收到quartz 中心的任务指令后 
		 * 单独启动一个线程对象来执行定时业务，防止占用http 线程
		 */
		new Thread(){
			public void run(){
				AlipayQuartzSerivce alipayQuartzSerivce =
						(AlipayQuartzSerivce) ApplicationContextHelper.getApplicationContext().getBean("alipayQuartzSerivce");
				try {
					alipayQuartzSerivce.dowork();
				} catch (Exception e) {
					logger.error("定时通知业务系统支付宝交易流水接口 系统异常=", e.fillInStackTrace());
				}
			}
		}.start();
	}
	
}
