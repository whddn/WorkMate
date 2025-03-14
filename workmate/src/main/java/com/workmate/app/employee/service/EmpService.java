package com.workmate.app.employee.service;

import java.util.List;

public interface EmpService {
	
	// 조직도 페이지 조회
	public EmpVO findOrganPage(EmpVO empVO);
	
	// 단건 조회 (조직도)
	public EmpVO findEmpByEmpNo(EmpVO empVO);
	
	// 등록 
	public int inputNewEmp(EmpVO empVO);
	
	
	
	// 팀명 조회
	public List<EmpVO> selectAllTeam();
	
	// 직급 조회
	public List<EmpVO> selectAllPosition();
	
	// 전체 사원명 부서명 조회
	public List<EmpVO> empNameList();
}
