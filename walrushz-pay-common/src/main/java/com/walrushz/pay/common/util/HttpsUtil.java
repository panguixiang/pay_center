package com.walrushz.pay.common.util;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.walrushz.pay.common.model.WeChatModel;
import com.walrushz.pay.common.wechat.common.RandomStringGenerator;
import com.walrushz.pay.common.wechat.common.Signature;
import com.walrushz.pay.common.wechat.common.XMLParser;
/**
 * https 
 * @author panguixiang
 *
 */
@SuppressWarnings("deprecation")
public class HttpsUtil {

	/**
	 * @DateTime 2012年2月17日下午3:16:41
	 * @Author panguixiang
	 * @QQ 63972012
	 * @Desc https post xml
	 * @param url
	 * @param postDataXML
	 * @param charset
	 * @return String
	 */
	@SuppressWarnings("resource")
	public static String doPost(String url, String postDataXML, String charset) {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);

			StringEntity postEntity = new StringEntity(postDataXML, charset);
			httpPost.addHeader("Content-Type", "text/xml");
			httpPost.setEntity(postEntity);

			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
		return result;
	}

	private static class SSLClient extends DefaultHttpClient {
		public SSLClient() throws Exception {
			super();
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = this.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));
		}
	}

	public static void main(String args[]) {
		String key = "6a154d2cbb854217b84d94a085af32c4";
		WeChatModel model = new WeChatModel();
		model.setAppid("wx400670b9798940c2");
		model.setAttach("支付测试");
		model.setBody("NATIVE支付测试");
		model.setMch_id("1311862201");
		model.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
		model.setNotify_url("http://wxpay.weixin.qq.com/pub_v2/pay/notify.v2.php");
		// model.setOpenid("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");
		model.setOut_trade_no("3232323232");
		model.setProduct_id("00323333333");
		model.setSpbill_create_ip("14.23.150.211");
		model.setTime_start("20160217152332");
		//model.setTotal_fee("100");
		model.setTrade_type("NATIVE");
		String paramStr;
		try {
			paramStr = Signature.getSign(model, key);
			model.setSign(paramStr);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XStream xstream = new XStream();
		xstream.alias("xml", WeChatModel.class);
		String xml = xstream.toXML(model);
		xml = xml.replace("__", "_");
		System.out.println(xml);
		String str = doPost("https://api.mch.weixin.qq.com/pay/unifiedorder", xml, "utf-8");
		str = str.replace("<![CDATA[", "");
		str = str.replace("]]>", "");
		try {
			Map<String, Object> map = XMLParser.getMapFromXML(str);
			System.out.println("------------" + map.get("return_code"));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
