package com.workmate.app.employee.service;

import java.util.List;
import java.util.Map;

public interface EmpService {
	

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
	
	// 지나간 평가 단건 조회 -> 사원 목록 다 나옴 (관리자) 
	public Map<String, Object> findAdminEvaluBeforeById(EvaluVO evaluVO);
	
	// 지나간 평가 단건 조회 (사원 기준)
	public List<EvaluVO> findAdminEvaluEmpOneById(EvaluVO evaluVO);
	
	// 평가 결과 - 평가자 정보 조회 
	//public List<DepartmentVO> findEvaluInfo(EvaluVO evaluVO);
	public List<EvaluVO> findEvaluInfo(EvaluVO evaluVO);
	
	// 평가 결과 - 피평가자 정보 조회
	//public List<DepartmentVO> findEvaluateeInfo(EvaluVO evaluVO);
	public List<EvaluVO> findEvaluateeInfo(EvaluVO evaluVO);

	// 나의 평가한 리스트 / 평가할 리스트 조회 (페이지)
	public List<EvaluVO> findMyEvaluList(EvaluVO evaluVO);

	// 평가 제출 전 : 평가자 기준으로 피평가자 + 항목 조회 (점수 없음)
	public List<EvaluVO> findMyEvaluById(EvaluVO evaluVO);
	
	// 평가 제출 후 : 평가자 기준으로 평가 결과(점수 포함) 조회
	public List<EvaluVO> findMyEvaluProcess(EvaluVO evaluVO);
	//public List<EvaluVO> findMyEvaluingById(EvaluVO evaluVO);
	
	// 평가 등록 (관리자 페이지)
	public int inputNewEvalu(EvaluVO evaluVO);
	
	// 평가 등록시 평가 항목 / 평가 내용 조회 (관리자)
	public Map<String, List<EvaluVO>> findEvaluContentList(EvaluVO evaluVO);
	
	// 평가 폼 등록 기능 AJAX (관리자)
	public int inputNewEvaluForm(EvaluVO evaluVO);
	
	// 평가한 결과 값 서버에 등록하는 AJAX (result 테이블- 관리자)
	public int inputEvaluResultScore(List<EvaluVO> evaluList,  String mode);
	
	// 평가 보내고 나면 상태를 평가 완료로 바꿈 ?
	public int modifyEvaluStatus(EvaluVO vo);
	
	// 평가 상태 조회 쿼리
	public String findEvaluStatus(int formNo);

	// 내가 피평가자로 속해 있는 평가 리스트 (받은 평가 리스트)
	public List<EvaluVO> findMyEvaluResultList(EvaluVO evaluVO);
	
	// 내가 받은 평가 점수 확인 (단건 조회)
	public List<EvaluVO> findMyEvaluScoreResultById(EvaluVO evaluVO);
	
	// 사원 중복 제거 
	public String findEvaluStatusById(EvaluVO vo);
	
	// 자동 평가 종료로 
	public int autoUpdateStatusByDate();
	
	// 점수 임시저장
	public int modifyEvaluScore(EvaluVO vo);
	
	// 임시 저장된 점수 불러오기
	public Map<String, Integer> findTempEvaluScore(EvaluVO vo);

	// 평가자 상태 수정 (임시저장 / 제출완료)
	public int modifyEvaluGroupStatus(EvaluVO vo); 
	
	// 임시저장 삭제
	public void dropTempEvaluScore(EvaluVO vo);
	
	// 관리자 평가 폼 단건 조회 (평과 결과 X)
	public List<EvaluVO> findInEvaluForm(EvaluVO vo);
}
