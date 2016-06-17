package com.walrushz.pay.common.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	 // 合作身份者ID，以2088开头由16位纯数字组成的字符串
    public final static String alipay_partner        = "2088021874372297";
    // 收款支付宝账号
    public final static String alipay_seller_email   = "100357158@qq.com";
    // 商户的私钥
    public final static String alipay_RSA_priv_key            = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANk5HRoGISB3/00O50ZeXh1xirTdR3PYEmZtlvnuiGMtQGKIdML9oZaGTQIIxKwqezQxAndlYcIkDGs+LM5lKWoIlR1eqHoCPj+7R9Ml4MsFRd5v27ePyUOvWqjgQ/QHbdMjBtrPoyO/QTxZCu6dsRhrZBcEZ4K66F0Yzz7L/DpbAgMBAAECgYBMrzz20vMe4z3dlkVPMkFHY63z66ot2zmjr0x9Lxg26uKv5jxSGffRi9fjv/SuUtDru+GDBOdeAhvgQDLRKiVUAvAE59WwzPPF3e9wXhkP2VRIiipKniA4gTx7xbBq02YqAN8BrhpRNY19k7wWqqX/AX+FsZ3RUUKbWly5UNrUAQJBAO8Gr4h4ACkxei6f4dRMnqtFwH9LekoZdJL6Ozpjbp0rZPhNCk9u8B//DldkKoe000MVSQEId9L+JWdqYgVYfLECQQDophDNwFBkJx6U6w9EBPhzzXZ6F59CYU6QSGJLGP3Cl3A2qjTqHkGcFg/lZSViJkyCnVS6ZFJDZrbXXkVu9XrLAkBxmz7g4RBKg+6rrGgcjJI5m3yDxC8KexDSLsbp2IqhfxeNgKhu2q6ctpBtbvlb6NEkqEA7knfJO+wxO/n8ynLxAkBH4MbgSsKLrupr6BPXWBHK9XfdUN8LizTSrwpvWVFNhaxNt1iR3Lc4mjhfD1j1cg/Bawp6a/RejqV+9MlO9tjxAkATNHLiF6J/oc92SyBedrz4Boys5vNSJkq82c1zX7WMIYJDT+zLmUuwRMqfCG6dJ56f63LC+XZUK8iqQzNKhZC6";
    public final static String alipay_create_direct_pay_by_user = "create_direct_pay_by_user";
    
    public final static String sign_type      = "MD5";
    
    public final static String alipay_payment_type_1 = "1";
    
    public final static String qr_pay_mode = "2";
    public static String key_md5         = "mca7jermffyttxp3ww6nbqf5zkfvbbeq";
}
