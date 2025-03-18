package com.workmate.app.employee.mapper;

import java.util.List;

import com.workmate.app.employee.service.DepartmentVO;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.employee.service.EvaluVO;

public interface EmpMapper {
	// 단건 조회 
	public EmpVO selectOneEmp(EmpVO empVO);
	
	// 전체 조회 (사원명과 부서명)
	public List<EmpVO> selectAllName(); 
	
	// 등록 
	public int insertOneEmp(EmpVO empVO);
	
	// 팀명 조회
	public List<EmpVO> selectAllEmpTeam();
	
	// 직급 조회
	public List<EmpVO> selectAllPosition();
	
	// 사원 수정
	public int updateOneEmp(EmpVO empVO);
		// 평가 양식 조회
	public List<EvaluVO> selectOneEvalu(EvaluVO evaluVO);
	
	// 지난 평가 결과 전체 조회 (관리자)
	public List<EvaluVO> selectAllEvalu(EvaluVO evaluVO);
	
	// 지난 평가 단건 조회 (관리자)
	public List<EvaluVO> selectOneEvaluInList(EvaluVO evaluVO);
	
	
	//public List<DepartmentVO> selectEvaluInfo(EvaluVO evaluVO);			// 평가 결과 - 평가자 정보 조회 
	//public List<DepartmentVO> selectEvaluateeInfo(EvaluVO evaluVO); 	// 평가 결과 - 피평가자 정보 조회
	public List<EvaluVO> selectEvaluInfo(EvaluVO evaluVO);
	public List<EvaluVO> selectEvaluateeInfo(EvaluVO evaluVO);
	// 지난 평가 결과 페이지 단순 불러냄
	//public List<EvaluVO> selectEvaluList(EvaluVO evaluVO);
	
	// 나의 평가 관리 (일반 사용자) -> 쿼리문 완성되면 사용 할 것
	//public List<EvaluVO> selectMyEvaluOne(EvaluVO evaluVO);
	
	// 나의 평가 관리 페이지 (단순 출력문)
	public String selectMyEvaluOne(EvaluVO evaluVO);
	
	// 나의 평가 단순 조회 
	public List<EvaluVO> selectMyEvaluResultOne(EvaluVO evaluVO);
	
	// 평가 등록 페이지 (관리자)
	public int insertOneEvalu(EvaluVO evaluVO);
	
	// 평가 항목, 평가 내용 조회 (평가 등록시 필요)
	public List<EvaluVO> selectAllContent(EvaluVO evaluVO);
}
