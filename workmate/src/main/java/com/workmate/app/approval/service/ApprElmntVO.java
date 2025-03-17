package com.workmate.app.approval.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprElmntVO {
	private Integer apprelmntNo;	// 결재요소 번호
	private String apprResult;		// 결재 결과
	private String apprReason;		// 사유
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date apprDate;			// 결재일
	private Integer signNo;			// 사인 번호
	private Integer approver;		// 결재자 번호
	private String deptNo;			// 부서 번호
	private String apprNo;			// 결재번호
	
	private String userName;		// 결재자 이름
	private String teamName;		// 팀명
	private String departmentName;	// 부서명
}