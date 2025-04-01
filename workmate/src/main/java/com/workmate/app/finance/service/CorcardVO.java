package com.workmate.app.finance.service;

import java.util.Date;

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
}
