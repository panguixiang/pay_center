package com.walrushz.pay.front.service;

import java.math.BigDecimal;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.walrushz.pay.common.alipay.config.AlipayConfig;
import com.walrushz.pay.common.alipay.util.AlipayStatusEnum;
import com.walrushz.pay.common.alipay.util.AlipaySubmit;
import com.walrushz.pay.common.context.CommonConstant;
import com.walrushz.pay.common.context.ContextCache;
import com.walrushz.pay.common.model.AlipayTradeFlow;
import com.walrushz.pay.common.model.CallBackBusiness;
import com.walrushz.pay.common.util.HttpUtil;
import com.walrushz.pay.common.util.IpCheck;
import com.walrushz.pay.common.util.RSATools;
import com.walrushz.pay.common.util.RoomUtil;
import com.walrushz.pay.common.util.UtilDate;
import com.walrushz.pay.dao.AlipayTradeFlowDao;

import net.sf.json.JSONObject;

/**
 * alipya支付 业务service
 * @author panguixiang
 *
 */
@Service
public class AlipayService {
	
	private static Logger logger = Logger.getLogger(AlipayService.class);

	@Value("${service_domain}")
	private String service_domain;//回调域名
	
	@Autowired
	private AlipayTradeFlowDao alipayTradeFlowDao;
	
	/**
	 * 初始化支付宝form字符串并持久化交易流水
	 * @param returnMap
	 * @param businessKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> createAliPayForm(Map<String, Object> returnMap, 
			String businessKey, HttpServletRequest request) throws Exception{
		
		JSONObject json = JSONObject.fromObject(returnMap.get(CommonConstant.DATA));//将解密后的data转化为json
		Map<String, String> map = initAliPayForm(json, IpCheck.getClientIp(request),businessKey);//造alipay支付表单数据
		if (map.get("error") != null) {
			returnMap.put(CommonConstant.STATUS, "406");
			returnMap.put(CommonConstant.MESSAGE, map.get("error"));
			returnMap.put(CommonConstant.DATA, "");
			return returnMap;
		} else {
			map.put("businessKey", businessKey);
			returnMap.put(CommonConstant.DATA, createFormAndDb(map, json));//执行持久化操作并生成支付宝form表单字符串
		}
		return returnMap;
	}
	
	/**
	 * 根据支付宝的回调，来修改交易流水状态
	 * @param trade_flow_no
	 * @param pay_trade_no
	 * @param trade_status
	 * @throws Exception
	 */
	public String updateAlipayTradeFlow(HttpServletRequest request) throws Exception{
		
		String tradeFlowNo = request.getParameter("out_trade_no");
		String tradeStatus = request.getParameter("trade_status");//交易状态
		String year = request.getParameter("extra_common_param");//公用回传参数 值为发起交易时的年份
		String total_fee = request.getParameter("total_fee");
		String buyer_email = request.getParameter("buyer_email");
		String trade_no=request.getParameter("trade_no");
		String seller_email=request.getParameter("seller_email");
		logger.info("====来自支付宝的回调====,平台交易流水=".concat(tradeFlowNo).concat(",支付宝交易流水=").concat(trade_no).
				concat(",支付宝交易状态=".concat(tradeStatus)).concat(",支付宝交易金额=".concat(total_fee)));
		AlipayTradeFlow tradeFlow = alipayTradeFlowDao.getAlipayTradeFlowByFlowNo(year, tradeFlowNo);
		if(tradeFlow==null) {
			logger.error("根据支付宝的支付回调，数据库记录不存在此支付流水,支付流水编号==".concat(tradeFlowNo));
			return "success";
		}
		if(tradeFlow.getIs_notice_client()==2) {
			logger.info("====来自支付宝的回调==重复回调==此交易流水已经处理完成无需做其他处理====,平台交易流水=".concat(tradeFlowNo).concat(",支付宝交易流水=").concat(trade_no).
					concat(",支付宝支付交易状态=".concat(tradeStatus)).concat(",支付宝交易金额=".concat(total_fee)));
			return "success";
		}
		if(total_fee==null) {
			logger.error("支付宝回调的支付金额为空=支付中心交易流水号=".concat(tradeFlowNo).concat(", 支付宝交易流水号==".concat(trade_no)));
			return "fail";
		}
		int typeStatus = AlipayStatusEnum.getIndex(tradeStatus.toUpperCase());
	    int is_notice_client=1;//是否通知成功业务系统，1=未通知（通知未得到业务系统响应success），2=通知成功并得到业务系统正确响应
		if(typeStatus==2 || typeStatus==3) {//交易成功2 或者  交易关闭3  时 才发http请求通知客户端
			CallBackBusiness callBack = new CallBackBusiness();
			callBack.setBusniessKey(tradeFlow.getBusinessKey());
			callBack.setExtra_param(tradeFlow.getExtraParam());
			callBack.setOrder_id(tradeFlow.getOrderId());
			callBack.setTotal_fee(total_fee);
			callBack.setTrade_status(typeStatus);
			Object obj = ContextCache.RSAKeyMap.get(callBack.getBusniessKey().concat(CommonConstant.RSA_PUBLICK_KEY_SUFFIX));
			RSAPublicKey rsaPubKey = (RSAPublicKey)obj;
			String enRsa = RSATools.RSAENcode(rsaPubKey,JSONObject.fromObject(callBack).toString());
			try {
				String clientResult = HttpUtil.sendPost(tradeFlow.getBack_url().trim(), enRsa);
				if(StringUtils.equals(clientResult, "success") || clientResult.indexOf("success")>-1) {
					is_notice_client=2;
				}
			} catch(Exception e) {
				logger.error("根据支付宝的支付回调，通知业务系统时发生异常,支付中心支付流水编号==".concat(tradeFlowNo), e.fillInStackTrace());
			}
		}
		/**
		 * 调用存储过程，执行更新流水操作
		 */
		Map<String,Object> proMap = new HashMap<String,Object>();
		proMap.put("buyer_email",buyer_email);
		proMap.put("trade_no",trade_no);
		proMap.put("seller_email",seller_email);
		proMap.put("total_fee",new BigDecimal(total_fee).setScale(3, BigDecimal.ROUND_HALF_UP));//字符串转化为decimal类型
		proMap.put("tradeFlowNo",tradeFlowNo);
		proMap.put("typeStatus",typeStatus);
		proMap.put("orderId",tradeFlow.getOrderId());
		proMap.put("year",year);
		proMap.put("isNoticed",is_notice_client);
		/**
		 * 修改交易流水状态
		 */
		alipayTradeFlowDao.procUpdateAlipayTradeFlow(proMap);
		return "success";
	}
	
	/**
	 * 批量查询业务系统支付宝交易订单状态 
	 * @param orderIds
	 * @return
	 */
	public Map<String, Object> batchSearchByApliyOrderIds(Map<String, Object> returnMap) throws Exception{
		
		Object obj =  returnMap.get(CommonConstant.DATA);//从解密的map里获得data参数
		if(obj==null) {//判断是否为null
			returnMap.put(CommonConstant.STATUS, "406");
			returnMap.put(CommonConstant.MESSAGE, "订单编号不能为空");
			returnMap.put(CommonConstant.DATA, "");
			return returnMap;
		}
		String[] orderArr = ((String)obj).split(",");//split获得订单数组列表
		if(orderArr==null || orderArr.length==0 || orderArr.length>50) {//如果过大，或则长度为0则返回错误提示
			returnMap.put(CommonConstant.STATUS, "406");
			returnMap.put(CommonConstant.MESSAGE, "订单编号不能为空且少于50个");
			returnMap.put(CommonConstant.DATA, "");
			return returnMap;
		}
		Map<String, List<String>> groupMap = RoomUtil.groupOrderIdsByYear(orderArr);//将orderArr按年份分组获得分组对象
		/**
		 * 遍历分组对象，合并每次分组获得的数据库记录对象集合
		 */
		Set<Entry<String,  List<String>>> entries = groupMap.entrySet();
		List<CallBackBusiness> batchList = new ArrayList<CallBackBusiness>();
		List<CallBackBusiness> list = null;
		for (Entry<String,  List<String>> entry : entries) {
			list = alipayTradeFlowDao.batchSearchByApliyOrderIds(entry.getKey(),entry.getValue());
			if(CollectionUtils.isNotEmpty(list)) {
				batchList.addAll(list);
			}
		}
		returnMap.put(CommonConstant.DATA, batchList);
		return returnMap;
	}
	/**
	 * 构造alipay支付表单数据
	 * @param json
	 * @param clientIP
	 * @return
	 * @throws Exception
	 */
	private Map<String,String> initAliPayForm(JSONObject json, String clientIP, String businessKey) throws Exception {
		 // 把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("extra_common_param", UtilDate.getDate(CommonConstant.dtYear));//必须将此传递给支付宝让其回传，不然无法准确查询本地按年分表记录
     // 商品名称
        if(!json.containsKey("order_id") || json.get("order_id")=="") {
        	sParaTemp.put("error", "交易订单号不能为空");
        	return sParaTemp;
        }
        if(alipayTradeFlowDao.getAlipayTradeCountByOrderId(RoomUtil.repStr(json.getString("order_id")),json.getString("order_id"))>0) {
        	sParaTemp.put("error", "交易订单号已存在");
        	return sParaTemp;
        }
        sParaTemp.put("service", AlipayConfig.alipay_create_direct_pay_by_user);
        sParaTemp.put("partner", AlipayConfig.alipay_partner);
        sParaTemp.put("_input_charset", CommonConstant.ENCODE_UTF_8);
        if(json.containsKey("seller_email") && json.get("seller_email")!="") {
        	sParaTemp.put("seller_email", json.getString("seller_email"));
        } else {
        	sParaTemp.put("seller_email", AlipayConfig.alipay_seller_email);
        }
        if(json.containsKey("buyer_email") && json.get("buyer_email")!="") {
        	sParaTemp.put("buyer_email", json.getString("buyer_email"));
        }
        // 支付类型
        sParaTemp.put("payment_type", AlipayConfig.alipay_payment_type_1);
        sParaTemp.put("notify_url", service_domain.concat("/notice/alipay").concat("/payCallBack"));
        // FIXME 同步回调地址
        if(json.containsKey("return_url") && json.get("return_url")!="") {
        	sParaTemp.put("return_url", json.getString("return_url"));
        }
        // 商户订单号 即 支付中心交易流水号
        sParaTemp.put("out_trade_no", RoomUtil.findNextVal());
        // 商品名称
        if(!json.containsKey("subject") || json.get("subject")=="") {
        	sParaTemp.put("error", "商品名称不能为空");
        	return sParaTemp;
        }
        sParaTemp.put("subject", json.getString("subject"));
        // 付款金额
        if(!json.containsKey("total_fee") || json.get("total_fee")=="") {
        	sParaTemp.put("error", "交易金额不能为空");
        	return sParaTemp;
        }
        if(!RoomUtil.isDecimalStr(json.getString("total_fee"))) {
        	sParaTemp.put("error", "交易金额格式不对，浮点型最多保留3位小数，且必须大于0");
        	return sParaTemp;
        }
        sParaTemp.put("total_fee", json.getString("total_fee"));
        // 商品描述
        if(json.containsKey("body") && json.get("body")!="") {
        	sParaTemp.put("body", "商户名称：海象网络");
        }
        // FIXME 商品展示地址
        if(json.containsKey("show_url") && json.get("show_url")!="") {
        	sParaTemp.put("show_url", json.getString("show_url"));
        }
     
            // 防钓鱼时间戳
        sParaTemp.put("anti_phishing_key", AlipaySubmit.query_timestamp());
        sParaTemp.put("exter_invoke_ip", clientIP);
        if(json.containsKey("default_login")) {
        	sParaTemp.put("default_login", json.getString("default_login"));
        }
        sParaTemp.put("qr_pay_mode", AlipayConfig.qr_pay_mode);
        return sParaTemp;
	}
	/**
	 * 执行持久化操作并生成支付宝form表单字符串
	 * @param map
	 * @param json
	 * @return
	 * @throws Exception
	 */
	private String createFormAndDb(Map<String,String> map,JSONObject json) throws Exception{
		/**
		 * 将alipay支付表单数据map转化为字符串form
		 */
		String form = AlipaySubmit.buildRequest(map, CommonConstant.method_post, "确认");
		/**
		 * 持久化alipay支付流水到交易交易中心
		 */
		alipayTradeFlowDao.saveAlipayTradeFlow(map, json);
		return form;
	}
	
}
