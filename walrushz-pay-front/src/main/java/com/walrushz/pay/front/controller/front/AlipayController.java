package com.walrushz.pay.front.controller.front;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walrushz.pay.common.context.CommonConstant;
import com.walrushz.pay.front.controller.FrontBaseController;
import com.walrushz.pay.front.service.AlipayService;

/**
 * alipay支付请求处理controller
 * @author panguixiang
 *
 */
@Controller
@RequestMapping("/front/alipay")
public class AlipayController extends FrontBaseController{
	
	private static Logger logger = Logger.getLogger(AlipayController.class);

	@Autowired
	private AlipayService alipayService;

	/**
	 * 拉取支付宝支付form
	 * @param businessKey
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "initAliPay/{businessKey}", method = RequestMethod.POST, produces = "application/json")
	public Map<String,Object> initAliPay(@PathVariable("businessKey") String businessKey, 
			HttpServletRequest request,HttpServletResponse response) {
		
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("pragma", "no-cache");
		response.setHeader("Content-Type", "text/html;charset=UTF-8");
		
		Map<String, Object> returnMap = null;
		try {
			returnMap = checkFront(request,response);
			if (returnMap.get(CommonConstant.STATUS) != "200") {
				return returnMap;
			}
			alipayService.createAliPayForm(returnMap, businessKey, request);
		} catch (Exception e1) {
			returnMap.put(CommonConstant.STATUS, "500");
			returnMap.put(CommonConstant.MESSAGE, "系统繁忙");
			returnMap.put(CommonConstant.DATA, "");
			logger.error("拉取支付宝支付异常==", e1.fillInStackTrace());
		}
		return returnMap;
	}
	
	
	/**
	 * 支付宝对账接口 业务系统调用此接口查询交易流水状态
	 * @param businessKey
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchSearchApli/{businessKey}", method = RequestMethod.POST, produces = "application/json")
	public Map<String,Object> batchSearchByApliyOrderIds(@PathVariable("businessKey") String businessKey, 
			HttpServletRequest request,HttpServletResponse response) {
		
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("pragma", "no-cache");
		response.setHeader("Content-Type", "text/html;charset=UTF-8");
		
		Map<String, Object> returnMap = null;
		try {
			returnMap = checkFront(request,response);
			if (returnMap.get(CommonConstant.STATUS) != "200") {
				return returnMap;
			}
			return alipayService.batchSearchByApliyOrderIds(returnMap);
		} catch (Exception e1) {
			returnMap.put(CommonConstant.STATUS, "500");
			returnMap.put(CommonConstant.MESSAGE, "系统繁忙");
			returnMap.put(CommonConstant.DATA, "");
			logger.error("支付宝对账接口 异常,业务系统为==".concat(businessKey), e1.fillInStackTrace());
		}
		return returnMap;
	}
	
}
