package com.workmate.app.employee.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.employee.mapper.EmpMapper;
import com.workmate.app.employee.service.EmpService;
import com.workmate.app.employee.service.EmpVO;

@Service
public class EmpServiceImpl implements EmpService {
	private EmpMapper empMapper;
	
	
	@Autowired
		public EmpServiceImpl(EmpMapper empMapper) {
		this.empMapper = empMapper;
	}
	
	@Override
	public EmpVO findEmpByEmpNo(EmpVO empVO) {
		return empMapper.selectOneEmp(empVO);
	}
	
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

	@Override
	public List<EmpVO> empNameList() {
		return empMapper.selectAllName();
	}


	@Override
	public EmpVO findOrganPage(EmpVO empVO) {
		return null;
	}
}
