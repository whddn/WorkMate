package com.workmate.app.employee.mapper;

import java.util.List;

import com.workmate.app.employee.service.DepartmentVO;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.employee.service.EvaluVO;

public interface EmpMapper {
	// 단건 조회 
	public EmpVO selectEmpById(EmpVO empVO);

	// 직급 조회
	public List<EmpVO> selectEmpPositionList();
	
	// 전체 조회 (사원명과 부서명)
	public List<EmpVO> selectEmpNameList(); 
	
	// 사원 등록 
	public int insertEmployee(EmpVO empVO);
	
	// 팀명 조회
	public List<EmpVO> selectTeamList();
	
	// 사원 수정
	public int updateEmployee(EmpVO empVO);
	
	// 평가 양식 조회 (평가해야 할 페이지)
	public List<EvaluVO> selectEvaluList(EvaluVO evaluVO);
	
	// 지난 평가 결과 전체 조회 (관리자)
	public List<EvaluVO> selectBeforeEvaluList(EvaluVO evaluVO);
	
	// 지난 평가 단건 조회 (관리자)
	public List<EvaluVO> selectAdminBeforeEvaluList(EvaluVO evaluVO);
	
	// 평가자 / 피평가자 정보 조회
	public List<EvaluVO> selectEvaluInfoById(EvaluVO evaluVO);
	public List<EvaluVO> selectEvaluateeInfoById(EvaluVO evaluVO);

	
	// 평가 등록 페이지 (관리자)
	public int insertOneEvalu(EvaluVO evaluVO);
	
	// 평가 항목, 평가 내용 조회 (관리자 : 평가 등록시 필요)
	public List<EvaluVO> selectContentList(EvaluVO evaluVO);
	
	// 평가 폼 등록 기능 
	public int insertEvaluForm(EvaluVO evlauVO);
	
	// 평가 포맷 등록 기능
	public int insertEvaluFormat(EvaluVO evaluVO);
	
	// 평가자 그룹 등록 기능 
	public int insertEvaluGroup(EvaluVO evaluVO);
	
	// 피평가자 그룹 등록 기능
	public int insertEvaluateeGroup(EvaluVO evaluVO);
	
	// 나의 평가했던 리스트 페이지 전체 조회
	public List<EvaluVO> selectMyEvaluList(EvaluVO evaluVO);
	
	// 나의 평가 단순 조회 단건 조회
	public List<EvaluVO> selectMyEvaluResultById(EvaluVO evaluVO);

	// 개인 평가 진행 (평가할 페이지)
	public List<EvaluVO> selectOneEvaluById(EvaluVO evaluVO);

	// 평가한 거 등록 기능 (쿼리문 없음)
	public int insertEvaluScore(EvaluVO evaluVO);
	
	// 평가 완료 시 -> 평가 상태를 : 평가 완료로 
	public int updateEvaluStatus(int formNo);
	
}
