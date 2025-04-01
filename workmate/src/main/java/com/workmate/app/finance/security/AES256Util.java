package com.workmate.app.finance.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class AES256Util {
// 법인카드 암호화
	  private static final String key = "1234567890abcdef"; // 16자리 (128bit) 비밀키
	    private static final String iv = "abcdef1234567890";  // 16자리 IV

	    // 암호화
	    public static String encrypt(String plainText) throws Exception {
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
	        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
	        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

	        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
	        return Base64.getEncoder().encodeToString(encrypted);
	    }

	    // 복호화
	    public static String decrypt(String cipherText) throws Exception {
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
	        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
	        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

	        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
	        byte[] decrypted = cipher.doFinal(decodedBytes);
	        return new String(decrypted, StandardCharsets.UTF_8);
	    }
	}

