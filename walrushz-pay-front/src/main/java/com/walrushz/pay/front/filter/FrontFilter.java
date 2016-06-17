package com.walrushz.pay.front.filter;

import java.security.interfaces.RSAPublicKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.walrushz.pay.common.context.CommonConstant;
import com.walrushz.pay.common.context.ContextCache;
import com.walrushz.pay.common.util.IpCheck;
import com.walrushz.pay.common.util.RSATools;

public class FrontFilter extends HandlerInterceptorAdapter{
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String noAutorityUrl = request.getContextPath().concat("/common/pay/error");
		String ip = IpCheck.getClientIp(request);
		if(!IpCheck.checkIp(ip)) {
			 response.sendRedirect(noAutorityUrl.concat("?message=").concat("请求服务器不在ip白名单内"));
			 return false;
		}
		 String contextPath = request.getServletPath();
		String busniessKey =   contextPath.substring(contextPath.lastIndexOf("/")+1, contextPath.length());
		String data=request.getParameter("data");
		if(StringUtils.isBlank(data)) {
			 response.sendRedirect(noAutorityUrl.concat("?message=").concat("请求服务器的请求参数不能为空"));
			 return false;
		}
		Object obj = ContextCache.RSAKeyMap.get(busniessKey.concat(CommonConstant.RSA_PUBLICK_KEY_SUFFIX));
		if(obj==null) {
			 response.sendRedirect(noAutorityUrl.concat("?message=").concat("请求服务器的秘钥不存在"));
			 return false;
		}
		RSAPublicKey rsaPubKey = (RSAPublicKey)obj;
		String deresult = RSATools.RSADecode(rsaPubKey,data.trim());
		if(StringUtils.isBlank(deresult)) {
			 response.sendRedirect(noAutorityUrl.concat("?message=").concat("解密失败"));
			 return false;
		}
		request.setAttribute("body", deresult);
		return true;
	}
	
	public static void main(String args[]) {
		String path = "/front/alipay/initAliPay/koko";
		int d = path.lastIndexOf("/");
		System.out.println(d);
		String busniessKey =   path.substring(d+1, path.length());
		System.out.println(busniessKey);
	}

}
