
package com.walrushz.pay.common.util;

import java.util.Date;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
public class UtilDate {
	
	/**
	 * 根据CommonConstant 里的日期规则，来生成日期字符串
	 * @param dateFormat
	 * @return
	 */
	public static String getDate(String dateFormat){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dateFormat);
		return df.format(date);
	}
	
	
}
