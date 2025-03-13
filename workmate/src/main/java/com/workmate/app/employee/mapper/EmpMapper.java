package com.workmate.app.employee.mapper;

import java.util.List;

import com.workmate.app.employee.service.EmpVO;

public interface EmpMapper {
	// 단건 조회 
	public EmpVO selectOneEmp(EmpVO empVO);
	
	// 등록 
	public int insertOneEmp(EmpVO empVO);
	
	// 팀명 조회
	public List<EmpVO> selectAllEmpTeam();
	
	// 직급 조회
	public List<EmpVO> selectAllPosition();
}
