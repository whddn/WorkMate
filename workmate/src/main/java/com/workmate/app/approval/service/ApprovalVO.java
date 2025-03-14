package com.workmate.app.approval.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ApprovalVO {
	private String apprNo;		// 결재번호
	private String apprTitle;	// 결재제목
	private String apprContent;	// 결재내용
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;	// 상신일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expireDate;	// 만료일
	private String apprStatus;	// 결재상태
	private String deptNo;		// 처리부서
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date apprDate;		// 결재완료일
	private String reportNo;	// 보고서 번호
	private Integer userNo;		// 사원번호
	private Integer reserNo;	// 예약번호
	private String apprType;	// 결재유형
	
	private String formName;		// 결재양식 이름 FK
	private String formPath;		// 결재양식 경로 FK
	private String userName;		// 결재자 이름 FK
	private String departmentName;	// 결재 부서 이름 FK
}
