package com.workmate.app.approval.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
	private List<Integer> approverList = new ArrayList<>();	// 결재자 번호 리스트
	private List<Integer> referrerList = new ArrayList<>();	// 참조자 번호 리스트
	private String standard;		// select하는 기준
	private String apprStatusName;	// 결재상태이름
}
