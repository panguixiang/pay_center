package com.walrushz.pay.dao;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.annotations.Param;

import com.walrushz.pay.common.model.CallBackBusiness;
import com.walrushz.pay.common.model.WeChatModel;
import com.walrushz.pay.common.model.WebChatVo;

/**
 * 微信支付交易流水dao
 * @author panguixiang
 *
 */
public interface WechatTradeFlowDao {
	
	public int getCountByOrderId(@Param("orderId") String orderId, @Param("year") String year);
	
	public void saveWechatTradeFlow(@Param("model") WeChatModel model) throws Exception;
	
	public WebChatVo getWebChatVoByTradeNo(@Param("year") String year,@Param("out_trade_no") String out_trade_no);
	
	
	/**
	 * 修改交易流水
	 * @param trade_flow_no
	 * @param year
	 * @param pay_trade_no
	 * @param trade_type
	 * @param is_notice_client
	 * @throws Exception
	 */
	public void procUpdateWeChatTradeFlow(@Param("map") Map<String,Object> map) throws Exception;
	
	
	/**
	 * 分批查询交易成功，交易关闭的订单
	 * @param start
	 * @param end
	 * @param year
	 * @return
	 */
	public List<ConcurrentHashMap<String,Object>> queryWechatPageByStatusAndIsNoticed(@Param("start") int start,
			@Param("end") int end,@Param("year") String year) ;
	
	
	/**
	 * 根据orderid修改 alipy交易流水通知业务系统is_notice_client字段
	 * @param year
	 * @param isNotice
	 * @param orderId
	 * @throws Exception
	 */
	public void updateWeChatIsNoticeByOrderId(@Param("year") String year, 
			@Param("isNotice") int isNotice,@Param("orderId") String orderId) throws Exception;
	
	
	
	/**
	 * 分批查询交易成功，交易关闭的订单
	 * @param start
	 * @param end
	 * @param year
	 * @return
	 */
	public List<ConcurrentHashMap<String,String>> getWechatFlowNosByStatus(@Param("start") int start,
			@Param("end") int end,@Param("year") String year,@Param("status") Integer status) ;
	
	public void updateReturnMsg(@Param("trade_flow_no") String trade_flow_no,
			@Param("year") String year, @Param("map") Map<String, Object> map);
	
	/**
	 * 批量查询交易流水记录回调对象
	 * @param year
	 * @param orderIdsArr
	 * @return
	 */
	public List<CallBackBusiness> batchSearchByWechatOrderIds(@Param("year") String year, 
			@Param("orderIdsArr") List<String> orderIdsArr);

}
