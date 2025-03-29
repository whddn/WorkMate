package com.workmate.app.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.admin.service.AdminService;
import com.workmate.app.employee.mapper.EmpMapper;
import com.workmate.app.employee.service.DepartmentVO;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.reservation.mapper.CommonItemMapper;
import com.workmate.app.reservation.service.CommonItemVO;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	//필드주입 방법
	private CommonItemMapper commonitemMapper;
	@Autowired
	private EmpMapper empMapper;
	
	/*
	 * 생성자 방식 public AdminServiceImpl(CommonItemMapper commonitemMapper) {
	 * this.commonitemMapper = commonitemMapper; }
	 */

	// 공용품 전체조회
	@Override
	public List<CommonItemVO> findItemList() {
		return commonitemMapper.selectItemList();
	}

	// 공용품 단건조회
	@Override
	public CommonItemVO findItemById(CommonItemVO commonitemVO) {
		return commonitemMapper.selectItemById(commonitemVO);
	}

	// 공용품생성
	@Override
	public int inputCommonItem(CommonItemVO commonitemVO) {
		int result = commonitemMapper.insertCommonItemInfo(commonitemVO);
		return result == 1 ? commonitemVO.getCommonNo() : -1;
	}

	// 공용품수정
	@Override
	public Map<String, Object> modifyItem(CommonItemVO commonitemVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;
		int result = commonitemMapper.updateItemInfo(commonitemVO);
		if(result == 1) {
			isSuccessed = true;
		}
		map.put("result", isSuccessed);
		map.put("target", commonitemVO);
		
		return map;
	}

	// 공용품삭제
	@Override
	public Map<String, Object> dropItem(int commonNo) {
		Map<String, Object> map = new HashMap<>();
		int result = commonitemMapper.deleteItemInfo(commonNo);
		if(result == 1) {
			map.put("commonNo", commonNo);
		}
		return map;
	}

	// 부서 조회
	@Override
	public List<DepartmentVO> findDeptList(DepartmentVO deptVO) {
		return empMapper.selectDepartmentList(deptVO);
	}
	
	// 부서 단건 조회
	@Override
	public DepartmentVO findDeptById(DepartmentVO deptVO) {
		return empMapper.selectDepartmentById(deptVO);
	}


	// 신규 부서 등록
	@Override
	public int inputNewDept(DepartmentVO deptVO) {
		return empMapper.insertNewDepartment(deptVO);
	}
	
	// 부서 수정
	@Override
	public int modifyDept(DepartmentVO deptVO) {
		return 0;
	}

	// 전체 조회 
	@Override
	public List<EmpVO> findAllEmployees(EmpVO empVO) {
		return empMapper.selectAllEmpList(empVO);
	}

	@Override
	public List<Integer> findCurrentHeads() {
		return null;
	}


}
