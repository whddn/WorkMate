package com.workmate.app.attendance.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class WorkVO {
	
		// work table 근태
		private String workStatus;	//근무상태
		private int statusCount;	//근무상태수	
		private Date workDate;	    //출근일시   
		private Date startWork;	    //출근일시   
		private Date afterWork;		//퇴근일시
		private Integer workNo;		//근태번호
		private double workTime;		//근무시간
		private int totalWork;		//총근무시간
		private double addWorkTime; //연장근무시간
		private Integer userNo;		//사원번호
		private String userName;		//사원번호
		private String userId;		//사원아이디
		private String teamName;		//사원아이디
		
		private double totalWorkTime; // 현재달 총 근무시간 
		private double remainWorkTime; // 남은 근무시간
		
		// annual table 연차내역
		private String annualNo;	//연차번호
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date annualStart;	//연차시작일
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date annualEnd;		//연차종료일
		private int annualCount;	//연차일수
		private String annualType;	//연차유형
		private String apprNo;		//결재번호
		
		
		// occ_annual table 발생연차
		private int occAnnualNo;	//발생연차번호
		private int workYear;		//근무년도
		private int useAnnualYear;	//사용년도
		private int occAnnual;		//발생연차
		private int checkStatus;	//정산여부
		private int usedAnnualCount;	//사용연차
		private int remainAnlLeave;		//남은연차
		
		// 전체사원페이지
		private int allStatusCount;	//근무상태수	
		private int nstartCount;	//출근미체크수	
		private int nafterCount;	//퇴근미체크수	
		private int addWorkCount;	//연장근무총합
		
		
		
		// 검색날짜
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date stDate;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date endDate;
		
		// 결재관련
		private Date apprDate; // 결재 상신 날짜
		private String apprContent; //결재 내용
}
