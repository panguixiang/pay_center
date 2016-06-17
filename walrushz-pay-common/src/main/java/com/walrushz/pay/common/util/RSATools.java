package com.walrushz.pay.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.walrushz.pay.common.context.CommonConstant;

public class RSATools {
	
	private static Logger logger = Logger.getLogger(RSATools.class);
	
	/**
	 * 生成RSA，公钥，私钥队，并保存到服务根路径下
	 * @param pubkeyfile
	 * @param privatekeyfile
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void makekeyfile(String keyfilePath, String businessKey)
			throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		File file = new File(keyfilePath);
		if(!file.exists() && !file.isDirectory()) {
			file.mkdir(); 
		}
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA/ECB/PKCS1Padding");
		// 初始化密钥对生成器，密钥大小为1024位
		keyPairGen.initialize(1024);
		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		// 得到私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 得到公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// 生成私钥
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(keyfilePath.concat(File.separator).concat(businessKey).concat(CommonConstant.RSA_PRIVATE_KEY_SUFFIX)));
		oos.writeObject(privateKey);
		oos.flush();
		oos.close();
		oos = new ObjectOutputStream(new FileOutputStream(keyfilePath.concat(File.separator).concat(businessKey).concat(CommonConstant.RSA_PUBLICK_KEY_SUFFIX)));
		oos.writeObject(publicKey);
		oos.flush();
		oos.close();
	}

	/**
	 * 加密字符串=Base64(RSA(报文体)
	 * @param Key RSA公钥，私钥  对象
	 * @param data 报文体
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws Exception
	 */
	public static String RSAENcode(Key k, String dataStr) {
		byte[] data;
		try {
			data = dataStr.getBytes(CommonConstant.ENCODE_UTF_8);
			if (k != null) {
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				cipher.init(Cipher.ENCRYPT_MODE, k);
				byte[] dataReturn = null;
				StringBuilder sb = new StringBuilder();  
	            for (int i = 0; i < data.length; i += 100) {  
	                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i,  
	                        i + 100));  
	                sb.append(new String(doFinal));  
	                dataReturn = ArrayUtils.addAll(dataReturn, doFinal);  
	            }  
	            Base64 base64 = new Base64();
				return base64.encodeAsString(dataReturn);
			}
		} catch (Exception e) {
			System.out.println("===================="+e.getMessage());
			logger.error("====RSA加密异常====",e);
			return null;
		}
		return null;
	}
	
	
	
	/**
	 * 解密字符串=Base64(RSA(报文体)
	 * @param Key RSA公钥，私钥  对象
	 * @param data 报文体
	 * @param encrypt 1 加密 0解密
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws Exception
	 */
	public static String RSADecode(Key k, String dataStr) {
		byte[] data;
		try {
			data = dataStr.getBytes(CommonConstant.ENCODE_UTF_8);
			if (k != null) {
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				Base64 base64 = new Base64();
				cipher.init(Cipher.DECRYPT_MODE, k);
				
				data=base64.decode(data);
				
				StringBuilder sb = new StringBuilder();  
	            for (int i = 0; i < data.length; i += 128) {  
	                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i,  
	                        i + 128));  
	                sb.append(new String(doFinal));  
	            }  
	            return sb.toString(); 
			}
		} catch (Exception e) {
			logger.error("====RSA解密异常====",e);
			return null;
		}
		return null;
	}

	public static void main(String[] args) throws Exception {

		/*String file = "d:/pri.txt";
		// makekeyfile(file, "women");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		RSAPrivateKey privKey = (RSAPrivateKey)ois.readObject();
		ois.close();

		ois = new ObjectInputStream(new FileInputStream("d:/pub.txt"));
		RSAPublicKey pubKey = (RSAPublicKey)ois.readObject();
		ois.close();
		Thread.sleep(3000);

		// 使用公钥加密
		String msg = "~O(∩_∩)O哈哈saddddddddddddddddddddddddddddddddddddddddd24二十大法师打范德萨发大水sddsdsdsdsdsfds"
				+ "sfdafdsfsdfsdafsdafasdfsadfsadfsadfasdf4343ds4343ds4343ds"
				+ "4343ds4343ds4343ds4343ds4343ds4343dsdssddsds4343ds4343ds4343ds4343ds4343ds"
				+ "adsfasdfsafsdafsaaaaaaaaaaaaaaaaaaaaaaaaaa~4343ds&sd=dfsd?=324#@";
		String enc = "UTF-8";
		// 使用公钥加密私钥解密
		String result = RSAENcode(privKey, msg);
		System.out.println("加密: " + result);
		
		String deresult = RSADecode(pubKey, result);
		System.out.println("解密: " + deresult);*/


	}
}
