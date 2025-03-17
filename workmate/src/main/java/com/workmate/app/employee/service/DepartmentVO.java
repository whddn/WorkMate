package com.workmate.app.employee.service;

import java.util.List;

import lombok.Data;

@Data
public class DepartmentVO {
	private String departmentId;		// 부서 번호
	private int departmentHead;			// 부서장 번호
	private String departmentName;		// 부서 이름
	private int budget;					// 예산 
	private String budgetStatus;		// 예산 상태
	
	List<EmpVO> empList;
	List<TeamVO> teamList;
}
