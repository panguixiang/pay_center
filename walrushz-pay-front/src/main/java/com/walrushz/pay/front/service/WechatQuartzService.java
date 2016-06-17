package com.walrushz.pay.front.service;

import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walrushz.pay.common.context.CommonConstant;
import com.walrushz.pay.common.context.ContextCache;
import com.walrushz.pay.common.model.WeChatOrderQuery;
import com.walrushz.pay.common.util.HttpUtil;
import com.walrushz.pay.common.util.HttpsUtil;
import com.walrushz.pay.common.util.RSATools;
import com.walrushz.pay.common.util.RoomUtil;
import com.walrushz.pay.common.util.UtilDate;
import com.walrushz.pay.common.wechat.common.RandomStringGenerator;
import com.walrushz.pay.common.wechat.common.Signature;
import com.walrushz.pay.common.wechat.common.WeChatConfig;
import com.walrushz.pay.common.wechat.common.XMLParser;
import com.walrushz.pay.dao.WechatTradeFlowDao;

import net.sf.json.JSONObject;


/**
 * 微信支付定时任务 业务处理
 * 
 * @author panguixiang
 *
 */
@Service("wechatQuartzService")
public class WechatQuartzService {
	
	private static Logger logger = Logger.getLogger(WechatQuartzService.class);

	@Autowired
	private WechatTradeFlowDao wechatTradeFlowDao;
	
	/**
	 * 定时循环查询微信交易流水表里每个未获得业务系统回调确认的交易流水
	 * 重新发起通知，直到得到业务系统正确通知响应，修改交易流水通知状态
	 * @param param
	 */
	public void callbackBusiness() throws Exception{
		String nowYear = UtilDate.getDate(CommonConstant.dtYear);
		logger.info("====WechatQuartzService====定时回调 微信支付 交易流水 给业务系统===开始执行========");
		int start =0,end=20;
		List<ConcurrentHashMap<String,Object>> list = null;
		String backUrl="",clientResult="";
		Object objRsa;
		RSAPublicKey rsaPubKey;
		List<String> flowTableArr = RoomUtil.yearArr(nowYear);//查询当前年份和上一年份的交易流水表，防止跨年,年份跨度超过30天的则只查询当前年份
		for(String tableFixx : flowTableArr) {//查询按年分表
			while(true) {
				list = wechatTradeFlowDao.queryWechatPageByStatusAndIsNoticed(start, end, tableFixx);
				if(CollectionUtils.isEmpty(list)) {//若查询不到结果集，则修改start为出事状态，退出while循环
					start =0;
					break;
				}
				/**
				 * 若查询的list非空，则遍历发送http post请求回调业务系统，通知交易状态
				 */
				for(ConcurrentHashMap<String,Object> map : list) {
					objRsa = ContextCache.RSAKeyMap.get(((String)map.get("business_key")).concat(CommonConstant.RSA_PUBLICK_KEY_SUFFIX));
					try {
						rsaPubKey = (RSAPublicKey)objRsa;
						backUrl = (String)map.get("back_url");
						map.remove("back_url");
						clientResult = HttpUtil.sendPost(backUrl.trim(), RSATools.RSAENcode(rsaPubKey,JSONObject.fromObject(map).toString()));
						if(StringUtils.equals(clientResult, "success") || clientResult.indexOf("success")>-1) {//若得到业务系统的正确响应，修改交易流水回调参数
							wechatTradeFlowDao.updateWeChatIsNoticeByOrderId(tableFixx, 2, (String)map.get("order_id"));
						}
					} catch(Exception e) {
						logger.error("定时通知业务系统="+map.get("business_key")+",微信交易流水状态时发生异常,交易订单号==".concat((String)map.get("order_id")), e.fillInStackTrace());
					}
				}
				start +=20;//遍历递增
			}
		}
		logger.info("====WechatQuartzService====定时回调 微信支付 交易流水给业务系统===结束执行========");
	}
	
	
	
	/**
	 * 定时去微信服务器查询未支付成功，失败的订单
	 * @param param
	 */
	public void wechatOrderQuery() throws Exception{
		String nowYear = UtilDate.getDate(CommonConstant.dtYear);
		logger.info("====WechatQuartzService====定时回调 微信服务器查询交易订单状态 ，更新本地交易流水数据库===开始执行========");
		int start =0,end=20;
		List<ConcurrentHashMap<String,String>> list = null;
		List<String> flowTableArr = RoomUtil.yearArr(nowYear);//查询当前年份和上一年份的交易流水表，防止跨年,年份跨度超过30天的则只查询当前年份
		WeChatOrderQuery model = new WeChatOrderQuery();
		String postResult = "";
		for(String tableFixx : flowTableArr) {//查询按年分表
			while(true) {
				list = wechatTradeFlowDao.getWechatFlowNosByStatus(start, end, tableFixx,1);//1表示初始化状态的订单
				if(CollectionUtils.isEmpty(list)) {//若查询不到结果集，则修改start为出事状态，退出while循环
					start =0;
					break;
				}
				/**
				 * 若查询的list非空，则遍历发送http post请求回调业务系统，通知交易状态
				 */
				for(ConcurrentHashMap<String,String> map : list) {
					try {
							model.setSign(null);
					        model.setAppid(WeChatConfig.wechat_Appid);
					        model.setMch_id(WeChatConfig.wechat_Mch_id);
					        model.setOut_trade_no(map.get("trade_flow_no"));
							model.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
							model.setSign(Signature.getSign(model, WeChatConfig.wechat_key));
							postResult = HttpsUtil.doPost(WeChatConfig.wechat_orderquery, XMLParser.toXml(model), "utf-8");
							doOrder(postResult,model.getOut_trade_no(),tableFixx);
					} catch(Exception e) {
						logger.error("定时去微信服务器查询未支付成功，失败的订单异常，交易流水号 trade_flow_no==="+map.get("trade_flow_no"), e.fillInStackTrace());
					}
				}
				start +=20;//遍历递增
			}
		}
		logger.info("====WechatQuartzService====定时回调 微信服务器查询交易订单状态 ，更新本地交易流水数据库===结束执行========");
	}
	/**
	 * 解析微信xml数据，进行订单状态处理
	 * @param postResult
	 * @param trade_flow_no
	 * @param year
	 * @return
	 * @throws Exception
	 */
	private boolean doOrder(String postResult,String trade_flow_no,String year) throws Exception{
		Map<String, Object> wechatMap = null;
		String return_code="",result_code="",trade_state="";
		postResult = postResult.replace("<![CDATA[", "");
		postResult = postResult.replace("]]>", "");
		wechatMap = XMLParser.getMapFromXML(postResult);
		return_code= (String)wechatMap.get("return_code");
		if(StringUtils.equals("FAIL", return_code.toUpperCase())) {//只将错误描述更新到流水里，不做其他操作
			wechatTradeFlowDao.updateReturnMsg(trade_flow_no,year,wechatMap);
			return true;
		}
		result_code=(String)wechatMap.get("result_code");
		if(StringUtils.equals("FAIL", result_code.toUpperCase())) {//只将错误描述更新到流水里，不做其他操作
			wechatTradeFlowDao.updateReturnMsg(trade_flow_no,year,wechatMap);
			return true;
		}
		/**
		 * 微信查询返回订单状态
		 * SUCCESS—支付成功 
		   REFUND—转入退款 
		   NOTPAY—未支付 
		   CLOSED—已关闭 
		   REVOKED—已撤销（刷卡支付） 
		   USERPAYING--用户支付中 
		   PAYERROR--支付失败(其他原因，如银行返回失败)
		 */
		int status =1;
		trade_state=(String)wechatMap.get("trade_state");
		if(StringUtils.equals("SUCCESS", trade_state.toUpperCase())) {
			status=2;
		} else if(StringUtils.equals("PAYERROR", trade_state.toUpperCase())) {
			status=3;
		} else if(StringUtils.equals("CLOSED", trade_state.toUpperCase())) {
			status=4;
		}
		wechatMap.put("trade_status", status);
		wechatTradeFlowDao.updateReturnMsg(trade_flow_no,year,wechatMap);
		return true;
	}
	
}
