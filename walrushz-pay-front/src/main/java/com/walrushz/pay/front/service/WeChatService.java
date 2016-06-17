package com.walrushz.pay.front.service;

import java.math.BigDecimal;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
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

import com.walrushz.pay.common.context.CommonConstant;
import com.walrushz.pay.common.context.ContextCache;
import com.walrushz.pay.common.model.CallBackBusiness;
import com.walrushz.pay.common.model.WeChatModel;
import com.walrushz.pay.common.model.WebChatVo;
import com.walrushz.pay.common.util.HttpUtil;
import com.walrushz.pay.common.util.HttpsUtil;
import com.walrushz.pay.common.util.IpCheck;
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
 * 微信支付初始化支付订单service层
 * @author panguixiang
 *
 */
@Service
public class WeChatService {

	private static Logger logger = Logger.getLogger(WeChatService.class);
	
	@Value("${service_domain}")
	private String service_domain;//回调域名
	
	@Autowired
	private WechatTradeFlowDao wechatTradeFlowDao;
	/**
	 * 初始化支付宝form字符串并持久化交易流水
	 * @param returnMap
	 * @param businessKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> createWechat(Map<String, Object> returnMap, 
			String businessKey,String trade_type, HttpServletRequest request) throws Exception{
		JSONObject json = JSONObject.fromObject(returnMap.get(CommonConstant.DATA));//将解密后的data转化为json
		WeChatModel model = initWechatModel(json,IpCheck.getClientIp(request),businessKey,trade_type);
		if(StringUtils.isNotBlank(model.getParamError())) {
			returnMap.put(CommonConstant.STATUS, "406");
			returnMap.put(CommonConstant.MESSAGE, model.getParamError());
			returnMap.put(CommonConstant.DATA, "");
			return returnMap;
		}
		String xmlStr = XMLParser.toXml(model);
		String str = HttpsUtil.doPost(WeChatConfig.wechat_init_pay_url, xmlStr, "utf-8");
		str = str.replace("<![CDATA[", "");
		str = str.replace("]]>", "");
		Map<String, Object> wechatMap = XMLParser.getMapFromXML(str);
		model.setWechat_return_code((String)wechatMap.get("return_code"));
		model.setWechat_return_msg((String)wechatMap.get("return_msg"));
		if(StringUtils.equals(model.getWechat_return_code(),"SUCCESS")) {
			model.setWechat_result_code((String)wechatMap.get("result_code"));
			model.setWechat_err_code((String)wechatMap.get("err_code"));
			model.setWechat_err_code_des((String)wechatMap.get("err_code_des"));
		}
		if(StringUtils.equals(model.getWechat_result_code(),"SUCCESS")) {
			returnMap.put(CommonConstant.STATUS, "200");
			returnMap.put(CommonConstant.MESSAGE, "");
			returnMap.put(CommonConstant.DATA, wechatMap.get("code_url"));
			model.setCode_url((String)wechatMap.get("code_url"));
		} else {
			returnMap.put(CommonConstant.STATUS, "406");
			returnMap.put(CommonConstant.MESSAGE, model.getWechat_err_code_des());
			returnMap.put(CommonConstant.DATA, "");
		}
		if(StringUtils.equals(model.getWechat_return_code(),"SUCCESS")) {//若为FAIL则微信支付系统并未受理此支付请求成功，所以不将支付流水记录到数据库
			wechatTradeFlowDao.saveWechatTradeFlow(model);
		}
		return returnMap;
	}
	
	/**
	 * 构造alipay支付表单数据
	 * @param json
	 * @param clientIP
	 * @return
	 * @throws Exception
	 */
	private WeChatModel initWechatModel(JSONObject json, String clientIP, String businessKey, String trade_type) throws Exception {
		 // 把请求参数打包成数组
		WeChatModel model = new WeChatModel();
		model.setAttach(UtilDate.getDate(CommonConstant.dtYear));//必须将此传递给微信让其回传，不然无法准确查询本地按年分表记录
     // 商品名称
        if(!json.containsKey("order_id") || json.getString("order_id")=="") {
        	model.setParamError("交易订单号不能为空");
        	return model;
        }
        int count = wechatTradeFlowDao.getCountByOrderId(json.getString("order_id"), RoomUtil.repStr(json.getString("order_id")));
        if(count>0) {
        	model.setParamError("交易订单号已存在");
        	return model;
        }
        
        if(!json.containsKey("body") || json.get("body")=="") {
        	model.setParamError("商品描述不能为空");
        	return model;
        }
        if(!json.containsKey("total_fee") || json.get("total_fee")=="") {
        	model.setParamError("交易金额不能为空");
        	return model;
        } 
        if(!RoomUtil.isDecimalStr(json.getString("total_fee"))) {
        	model.setParamError("交易金额格式不对，浮点型最多保留2位小数，且必须大于0");
        	return model;
        }else {
        	BigDecimal bd=new BigDecimal(json.getString("total_fee"));//因为微信支付传递的金额以分为单位的int类型，所以这里必须将传递过来的元为单位的金额转化
        	bd=bd.multiply(new BigDecimal("100"));
        	model.setTotal_fee(bd.longValue());
        }
        if(trade_type.equals("NATIVE") && (!json.containsKey("product_id") || json.getString("product_id")=="")) {
        	model.setParamError("商品ID不能为空");
        	return model;
        } else {
        	model.setProduct_id(json.getString("product_id"));
        }
        if(!json.containsKey("back_url") || json.getString("back_url")=="") {
        	model.setParamError("支付结果回调地址不能为空");
        	return model;
        }
        if(trade_type.equals("JSAPI")) {
        	model.setOpenid(WeChatConfig.wechat_OpenID);
        }
        /**
         * 开始获得签名sign，其他无需传递给微信支付接口的参数，必须要在完成签名后才可以赋值给model
         */
        model.setOut_trade_no(RoomUtil.findNextVal());
        model.setAppid(WeChatConfig.wechat_Appid);
		model.setBody(json.getString("body"));
		model.setMch_id(WeChatConfig.wechat_Mch_id);
		model.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
		model.setNotify_url(service_domain.concat("/notice/wechat").concat("/payCallBack"));
		model.setSpbill_create_ip(clientIP);
		model.setTime_start(UtilDate.getDate("yyyyMMddHHmmss"));
		model.setTrade_type(trade_type);
		model.setSign(Signature.getSign(model, WeChatConfig.wechat_key));
		
		/**
		 * 完成sign签名后开始其他参数设置
		 */
		
		if(json.containsKey("description") && json.get("description")!="") {
			model.setDescription(json.getString("description"));
		}
		if(json.containsKey("seller_trade_account") && json.get("seller_trade_account")!="") {
			model.setSeller_trade_account(json.getString("seller_trade_account"));
		}
		if(json.containsKey("extra_param") && json.get("extra_param")!="") {
			model.setExtra_param(json.getString("extra_param"));
		}
		if(json.containsKey("payer_busniess_account") && json.get("payer_busniess_account")!="") {
			model.setPayer_busniess_account(json.getString("payer_busniess_account"));
		}
		model.setOrder_id(json.getString("order_id"));
		model.setBack_url(json.getString("back_url"));
		model.setBusiness_key(businessKey);
        return model;
	}
	
	/**
	 * 根据微信支付的回调，来修改交易流水状态
	 * @param trade_flow_no
	 * @param pay_trade_no
	 * @param trade_status
	 * @throws Exception
	 */
	public String updateWeChatTradeFlow(Map<String, Object> map) throws Exception{
		int trade_status=1;
		if(StringUtils.equals(((String)map.get("result_code")).toUpperCase(),"SUCCESS")) {
			trade_status=2;
		} else {//支付失败
			trade_status=3;
		}
		String trade_flow_no = (String)map.get("out_trade_no");
		String year = (String)map.get("attach");
		logger.info("====来自微信支付的回调====,平台交易流水=".concat(trade_flow_no).concat(",微信交易流水=").concat((String)map.get("transaction_id")).
				concat(",微信支付交易状态=".concat(trade_status+"")).concat(",支付宝交易金额=".concat((String)map.get("total_fee"))));
		WebChatVo weChatFlow = wechatTradeFlowDao.getWebChatVoByTradeNo(year, trade_flow_no);
		if(weChatFlow==null) {
			logger.error("根据微信的支付回调，数据库记录不存在此支付流水,支付流水编号trade_flow_no==".concat(trade_flow_no));
			return "SUCCESS";
		}
		if(weChatFlow.getIs_notice_client()==2) {
			logger.info("====来自微信支付的回调==重复回调==此交易流水已经处理完成无需做其他处理====,平台交易流水=".concat(trade_flow_no).concat(",微信交易流水=").concat((String)map.get("transaction_id")).
					concat(",微信支付交易状态=".concat(trade_status+"")).concat(",微信交易金额=".concat((String)map.get("total_fee"))));
			return "SUCCESS";
		}
	    int is_notice_client=1;//是否通知成功业务系统，1=未通知（通知未得到业务系统响应success），2=通知成功并得到业务系统正确响应
		CallBackBusiness callBack = new CallBackBusiness();
		callBack.setBusniessKey(weChatFlow.getBusiness_key());
		callBack.setExtra_param(weChatFlow.getExtra_param());
		callBack.setOrder_id(weChatFlow.getOrder_id());
		callBack.setTotal_fee((String)map.get("total_fee"));
		callBack.setTrade_status(trade_status);
		Object obj = ContextCache.RSAKeyMap.get(callBack.getBusniessKey().concat(CommonConstant.RSA_PUBLICK_KEY_SUFFIX));
		RSAPublicKey rsaPubKey = (RSAPublicKey)obj;
		String enRsa = RSATools.RSAENcode(rsaPubKey,JSONObject.fromObject(callBack).toString());
		try {
			String clientResult = HttpUtil.sendPost(weChatFlow.getBack_url().trim(), enRsa);
			if(StringUtils.equals(clientResult, "success") || clientResult.indexOf("success")>-1) {
				is_notice_client=2;
			}
		} catch(Exception e) {
			logger.error("根据微信的支付回调，通知业务系统时发生异常,支付中心支付流水编号==".concat(trade_flow_no), e.fillInStackTrace());
		}
		/**
		 * 调用存储过程，执行更新流水操作,修改交易流水状态
		 *
		 */
		if(!map.containsKey("return_msg") || map.get("return_msg")==null ) {
			map.put("return_msg", "");
		}
		if(!map.containsKey("err_code_des") || map.get("err_code_des")==null) {
			map.put("err_code_des", "");
		}
		if(!map.containsKey("err_code") || map.get("err_code")==null) {
			map.put("err_code", "");
		}
		if(!map.containsKey("err_code_des") || map.get("err_code_des")==null) {
			map.put("err_code_des", "");
		}
		map.put("order_id", weChatFlow.getOrder_id());
		map.put("trade_status", trade_status);
		map.put("is_notice_client", is_notice_client);
		map.put("yearstr", year);
		wechatTradeFlowDao.procUpdateWeChatTradeFlow(map);
		return "SUCCESS";
	}
	
	
	/**
	 * 批量查询业务系统微信交易订单状态
	 * @param orderIds
	 * @return
	 */
	public Map<String, Object> batchSearchByWechatOrderIds(Map<String, Object> returnMap) throws Exception{
		
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
			list = wechatTradeFlowDao.batchSearchByWechatOrderIds(entry.getKey(),entry.getValue());
			if(CollectionUtils.isNotEmpty(list)) {
				batchList.addAll(list);
			}
		}
		returnMap.put(CommonConstant.DATA, batchList);
		return returnMap;
	}
	
}
