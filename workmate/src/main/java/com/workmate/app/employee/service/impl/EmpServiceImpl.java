package com.workmate.app.employee.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		return empMapper.selectOneEmp(empVO);
	}
	
	// 등록 
	@Override
	public int inputNewEmp(EmpVO empVO) {
		return empMapper.insertOneEmp(empVO);
	}
	
	// 팀명 조회
	@Override
	public List<EmpVO> selectAllTeam() {
		return empMapper.selectAllEmpTeam();
	}
	
	// 직급 조회
	@Override
	public List<EmpVO> selectAllPosition(){
		return empMapper.selectAllPosition();
	}
	// 조직도 사원 전체 조회
	@Override
	public List<EmpVO> empNameList() {
		return empMapper.selectAllName();
	}


	@Override
	public EmpVO findOrganPage(EmpVO empVO) {
		return null;
	}
	
	// 사원 수정
	@Override
	public int updateEmp(EmpVO empVO) {
		return empMapper.updateOneEmp(empVO);
	}
	
	// 평가 양식 조회
	@Override
	public List<EvaluVO> findOneEvaluPage(EvaluVO evaluVO) {
		return empMapper.selectOneEvalu(evaluVO);
	}
	// 단순 평가 리스트 조회 (페이지 불러냄) 
//	@Override
//	public EvaluVO findEvaluList(EvaluVO evaluVO) {
//		return null;
//	}

	
	// 지나간 평가 전체 리스트 조회 (전체 조회)
	@Override
	public List<EvaluVO> findBeforeEvaluList(EvaluVO evaluVO) {
		return empMapper.selectAllEvalu(evaluVO);
	}
	// 나의 평가 리스트 조회 
	@Override
	public EvaluVO findMyEvaluList(EvaluVO evaluVO) {
		return null;
	}

	// 나의 평가 단순 1건 조회
	@Override
	public List<EvaluVO> findOneEvaluResult(EvaluVO evaluVO) {
		return empMapper.selectMyEvaluResultOne(evaluVO);
	}

	// 지나간 평가 단건 조회 (관리자 - 단건 조회)
	@Override
	public List<EvaluVO> findBeforeEvaluOne(EvaluVO evaluVO) {
		return empMapper.selectOneEvaluInList(evaluVO);
	}

	// 평가 등록
	@Override
	public int inputNewEvalu(EvaluVO evaluVO) {
		return empMapper.insertOneEvalu(evaluVO);
	}
	
	// 평가 등록시 평가 항목/평가 내용 조회
	@Override
	public Map<String, List<EvaluVO>> allEvaluContent(EvaluVO evaluVO) {
		List<EvaluVO> evaluList = empMapper.selectAllContent(evaluVO); // 평가 항목 리스트 
		// 동일한 평가 항목이 나오지 않게 하는 코드 (Map)
		Map<String, List<EvaluVO>> evaMap = new HashMap<>();  
		for (int i = 0; i < evaluList.size() ; i++ ) {  // evaluList.get(i) : 키 값, getEvaluCompet : value 값 
			if (evaMap.get(evaluList.get(i).getEvaluCompet()) != null ) { // i 번째의 항목을 받아오고, 그 값이 널이 아니면 아래 코드를 실행함 
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

	
}
