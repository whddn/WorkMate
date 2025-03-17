package com.workmate.app.employee.service.impl;

import java.util.List;

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

	
}
