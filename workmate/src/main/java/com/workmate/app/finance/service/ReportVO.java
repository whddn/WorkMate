package com.workmate.app.finance.service;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ReportVO {
	// Report 
	private Integer reportNo;			// 보고서 번호 
	private String reportTitle;			// 보고서 제목 
	private String reportStatus;		// 보고서 결재 상태
	private Date writeDate;				// 보고서 작성 날짜
	private Date updateDate;			// 수정 날짜
	private Integer totalDep;				// 입금액
	private Integer totalDrawal;				// 출금액
	private Integer totalBal;				// 잔액
	private String userPosition;		// 직급
	private String reportNote;			// 비고
	private Date apprDate;				// 결재 완료 날짜
	private int userNo;					// 사원번호
	private String userName;			// 사원이름
	private String teamNo;				// 팀번호
	private String teamName;
	private String departmentName;
	private String departmentId;		// 부서번호
	
	List<ReportVO> transHistoryList;	// 거래내역 리스트

	
	// transHistory
	private Integer transId;			// 거래번호
	private String transType;			// 거래 타입 (입금/출금)
	private Date transDate;				// 거래 날짜
	private Date transStart;			// 총 거래 시작일
	private Date transEnd;				// 총 거래 마감일
	private Integer balance;				// 잔액
	private String purposeUse;			// 상세
	private String transMethod;			// 거래 방법
	private Integer deposit;				// 입금액
	private Integer withdrawal;				// 출금액
}
