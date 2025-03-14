package com.workmate.app.attendance.service;

import java.util.Date;

import lombok.Data;

@Data
public class WorkVO {
	
		// work table 근태
		private String workStatus;	//근무상태
		private Date workDate;	    //출근일시   
		private Date startWork;	    //출근일시   
		private Date afterWork;		//퇴근일시
		private Integer workNo;		//근태번호
		private int workTime;		//근무시간
		private int totalWork;		//총근무시간
		private Integer userNo;		//사원번호
		
		//@DateTimeFormat(pattern = "yyyy-MM-dd")
		//private Date hireDate;
		
		// annual table 연차내역
		private String annualNo;	//연차번호
		private Date annualStart;	//연차시작일
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
}
