package com.workmate.app.approval.service;

import lombok.Data;

@Data
public class ReferenceVO {
	private Integer userNo;	// 참조자 번호
	private String apprNo;	// 결재 번호
	
	private String userName;		// 참조자 이름
	private String teamName;		// 팀 이름
	private String departmentName;	// 부서 이름
}
