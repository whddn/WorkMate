package com.workmate.app.attendance.mapper;

import java.util.List;

import com.workmate.app.attendance.service.WorkVO;

public interface AttendMapper {
	
	// 월별 근태 조회
	public List<WorkVO> selectWorkList();
	
	// 전체 근태 조회
	public List<WorkVO> allWorkList(WorkVO workVO);
	
	// 출근 등록
	public int insertStartInfo(WorkVO workVO);
	
	// 퇴근 등록 = 수정
	public int insertAfterInfo(WorkVO workVO);
	
	// 출퇴근 여부
	public WorkVO attendanceStatus(int userNo);
	
	// 내 연차 조회
	public List<WorkVO> occAnnualList(WorkVO workVO);
	
	// 연차 사용내역 전체 조회
	public List<WorkVO> allAnnualList(WorkVO workVO);

}
