package com.workmate.app.finance.service;

import java.util.Date;

import com.workmate.app.finance.security.AES256Util;

import lombok.Data;

@Data
public class CorcardVO {
	private int corcardNo;			// 법인카드 등록번호
	private String corcardNum;		// 카드 번호
	private int ownerNo;			// 소유주 사원번호
	private Date corcardStart;		// 카드 발급일
	private Date corcardEnd;		// 카드 만료일
	private String corcardComp;		// 카드사
	private String corcardStatus; 	// 카드 상태 (활성화, 만료 등)
	private String bankName;		// 은행명
	private Long mLimit;			// 월한도
	private Long dLimit;			// 일한도
	private int userNo;				// 등록 사원번호
	private String userName;
	
	// 마스킹된 카드 번호
	public String maskedCardNum() {
	    if (corcardNum != null) {
	        String digits = corcardNum.replaceAll("[^0-9]", ""); // 숫자만 추출
	        if (digits.length() == 16) {
	            return digits.substring(0, 4) + "-****-****-" + digits.substring(12);
	        }
	    }
	    return corcardNum; // 마스킹 불가능할 경우 원본 반환
	}
	
	// 암호화 & 복호화
	public String getMaskedCardNumDecrypted() {
	    if (corcardNum != null) {
	        try {
	            System.out.println("📦 corcardNum 원본: " + corcardNum);

	            // 숫자만 있을 때 (평문)
	            if (corcardNum.matches("\\d{16}")) {
	                System.out.println("➡ 평문 카드번호로 판단됨");
	                return corcardNum.substring(0, 4) + "-****-****-" + corcardNum.substring(12);
	            }

	            // 암호문으로 간주하고 복호화
	            String decrypted = AES256Util.decrypt(corcardNum);
	            System.out.println("✅ 복호화 성공: " + decrypted);

	            if (decrypted.length() == 16) {
	                return decrypted.substring(0, 4) + "-****-****-" + decrypted.substring(12);
	            }
	        } catch (Exception e) {
	            System.out.println("❌ 복호화 실패: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	    return "[복호화 실패]";
	}
	}

