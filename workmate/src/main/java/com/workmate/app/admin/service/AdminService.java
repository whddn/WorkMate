package com.workmate.app.admin.service;

import java.util.List;
import java.util.Map;

import com.workmate.app.employee.service.DepartmentVO;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.employee.service.TeamVO;
import com.workmate.app.reservation.service.CommonItemVO;

public interface AdminService {
	// 전체조회
	public List<CommonItemVO> findItemList();
	//단건조회
	public CommonItemVO findItemById(CommonItemVO commonitemVO);
	// 공용품 등록
	public int inputCommonItem(CommonItemVO commonitemVO);
	// 수정
	public Map<String, Object>modifyItem(CommonItemVO commonitemVO);
	// 삭제
	public Map<String, Object>dropItem(int commonNo);
	
	// 부서 전체 조회
	public List<DepartmentVO> findDeptList(DepartmentVO deptVO);
	// 부서 단건 조회
	public DepartmentVO findDeptById(DepartmentVO deptVO);
	// 부서 등록
//	public int inputNewDept(DepartmentVO deptVO);
	// 부서 수정
	public int modifyDept(DepartmentVO deptVO);
	// 사원 전체 조회 (select2)
	public List<EmpVO> findAllEmployees(EmpVO empVO);
	// 부서장 확인용
	public List<Integer> findCurrentHeads();
	// 부서 & 팀 등록
	public int inputNewDeptAndTeam(DepartmentVO deptVO, TeamVO teamVO);
}
