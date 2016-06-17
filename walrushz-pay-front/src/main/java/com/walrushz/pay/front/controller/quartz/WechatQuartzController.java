package com.walrushz.pay.front.controller.quartz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walrushz.pay.front.context.ApplicationContextHelper;
import com.walrushz.pay.front.service.WechatQuartzService;
/**
 * 微信支付定时任务处理器
 * @author panguixiang
 *
 */
@Controller
@RequestMapping("/quartz/wechat")
public class WechatQuartzController {
	
	private static Logger logger = Logger.getLogger(WechatQuartzController.class);

	/**
	 * 定时通知业务系统微信交易流水接口
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
				WechatQuartzService wechatQuartzService =
						(WechatQuartzService) ApplicationContextHelper.getApplicationContext().getBean("wechatQuartzService");
				try {
					wechatQuartzService.callbackBusiness();
				} catch (Exception e) {
					logger.error("定时通知业务系统微信交易流水接口 系统异常=", e.fillInStackTrace());
				}
			}
		}.start();
	}
	
	
	/**
	 * 定时访问微信支付 订单查询接口，对未处理的订单进行查询确认支付状态
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryWechatOrder", method = RequestMethod.GET)
	public void queryWechatOrder(HttpServletRequest request,HttpServletResponse response) {
		
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
				WechatQuartzService wechatQuartzService =
						(WechatQuartzService) ApplicationContextHelper.getApplicationContext().getBean("wechatQuartzService");
				try {
					wechatQuartzService.wechatOrderQuery();
				} catch (Exception e) {
					logger.error("定时访问微信支付 订单查询接口 系统异常=", e.fillInStackTrace());
				}
			}
		}.start();
	}
	
}
