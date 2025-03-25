package com.workmate.app.finance.service;

import java.util.Date;

import lombok.Data;

@Data
public class FinanceVO {
	// Report 
	private Integer reportNo;			// 보고서 번호 
	private String reportTitle;			// 보고서 제목 
	private String reportStatus;		// 보고서 결재 상태
	private Date writeDate;				// 보고서 작성 날짜
	private Date updateDate;			// 수정 날짜
	private int deposit;				// 입금액
	private int withdrawal;				// 출금액
	private int balance;				// 잔액
	private String userPosition;		// 직급
	private String reportNote;			// 비고
	private Date apprDate;				// 결재 완료 날짜
	private int userNo;					// 사원번호
	private String userName;			// 사원이름
}
