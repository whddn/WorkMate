package com.workmate.app.approval.service;

import lombok.Data;

@Data
public class ApprFormVO {
	private String apprTitle;	// 결재양식 식별자(결재유형)
	private String formName;	// 결재양식 이름
	private String formPath;	// 결재양식 경로
	private String contactNo;	// 결재양식의 담당자번호. 원래 Integer여야 하는데 여기선 String이 되버렸다.
	
	private String department_name;	// 담당부서명. join으로 얻는다
    private String user_name;		// 담당자명. join으로 얻는다
}
