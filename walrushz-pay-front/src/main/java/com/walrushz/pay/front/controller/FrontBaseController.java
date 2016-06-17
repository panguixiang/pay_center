package com.walrushz.pay.front.controller;

import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.walrushz.pay.common.context.CommonConstant;
import com.walrushz.pay.common.context.ContextCache;
import com.walrushz.pay.common.util.IpCheck;
import com.walrushz.pay.common.util.RSATools;
/**
 * 校验业务系统支付请求公共接口
 * @author panguixiang
 *
 */
public class FrontBaseController {

	/**
	 * 校验IP白名单
	 * 校验data数据是否存在
	 * 校验业务系统秘钥是否合法
	 * 校验解密业务系统密文
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> checkFront(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		String ip = IpCheck.getClientIp(request);
		returnMap.put(CommonConstant.STATUS, "406");
		returnMap.put(CommonConstant.DATA, "");
		if(!IpCheck.checkIp(ip)) {
			returnMap.put(CommonConstant.MESSAGE, "请求服务器不在ip白名单内");
			return returnMap;
		}
		String contextPath = request.getServletPath();
		String busniessKey =   contextPath.substring(contextPath.lastIndexOf("/")+1, contextPath.length());
		String data=request.getParameter("data");
		if(StringUtils.isBlank(data)) {
			returnMap.put(CommonConstant.MESSAGE, "请求服务器的请求参数不能为空");
			return returnMap;
		}
		Object obj = ContextCache.RSAKeyMap.get(busniessKey.concat(CommonConstant.RSA_PUBLICK_KEY_SUFFIX));
		if(obj==null) {
			returnMap.put(CommonConstant.MESSAGE, "请求服务器的秘钥不存在");
			return returnMap;
		}
		RSAPublicKey rsaPubKey = (RSAPublicKey)obj;
		String deresult = RSATools.RSADecode(rsaPubKey,data.trim());
		if(StringUtils.isBlank(deresult)) {
			returnMap.put(CommonConstant.MESSAGE, "秘钥错误，解密失败");
			return returnMap;
		} 
		returnMap.put(CommonConstant.STATUS, "200");
		returnMap.put(CommonConstant.MESSAGE, "操作成功");
		returnMap.put(CommonConstant.DATA, deresult);
		return returnMap;
	}

}
