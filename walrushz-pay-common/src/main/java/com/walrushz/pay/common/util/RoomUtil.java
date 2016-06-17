package com.walrushz.pay.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class RoomUtil {

	/**
	 * 唯一码生成器 返回值26位 数字字符串（yyyyMMddHHmmssSSS+11位随机数）
	 * 
	 * @param name
	 * @return
	 */
	public static String findNextVal() {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		StringBuilder builder = new StringBuilder();
		builder.append(formatDate.format(new Date()));
		return builder.append(String.valueOf(Math.round(Math.random() * 899999999 + 100000000))).toString();
	}

	/**
	 * 校验decimalStr 是否为金额格式（2位小数）
	 * 
	 * @param decimalStr
	 * @return
	 */
	public static boolean isDecimalStr(String decimalStr) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern
				.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后3位的数字的正则表达式
		java.util.regex.Matcher match = pattern.matcher(decimalStr);
		if (match.matches()) {// 判断是否为浮点型
			return checkIsZero(decimalStr);
		} else {
			return false;
		}

	}

	/**
	 * 判断是否为0
	 * 
	 * @param decimalStr
	 * @return
	 */
	private static boolean checkIsZero(String decimalStr) {
		decimalStr = decimalStr.replace("0", "");
		if (StringUtils.isBlank(decimalStr) || decimalStr.equals(".")) {
			return false;
		}
		return true;
	}

	/**
	 * 根据orderIds字符串集合获得按年分组的orderId字符集合Map对象
	 * 
	 * @param orderIds
	 * @return
	 */
	public static Map<String, List<String>> groupOrderIdsByYear(String[] arr) throws Exception {
		String sun;
		Map<String, List<String>> ordersGroupMap = new HashMap<String, List<String>>();
		List<String> list = null;
		for (String para : arr) {
			if (StringUtils.isBlank(para)) {// 若为空则无需理会，挑个此次循环继续
				continue;
			}
			sun = repStr(para);// 获得orderId前4位字符的字符串-》年份
			if (ordersGroupMap.get(sun) != null) {// 将相同年份的订单放入一个list容器
				list = ordersGroupMap.get(sun);
				list.add(para);
			} else {// 将相同年份的订单放入一个list容器
				list = new ArrayList<String>();
				list.add(para);
				ordersGroupMap.put(sun, list);
			}
		}
		return ordersGroupMap;

	}

	/**
	 * 获得orderId 前4位字符的子字符串
	 * 
	 * @param orderId
	 * @return
	 */
	public static String repStr(String orderId) {
		String reg = "^(.{4}).*$";
		reg = orderId.replaceAll(reg, "$1");
		return reg;
	}

	/**
	 * 字符串数字减值
	 * 
	 * @param strNumber
	 * @param val
	 * @return
	 */
	public static String strNumberSub(String strNumber, int val) {
		return String.valueOf(Integer.parseInt(strNumber) - val);
	}

	/**
	 * 获取某年最后一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static String getYearLast(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String sDate = f.format(currYearLast);
		return sDate.toString();
	}

	/**
	 * 计算当前now日期和过去old日期之间的天数差
	 * 
	 * @param now
	 * @param old
	 * @return
	 * @throws Exception
	 */
	public static int checkDateDiffDay(String now, String old) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 定义时间格式
		Date d1 = df.parse(now);// 最后时间
		Date d2 = df.parse(old);// 起始时间
		long diff = d1.getTime() - d2.getTime();// 算出两个时间的秒数差
		long days = diff / (1000 * 60 * 60 * 24);
		return (int) days;
	}

	/**
	 * 获得当前年和上一年的集合
	 * 
	 * @param nowYear
	 * @return
	 * @throws Exception
	 */
	public static List<String> yearArr(String nowYear) throws Exception {
		String oldYear = RoomUtil.strNumberSub(nowYear, 1);// 获得去年的年份
		List<String> flowTableArr = new ArrayList<String>();// 查询当前年份和上一年份的交易流水表，防止跨年
		int dayDiff = RoomUtil.checkDateDiffDay(UtilDate.getDate("yyyy-MM-dd"),
				RoomUtil.getYearLast(Integer.parseInt(oldYear)));
		if (dayDiff > 30) {// 如果大于30天，则无需再查询去年表
			flowTableArr.add(nowYear);
		} else {
			flowTableArr.add(nowYear);
			flowTableArr.add(oldYear);
		}
		return flowTableArr;
	}

	public static void main(String args[]) {
		try {
			System.out.println(checkDateDiffDay("2015-10-10", "2015-10-02"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
