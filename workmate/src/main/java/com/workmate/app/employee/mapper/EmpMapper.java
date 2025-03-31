package com.workmate.app.employee.mapper;

import java.util.List;

import com.workmate.app.employee.service.DepartmentVO;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.employee.service.EvaluVO;
import com.workmate.app.employee.service.TeamVO;

public interface EmpMapper {
	// 사원 전헤
	public List<EmpVO> selectAllEmpList(EmpVO empVO);
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
	public List<EvaluVO> selectAdminBeforeEvaluById(EvaluVO evaluVO);
	
	// 지난 평가 단건 조회 (관리자 + userNo 기반)
	public List<EvaluVO> selectAdminBeforeUserEvaluById(EvaluVO evaluVO);
	
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

	// 개인 평가 진행 (내가 평가자로 등록된 평가 단건 조회) - 평가 진행 전
	public List<EvaluVO> selectOneEvaluById(EvaluVO evaluVO);
	
	// 내가 등록한 평가 단건 조회 - 평가 진행 후 
	public List<EvaluVO> selectMyEvaluingById(EvaluVO evaluVO);
	
	// 평가한 거 등록 진행 (평가 결과에 insert)
	public int insertEvaluScore(EvaluVO evaluVO);
	
	// 평가 완료 시 -> 평가 상태를 : 평가 완료로 
	public int updateEvaluStatus(int formNo);
	
	// 평가 상태 조회 
	public String getEvaluStatus(int formNo);
	
	// 내가 피평가자로 등록된 평가 리스트 (나의 평가 결과 리스트)
	public List<EvaluVO> selectMyEvaluResultList(EvaluVO evaluVO);
	
	// 내가 받은 평가 단건 (피평가자 : loginUser)
	public List<EvaluVO> selectMyEvaluScoreResultById(EvaluVO evaluVO);
	
	// 평가자 그룹 ID 조회
	int selectEvaluGroupId(EvaluVO evaluVO);

	// 피평가자 그룹 ID 조회
	int selectEvaluateeGroupId(EvaluVO evaluVO);
	
	// 부서 조회
	public List<DepartmentVO> selectDepartmentList(DepartmentVO deptVO);
	
	// 부서 단건 조회
	public DepartmentVO selectDepartmentById(DepartmentVO deptVO);
	
	// 팀 등록
	public int insertNewTeam(TeamVO teamVO);
	
	// 부서 등록 
	public int insertNewDepartment(DepartmentVO deptVO);
	
	// 부서 수정
	public int updateDepartment(DepartmentVO deptVO);
	
	// 부서장 
	public List<Integer> selectDepartmentHeadList();
	
	// 제출한 평가 (나 : 평가자) 수정
	public int updateSubmitEvalu(EvaluVO evaluVO);
}
