package com.walrushz.pay.front.context;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.walrushz.pay.common.context.CommonConstant;
import com.walrushz.pay.common.context.ContextCache;
import com.walrushz.pay.common.model.BusnieseWhiteIp;
import com.walrushz.pay.front.service.WhiteListService;


public class InitCacheServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4784389336338917419L;
	
	private static Logger logger = Logger.getLogger(InitCacheServlet.class);
	
	public void init() throws ServletException {
		initWhiteIpCache();
		try {
			initRsaKey();
		} catch (Exception e) {
			logger.error("============初始化业务系统RSA秘钥对象====发生异常==================",e);
		}
	}

	private void initWhiteIpCache() {
		logger.info("==========begin===加载白名单列表=======================");
		
		WhiteListService whiteListService =
				(WhiteListService) ApplicationContextHelper.getApplicationContext().getBean("whiteListService");
		ContextCache.whiteIpListCaches=whiteListService.getBusnieseWhiteIps();
		logger.info("==========end===加载白名单列表=======================");
	}
	private void initRsaKey() throws Exception{
		logger.info("==========begin===加载业务系统RSA秘钥对象=======================");
		List<BusnieseWhiteIp> list = ContextCache.whiteIpListCaches;
		for(BusnieseWhiteIp whiteIp : list) {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(whiteIp.getRsaPubKey()));
			ContextCache.RSAKeyMap.put(whiteIp.getBusniessKey().concat(CommonConstant.RSA_PUBLICK_KEY_SUFFIX), (RSAPublicKey) ois.readObject());
			ois.close();
		}
		logger.info("==========end===加载业务系统RSA秘钥对象=======================");
	}
}
