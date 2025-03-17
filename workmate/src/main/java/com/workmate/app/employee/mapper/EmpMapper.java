package com.workmate.app.employee.mapper;

import java.util.List;

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
	
	// 지난 평가 결과 조회 (관리자)
	public List<EvaluVO> selectAllEvalu(EvaluVO evaluVO);
	
	// 지난 평가 단순 조회 (관리자)
	public List<EvaluVO> selectOneEvaluInList(EvaluVO evaluVO);
 	
	// 지난 평가 결과 페이지 단순 불러냄
	//public List<EvaluVO> selectEvaluList(EvaluVO evaluVO);
	
	// 나의 평가 관리 (일반 사용자) -> 쿼리문 완성되면 사용 할 것
	//public List<EvaluVO> selectMyEvaluOne(EvaluVO evaluVO);
	
	// 나의 평가 관리 페이지 (단순 출력문)
	public String selectMyEvaluOne(EvaluVO evaluVO);
	
	// 나의 평가 단순 조회 
	public List<EvaluVO> selectMyEvaluResultOne(EvaluVO evaluVO);
}
