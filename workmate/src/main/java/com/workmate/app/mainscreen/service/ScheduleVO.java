package com.workmate.app.mainscreen.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ScheduleVO {
	private String scheduleNo;		// 일정번호
	private String scheduleContent;	// 일정제목
	private String scheduleType;	// 일정종류
	private String publicScore;		// 공개범위
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date scheduleStartDate;	// 일정시작날짜
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date scheduleEndDate;	// 일정종료날짜
	private Integer userNo;			// 사원번호
	private Integer mailId;			// 메일ID
	
	private String deptNo;	// 사원이 속한 부서
	private String unit;	// 검색 단위
}
