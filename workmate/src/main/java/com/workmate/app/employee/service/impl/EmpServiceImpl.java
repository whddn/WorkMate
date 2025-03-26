package com.workmate.app.employee.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.employee.mapper.EmpMapper;
import com.workmate.app.employee.service.EmpService;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.employee.service.EvaluVO;

@Service
public class EmpServiceImpl implements EmpService {
	private EmpMapper empMapper;

	@Autowired
	public EmpServiceImpl(EmpMapper empMapper) {
		this.empMapper = empMapper;
	}

	// 단건 조회
	@Override
	public EmpVO findEmpByEmpNo(EmpVO empVO) {
		return empMapper.selectEmpById(empVO);
	}

	// 등록
	@Override
	public int inputNewEmp(EmpVO empVO) {
		return empMapper.insertEmployee(empVO);
	}

	// 팀명 조회
	@Override
	public List<EmpVO> findTeamList() {
		return empMapper.selectTeamList();
	}

	// 직급 조회
	@Override
	public List<EmpVO> findPositionList() {
		return empMapper.selectEmpPositionList();
	}

	// 조직도 사원 전체 조회
	@Override
	public List<EmpVO> findDeptEmpNameList() {
		return empMapper.selectEmpNameList();
	}

	@Override
	public EmpVO findOrganPage(EmpVO empVO) {
		return null;
	}

	// 사원 수정
	@Override
	public int updateEmp(EmpVO empVO) {
		return empMapper.updateEmployee(empVO);
	}

	// 평가 양식 조회
	@Override
	public List<EvaluVO> findOneEvaluPage(EvaluVO evaluVO) {
		return empMapper.selectEvaluList(evaluVO);
	}
	// 단순 평가 리스트 조회 (페이지 불러냄)
//	@Override
//	public EvaluVO findEvaluList(EvaluVO evaluVO) {
//		return null;
//	}


	// 내가 진행한 평가 단건 조회 
	@Override
	public List<EvaluVO> findMyEvaluById(EvaluVO evaluVO) {
	    List<EvaluVO> rawList = empMapper.selectOneEvaluById(evaluVO);
	    
	    Set<String> seen = new HashSet<>();	// set : 중복 제거 
	    List<EvaluVO> result = new ArrayList<>(); // 새 리스트 생성 

	    for (EvaluVO vo : rawList) {	// selectOne의 쿼리값을 모두 반환할 동안 
	        int userNo = vo.getUserNo();	// 유저 번호 
	        String evaluCompet = vo.getEvaluCompet();	// 항목 
	        String evaluContent = vo.getEvaluContent();	// 내용

	        if (evaluCompet == null) continue;	// compet이 null이면 계속 추가 

	        String key = userNo + "|" + evaluCompet.trim() + "|" + evaluContent.trim(); // key에 userNo, evaluCompet, evaluContent 기준 
	        						
	        if (seen.add(key)) {	// seen 리스트에 key 추가 
	            result.add(vo);		// result 에 vo 추가 
	        }


	    }

	    return result;
	}

	
	// 지나간 평가 전체 리스트 조회 (관리자 - 평가 - 전체 조회)
	@Override
	public List<EvaluVO> findBeforeEvaluList(EvaluVO evaluVO) {
		List<EvaluVO> result = empMapper.selectBeforeEvaluList(evaluVO);
		Set<Integer> teamSet = new HashSet<>();
		List<EvaluVO> uniqueTeam = new ArrayList<>();
		for (EvaluVO vo : result) {
			if (teamSet.add(vo.getEvaluFormNo())) { // 폼 번호 기준으로 중복 제거
				uniqueTeam.add(vo);
			}
		}
		return uniqueTeam;
	}
	
	
	@Override
	public Map<String, Object> findAdminEvaluBeforeById(EvaluVO evaluVO) {
	    List<EvaluVO> rawList = empMapper.selectAdminBeforeEvaluById(evaluVO);

	    // 1. 사람 중복 제거
	    Map<Integer, EvaluVO> peopleMap = new LinkedHashMap<>();

	    // 2. 항목 중복 제거
	    Set<String> itemKeySet = new LinkedHashSet<>();
	    List<EvaluVO> itemList = new ArrayList<>();

	    // 3. 점수 저장
	    Map<Integer, List<EvaluVO>> scoreMap = new LinkedHashMap<>();

	    for (EvaluVO vo : rawList) {
	        int userNo = vo.getUserNo();
	        String itemKey = vo.getEvaluCompet() + "|" + vo.getEvaluContent();

	        // 사람 정보 1번만
	        peopleMap.putIfAbsent(userNo, vo);

	        // 항목 중복 없이
	        if (itemKeySet.add(itemKey)) {
	            EvaluVO item = new EvaluVO();
	            item.setEvaluCompet(vo.getEvaluCompet());
	            item.setEvaluContent(vo.getEvaluContent());
	            item.setOrderNo(vo.getOrderNo());
	            itemList.add(item);
	        }

	        // 점수 보존
	        scoreMap.computeIfAbsent(userNo, k -> new ArrayList<>()).add(vo);
	    }

	    Map<String, Object> result = new LinkedHashMap<>();
	    result.put("people", new ArrayList<>(peopleMap.values()));
	    result.put("items", itemList);
	    result.put("scores", scoreMap); // Map<Integer, List<EvaluVO>>

	    return result;
	}



	// 관리자 단건 조회 항목 / 컨텐츠 중복 제거 
//	@Override
//	public List<EvaluVO> getEvaluItemsUniqueByUser(EvaluVO param) {
//	    List<EvaluVO> rawList = empMapper.selectAdminBeforeEvaluById(param); // 기존 쿼리 재활용
//	    Set<String> seen = new HashSet<>();
//	    List<EvaluVO> result = new ArrayList<>();
//
//	    for (EvaluVO vo : rawList) {
//	        if (vo.getUserNo() != param.getUserNo()) continue;
//
//	        String key = vo.getEvaluCompet() + "|" + vo.getEvaluContent();
//	        if (seen.add(key)) {
//	            result.add(vo);
//	        }
//	    }
//	    return result;
//	}
	// 관리자 단건 조회
//	@Override
//	public List<EvaluVO> findAdminEvaluBeforeById(EvaluVO EvaluVO) {
//	    return empMapper.selectAdminBeforeEvaluById(EvaluVO);
//	}
//	
	
	
	// 평가 등록 페이지
	@Override
	public int inputNewEvalu(EvaluVO evaluVO) {
		return empMapper.insertOneEvalu(evaluVO);
	}

	// 평가지 등록 AJAX

	// 평가 등록시 평가 항목/평가 내용 조회
	@Override
	public Map<String, List<EvaluVO>> findEvaluContentList(EvaluVO evaluVO) {
		List<EvaluVO> evaluList = empMapper.selectContentList(evaluVO); // 평가 항목 리스트
		// 동일한 평가 항목이 나오지 않게 하는 코드 (Map)
		Map<String, List<EvaluVO>> evaMap = new HashMap<>();
		for (int i = 0; i < evaluList.size(); i++) { // evaluList.get(i) : 키 값, getEvaluCompet : value 값
			if (evaMap.get(evaluList.get(i).getEvaluCompet()) != null) { // i 번째의 항목을 받아오고, 그 값이 널이 아니면 아래 코드를 실행함
				evaMap.get(evaluList.get(i).getEvaluCompet()).add(evaluList.get(i)); //

			} else {
				List<EvaluVO> oneEva = new ArrayList<EvaluVO>();
				oneEva.add(evaluList.get(i));
				evaMap.put(evaluList.get(i).getEvaluCompet(), oneEva);
			}

		}
		System.out.println(evaMap);
		return evaMap;
	}

	// 평가자 정보 조회
	@Override
	public List<EvaluVO> findEvaluInfo(EvaluVO evaluVO) {
		return empMapper.selectEvaluInfoById(evaluVO);
	}

	// 피평가자 정보 조회
	@Override
	public List<EvaluVO> findEvaluateeInfo(EvaluVO evaluVO) {
		return empMapper.selectEvaluateeInfoById(evaluVO);
	}

	// 다면 평가 폼 등록
	@Override
	public int inputNewEvaluForm(EvaluVO evaluVO) {

		int formInsert = empMapper.insertEvaluForm(evaluVO); // 폼 등록 쿼리문
		int result = 0;
		int evaluGroup = 0;
		int evaluateeGroup = 0;
		if (formInsert > 0) {
			evaluGroup = empMapper.insertEvaluGroup(evaluVO); // 평가자 그룹 등록
			evaluateeGroup = empMapper.insertEvaluateeGroup(evaluVO); // 피평가자 그룹 등록
		}
		if (formInsert > 0) { // 폼 등록 성공하면 실행
			List<EvaluVO> formatList = evaluVO.getEvaluItem(); // 여러 개의 항목 등록
			for (EvaluVO format : formatList) {
				format.setEvaluFormNo(evaluVO.getEvaluFormNo()); // 등록된 formNo 불러옴
				result += empMapper.insertEvaluFormat(format); // 항목 등록 쿼리문
			}

		}
		return result;
	}



	// 나의 진행된 평가 리스트 조회
	@Override
	public List<EvaluVO> findMyEvaluList(EvaluVO evaluVO) {
		return empMapper.selectMyEvaluList(evaluVO);
	}

	// 내가 평가자로 지정된 평가 확인 (내가 제출한 평가) 
	@Override
	public List<EvaluVO> findMyEvaluProcess(EvaluVO evaluVO) {
	    List<EvaluVO> rawList = empMapper.selectMyEvaluingById(evaluVO);

	    Set<String> seen = new HashSet<>();	// set : 중복 제거 
	    List<EvaluVO> result = new ArrayList<>(); // 새 리스트 생성 
	    System.out.println("🔍 rawList size: " + rawList.size());
	    for (EvaluVO vo : rawList) {	// selectOne의 쿼리값을 모두 반환할 동안 
	        int userNo = vo.getUserNo();	// 유저 번호 
	        String evaluCompet = vo.getEvaluCompet();	// 항목 
	        String evaluContent = vo.getEvaluContent();	// 내용
	        if (evaluCompet == null) continue;	// compet이 null이면 계속 추가 

	        String key = userNo + "|" + evaluCompet.trim() + "|" + evaluContent.trim(); // key에 userNo, evaluCompet, evaluContent 기준 
	        						
	        if (seen.add(key)) {	// seen 리스트에 key 추가 
	            result.add(vo);		// result 에 vo 추가 
	        }
	    }

	    return result;
	}
	
	// 평가 상태 변경 
	@Override
	public int modifyEvaluStatus(int formNo) {
		return empMapper.updateEvaluStatus(formNo);
	}

	// 다면평가 진행(저장)
	@Override
	public int inputEvaluResultScore(List<EvaluVO> evaluList) {
	    int result = 0;
	    for (EvaluVO evalu : evaluList) {
	        // 평가자 그룹 ID 조회 
	    
	       
	        // 피평가자 그룹 ID 조회 

	        // 평가 결과 insert
	        int inserted = empMapper.insertEvaluScore(evalu);
	        result += inserted;
	    }

	    return result;
	    }

	
	
	@Override
	public String findEvaluStatus(int formNo) {
	    return empMapper.getEvaluStatus(formNo);
	}


	// 내가 피평가자로 속해 있는 평가의 리스트 
	@Override
	public List<EvaluVO> findMyEvaluResultList(EvaluVO evaluVO) {
		return empMapper.selectMyEvaluResultList(evaluVO);
	}
	
	// 내가 받은 평가 점수 확인 (단건)
	@Override
	public List<EvaluVO> findMyEvaluScoreResultById(EvaluVO evaluVO) {
	    List<EvaluVO> oneList = empMapper.selectMyEvaluScoreResultById(evaluVO);

	    Set<Integer> seenUserNos = new HashSet<>();
	    
	    for (EvaluVO vo : oneList) {
	        int userNo = vo.getUserNo();
	        
	        // 이미 출력된 사람일 경우, 사람 관련 정보 비우기
	        if (!seenUserNos.add(userNo)) {
	            vo.setUserName(null);
	        }
	    }

	    return oneList;
	}
	


}
