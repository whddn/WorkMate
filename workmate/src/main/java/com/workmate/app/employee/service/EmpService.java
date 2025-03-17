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
	
	// 사원 수정 
	public int updateEmp(EmpVO empVO);
	
	// 평가 양식 조회
	public List<EvaluVO> findOneEvaluPage(EvaluVO evaluVO);
	
	// 지나간 평가 리스트 조회 (전체조회)
	public List<EvaluVO> findBeforeEvaluList(EvaluVO evaluVO);
	
	// 지나간 평가 단건 조회 (관리자) 
	public List<EvaluVO> findBeforeEvaluOne(EvaluVO evaluVO);
	
	// 지나간 평가 페이지 단순 출력문 
	//public EvaluVO findEvaluList(EvaluVO evaluVO);
	
	// 나의 평가 조회 (페이지)
	public EvaluVO findMyEvaluList(EvaluVO evaluVO);
	
	// 나의 평가 조회 (단건 조회) 
	public List<EvaluVO> findOneEvaluResult(EvaluVO evaluVO);
	
	// 평가 등록 (관리자)
	public int inputNewEvalu(EvaluVO evaluVO);
}
