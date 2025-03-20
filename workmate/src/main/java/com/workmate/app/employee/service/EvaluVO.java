package com.workmate.app.employee.service;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class EvaluVO {
	
	public EvaluVO() {
		
	}
 	public EvaluVO(Integer evaluItemNo, String evaluContent) {
		super();
		this.evaluItemNo = evaluItemNo;
		this.evaluContent = evaluContent;
	}
	// 평가 항목 테이블 (이미 만들어진 문제은행)
	private Integer evaluItemNo; 	// 평가 항목 번호
	private String evaluCompet;		// 평가 역량(평가분야)
	private String evaluContent;	// 평가 역량 설명
	private int evaluScore;			// 평가 점수 (1점~5점)
	private Date regDate;			// 평가 항목 등록 일자
	private String usageStatus;		// 사용 여부
	private String evaluSummary;	// 평가 요약
	// 평가 양식 (평가 양식 개별...?)
	private Integer evaluNo;		// 평가 번호
	private String orderNo;			// 평가 번호 나열 순서
	private int evaluFormNo;		// 평가 폼 번호 
	
	//List<DepartmentVO> dptList;
	// 평가 폼 (문제은행에서 랜덤으로 골라서 평가 양식을 생성함)
	private String evaluName;		// 평가 이름 
	private String evaluDescript;	// 평가 설명
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date insertDate;		// 평가 폼 등록 날짜
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date evaluStart;		// 평가 시작 날짜
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date evaluEnd;			// 평가 종료 날짜
	
	private int userNo;				// 유저 번호 
	private String userName;		// 유저 이름
	private String departmentName;	// 부서명
	private String teamName;		// 팀이름
	private String teamNo;			// 팀번호
	private String userPosition;	// 직급
	List<EvaluVO> evaluItem;		// AJAX로 보낼 배열 이름과 동일하게
	// 평가자 그룹 
	private String evaluGroupId;		// 평가자 그룹 아이디
	// 피평가자 그룹 
	private String evaluateeGroupId;	// 피평가자 그룹 아이디
	// 평가 결과 테이블
	private String evaluResultId; 	//
	private int evaluResScore;
}
