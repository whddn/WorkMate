package com.workmate.app.employee.service;

import java.util.List;
import java.util.Map;

public interface EmpService {
	
	// 조직도 페이지 조회
	public EmpVO findOrganPage(EmpVO empVO);
	
	// 단건 조회 (조직도)
	public EmpVO findEmpByEmpNo(EmpVO empVO);
	
	// 등록 
	public int inputNewEmp(EmpVO empVO);
	
	// 팀명 조회
	public List<EmpVO> findTeamList();
	
	// 직급 조회
	public List<EmpVO> findPositionList();
	
	// 전체 사원명 부서명 조회
	public List<EmpVO> findDeptEmpNameList();
	
	// 사원 수정 
	public int updateEmp(EmpVO empVO);
	
	// 평가 양식 조회
	public List<EvaluVO> findOneEvaluPage(EvaluVO evaluVO);
	
	// 지나간 평가 리스트 조회 (전체조회)
	public List<EvaluVO> findBeforeEvaluList(EvaluVO evaluVO);
	
	// 지나간 평가 단건 조회 (관리자) 
	public List<EvaluVO> findBeforeEvaluById(EvaluVO evaluVO);
	
	// 평가 결과 - 평가자 정보 조회 
	//public List<DepartmentVO> findEvaluInfo(EvaluVO evaluVO);
	public List<EvaluVO> findEvaluInfo(EvaluVO evaluVO);
	
	// 평가 결과 - 피평가자 정보 조회
	//public List<DepartmentVO> findEvaluateeInfo(EvaluVO evaluVO);
	public List<EvaluVO> findEvaluateeInfo(EvaluVO evaluVO);
	// 지나간 평가 페이지 단순 출력문 
	//public EvaluVO findEvaluList(EvaluVO evaluVO);
	
	// 나의 평가한 리스트 / 평가할 리스트 조회 (페이지)
	public List<EvaluVO> findMyEvaluList(EvaluVO evaluVO);

	// 나의 평가 진행 중 조회 (단건 조회) 
	public List<EvaluVO> findMyEvaluById(EvaluVO evaluVO);
	
	// 평가 진행 페이지
	public List<EvaluVO> findMyEvaluProcess(EvaluVO evaluVO);
	
	// 평가 등록 (관리자 페이지)
	public int inputNewEvalu(EvaluVO evaluVO);
	
	// 평가 등록시 평가 항목 / 평가 내용 조회 
	public Map<String, List<EvaluVO>> findEvaluContentList(EvaluVO evaluVO);
	
	// 평가 폼 등록 기능 AJAX 
	public int inputNewEvaluForm(EvaluVO evaluVO);
	
	// 평가한 결과 값 서버에 등록하는 AJAX (result 테이블)
	public int inputEvaluResultScore(EvaluVO evaluVO);
}
