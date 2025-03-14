package com.workmate.app.employee.service;

import java.util.Date;

import lombok.Data;

@Data
public class EvaluVO {
	// 평가 항목 테이블 (이미 만들어진 문제은행)
	private Integer evaluItemNo; 	// 평가 항목 번호
	private String evaluCompet;		// 평가 역량(평가분야)
	private String evaluContent;		// 평가 역량 설명
	private int evaluScore;			// 평가 점수 (1점~5점)
	private Date regDate;			// 평가 항목 등록 일자
	private String usageStatus;		// 사용 여부
	
	// 평가 양식 (평가 양식 개별...?)
	private Integer evaluNo;			// 평가 번호
	private String orderNo;			// 평가 번호 나열 순서
	private int evaluFormNo;			// 평가 폼 번호 
	
	// 평가 폼 (문제은행에서 랜덤으로 골라서 평가 양식을 생성함)
	private String evaluName;		// 평가 이름 
	private String evaluDescript;	// 평가 설명
	private Date insertDate;			// 평가 폼 등록 날짜
	private Date evaluStart;			// 평가 시작 날짜
	private Date evaluEnd;			// 평가 종료 날짜
	
	// 평가자 그룹 
	
	// 피평가자 그룹 
	
	// 평가 결과 테이블
	private String evaluResultId; 	//
	private int evaluResScore;
}
