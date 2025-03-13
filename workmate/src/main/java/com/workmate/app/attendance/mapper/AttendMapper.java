package com.workmate.app.attendance.mapper;

import java.util.List;

import com.workmate.app.attendance.service.WorkVO;

public interface AttendMapper {
	
	// 월별 근태 조회
	public List<WorkVO> selectWorkList();
	
	// 출근 등록
	public int insertEmpInfo();
}
