package com.workmate.app.employee.service;

import java.util.List;

import lombok.Data;

@Data
public class DepartmentVO {
	private String departmentId;		// 부서 번호
	private int departmentHead;			// 부서장 번호
	private String departmentName;		// 부서 이름
	private long budget;				// 예산 
	private String budgetStatus;		// 예산 상태
	private int userNo;					// 사원번호 
	List<TeamVO> teamList;
	private String userName;			// 부서장 이름
	private String teamName;			// 팀이름
	private String userMail;
	private String deptWork;
	private String userPhone;
	private String deptLocation;
	
}
