package com.walrushz.pay.common.context;
/**
 * 支付中心全局 常量 
 * @author panguixiang
 *
 */
public class CommonConstant {

	public final static String RSA_PUBLICK_KEY_SUFFIX = "_public.key";
	public final static String RSA_PRIVATE_KEY_SUFFIX = "_private.key";
	
	public final static String ENCODE_UTF_8="UTF-8";
	
    /**
     * get方式请求
     */
    public final static String method_get = "get";

    /**
     * post方式请求
     */
    public final static String method_post = "post";
    
    /** 年月日时分秒(无下划线) yyyyMMddHHmmss */
    public final static String dtLong                  = "yyyyMMddHHmmss";
    
    /** 完整时间 yyyy-MM-dd HH:mm:ss */
    public final static String simple                  = "yyyy-MM-dd HH:mm:ss";
    
    /** 年月日(无下划线) yyyyMMdd */
    public final static String dtShort                 = "yyyyMMdd";
    
    /** 年 yyyy */
    public final static String dtYear                 = "yyyy";
    
    public final static String STATUS="status";
    
    public final static String MESSAGE="message";
    
    public final static String DATA="data";
}
