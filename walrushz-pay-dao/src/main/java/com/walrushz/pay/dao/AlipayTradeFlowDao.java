package com.walrushz.pay.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.annotations.Param;

import com.walrushz.pay.common.model.AlipayTradeFlow;
import com.walrushz.pay.common.model.CallBackBusiness;

import net.sf.json.JSONObject;
/**
 * 支付宝交易流水dao
 * @author panguixiang
 *
 */
public interface AlipayTradeFlowDao {

	/**
	 * 持久化交易流水
	 * @param map
	 * @param json
	 * @throws Exception
	 */
	public void saveAlipayTradeFlow(@Param("map") Map<String,String> map, @Param("json") JSONObject json) throws Exception;
	/**
	 * 修改交易流水
	 * @param trade_flow_no
	 * @param year
	 * @param pay_trade_no
	 * @param trade_type
	 * @param is_notice_client
	 * @throws Exception
	 */
	public void procUpdateAlipayTradeFlow(@Param("map") Map<String,Object> map) throws Exception;
	/**
	 * 根据交易流水号获得交易流水对象
	 * @param year
	 * @param flowNo
	 * @return
	 */
	public AlipayTradeFlow getAlipayTradeFlowByFlowNo(@Param("year") String year,@Param("flowNo") String flowNo);
	/**
	 * 根据业务系统订单号，获得交易流水数量
	 * @param year
	 * @param orderId
	 * @return
	 */
	public int getAlipayTradeCountByOrderId(@Param("year") String year,@Param("orderId") String orderId);
	/**
	 * 批量查询交易流水记录回调对象
	 * @param year
	 * @param orderIdsArr
	 * @return
	 */
	public List<CallBackBusiness> batchSearchByApliyOrderIds(@Param("year") String year,@Param("orderIdsArr") List<String> orderIdsArr);
	/**
	 * 分批查询交易成功，交易关闭的订单
	 * @param start
	 * @param end
	 * @param year
	 * @return
	 */
	public List<ConcurrentHashMap<String,Object>> queryPageListByStatusAndIsNoticed(@Param("start") int start,
			@Param("end") int end,@Param("year") String year) ;
	/**
	 * 根据orderid修改 alipy交易流水通知业务系统is_notice_client字段
	 * @param year
	 * @param isNotice
	 * @param orderId
	 * @throws Exception
	 */
	public void updateIsNoticeByOrderId(@Param("year") String year, 
			@Param("isNotice") int isNotice,@Param("orderId") String orderId) throws Exception;
}
