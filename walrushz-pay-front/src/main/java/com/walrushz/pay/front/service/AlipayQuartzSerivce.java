package com.walrushz.pay.front.service;

import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walrushz.pay.common.context.CommonConstant;
import com.walrushz.pay.common.context.ContextCache;
import com.walrushz.pay.common.util.HttpUtil;
import com.walrushz.pay.common.util.RSATools;
import com.walrushz.pay.common.util.RoomUtil;
import com.walrushz.pay.common.util.UtilDate;
import com.walrushz.pay.dao.AlipayTradeFlowDao;

import net.sf.json.JSONObject;

/**
 * 定时查询apliy支付交易流水表里还未得到业务系统确认通知的流水主动定时通知业务系统
 * 
 * @author panguixiang
 *
 */
@Service("alipayQuartzSerivce")
public class AlipayQuartzSerivce {
	
	private static Logger logger = Logger.getLogger(AlipayQuartzSerivce.class);

	
	@Autowired
	private AlipayTradeFlowDao alipayTradeFlowDao;

	/**
	 * 定时循环查询alipay交易流水表里每个未获得业务系统回调确认的交易流水
	 * 重新发起通知，直到得到业务系统正确通知响应，修改交易流水通知状态
	 * @param param
	 */
	public void dowork() throws Exception{
		String nowYear = UtilDate.getDate(CommonConstant.dtYear);
		logger.info("====AlipayQuartzSerivce====定时回调apliy支付交易流水给业务系统===开始执行========");
		int start =0,end=20;
		List<ConcurrentHashMap<String,Object>> list = null;
		String backUrl="",clientResult="";
		Object objRsa;
		RSAPublicKey rsaPubKey;
		List<String> flowTableArr = RoomUtil.yearArr(nowYear);//查询当前年份和上一年份的交易流水表，防止跨年,年份跨度超过30天的则只查询当前年份
		for(String tableFixx : flowTableArr) {//查询按年分表
			while(true) {
				list = alipayTradeFlowDao.queryPageListByStatusAndIsNoticed(start, end, tableFixx);
				if(CollectionUtils.isEmpty(list)) {//若查询不到结果集，则修改start为出事状态，退出while循环
					start =0;
					break;
				}
				/**
				 * 若查询的list非空，则遍历发送http post请求回调业务系统，通知交易状态
				 */
				for(ConcurrentHashMap<String,Object> map : list) {
					objRsa = ContextCache.RSAKeyMap.get(((String)map.get("busniessKey")).concat(CommonConstant.RSA_PUBLICK_KEY_SUFFIX));
					try {
						rsaPubKey = (RSAPublicKey)objRsa;
						backUrl = (String)map.get("back_url");
						map.remove("back_url");
						clientResult = HttpUtil.sendPost(backUrl.trim(), RSATools.RSAENcode(rsaPubKey,JSONObject.fromObject(map).toString()));
						if(StringUtils.equals(clientResult, "success") || clientResult.indexOf("success")>-1) {//若得到业务系统的正确响应，修改交易流水回调参数
							alipayTradeFlowDao.updateIsNoticeByOrderId(tableFixx, 2, (String)map.get("order_id"));
						}
					} catch(Exception e) {
						logger.error("定时通知业务系统="+map.get("busniessKey")+",支付宝交易流水状态时发生异常,交易订单号==".concat((String)map.get("order_id")), e.fillInStackTrace());
					}
				}
				start +=20;//遍历递增
			}
		}
		logger.info("====AlipayQuartzSerivce====定时回调apliy支付交易流水给业务系统===结束执行========");
	}
}
