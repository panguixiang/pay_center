package com.walrushz.pay.front.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walrushz.pay.common.model.BusnieseWhiteIp;
import com.walrushz.pay.dao.WhiteListDao;

@Service("whiteListService")
public class WhiteListService {

	@Autowired
	private WhiteListDao whiteListDao;

	public List<BusnieseWhiteIp> getBusnieseWhiteIps() {
		return whiteListDao.getBusnieseWhiteIps();
	}
}
