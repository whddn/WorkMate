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
	private String formStatus;		// 평가 폼의 상태 (대기 중 / 진행 중 / 평가 완료)

	// 평가 양식 
	private Integer evaluNo;		// 평가 번호
	private String orderNo;			// 평가 번호 나열 순서
	private Integer evaluFormNo;	// 평가 폼 번호 
	private Double avgScore;		// 평가 평균 점수
	
	// 평가 폼 (문제은행에서 랜덤으로 골라서 평가 양식을 생성함)
	private String evaluName;		// 평가 이름 
	private String evaluDescript;	// 평가 설명
	private Date insertDate;		// 평가 폼 등록 날짜
	private Date evaluStart;		// 평가 시작 날짜
	private Date evaluEnd;			// 평가 종료 날짜
	List<EmpVO> empOne;
	List<TeamVO> teamList;
	
	private Integer userNo;			// 유저 번호 

	private String userName;		// 유저 이름
	private String departmentName;	// 부서명
	private String teamName;		// 팀이름
	private String teamNo;			// 팀번호
	private String userPosition;	// 직급
	List<EvaluVO> evaluItem;		// AJAX로 보낼 배열 이름과 동일하게 (폼 양식 등록) 
	
	// 평가자 그룹 
	private String evaluGroupId;		// 평가자 그룹 아이디
	private String usageStatus;		// 사용 여부 (평가자가 제출했는지 여부)
	
	// 피평가자 그룹 
	private String evaluateeGroupId;	// 피평가자 그룹 아이디
	
	// 평가 결과 테이블
	private String evaluResultId; 		// 평가 결과 ID
	private Integer evaluateeUserNo;	// 피평가자 사번
	private Integer evaluatorUserNo;
	
	private Double otherAvgScore;	// 다른 사원의 평균 점수 
	private String resultStatus;	// 평가 결과의 상태 (임시 저장 / 제출 완료)
	
	
}
