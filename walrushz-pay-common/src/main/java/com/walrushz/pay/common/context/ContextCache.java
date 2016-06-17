package com.walrushz.pay.common.context;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.walrushz.pay.common.model.BusnieseWhiteIp;

/**
 * 支付中心全局cache 缓存
 * @author panguixiang
 *
 */
public class ContextCache {

	/**
	 * 业务系统IP白名单对象缓存
	 */
	public static List<BusnieseWhiteIp> whiteIpListCaches = new ArrayList<BusnieseWhiteIp>();
	/**
	 * 业务系统RSA秘钥key对象缓存
	 */
	public static ConcurrentHashMap<String,Key> RSAKeyMap = new ConcurrentHashMap<String,Key>();
	
}
