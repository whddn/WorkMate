package com.workmate.app.finance.security;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class AES256Util {
// 법인카드 암호화
	private final String secretKey = "1234567890123456"; // 16자리 키 
	private final String transformation = "AES/ECB/PKCS5Padding";
	
	public String encrypt(String text) throws Exception {
	      Cipher cipher = Cipher.getInstance(transformation);
	        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	        return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes("UTF-8")));
	    }

	    public String decrypt(String encryptedText) throws Exception {
	        Cipher cipher = Cipher.getInstance(transformation);
	        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");
	        cipher.init(Cipher.DECRYPT_MODE, key);
	        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)), "UTF-8");
	    }
	}

