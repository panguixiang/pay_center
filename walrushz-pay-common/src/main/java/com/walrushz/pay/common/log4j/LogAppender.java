package com.walrushz.pay.common.log4j;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Priority;
/**
 * 自定义log4j 输出级别
 * @author panguixiang
 *
 */
public class LogAppender extends DailyRollingFileAppender {

	@Override
	public boolean isAsSevereAsThreshold(Priority priority) {
		// 只判断是否相等，而不判断优先级,防止高级别日志输出时输出低级别的日志
		return this.getThreshold().equals(priority);
	}
}
