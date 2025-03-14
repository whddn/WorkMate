package com.workmate.app.approval.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ApprElmntVO {
	private Integer apprelmntNo;
	private Integer apprlineNo;
	private Integer approver;
	private String apprResult;
	private String apprReason;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date apprDate;
	private Integer signNo;
	private String deptNo;
	
	private String userName;		// 결재자 이름
	private String teamName;		// 팀명
	private String departmentName;	// 부서명
}