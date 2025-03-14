package com.workmate.app.attendance.service;

import java.util.List;

public interface AttendanceService {
	
	//월별조회
	public List<WorkVO> findMonthWork();
	
	//전체조회
	public List<WorkVO> findAllWork();
	
	//출근등록
	public int startWork(WorkVO workVO);
	
	//퇴근등록
	public int afterWork(WorkVO workVO);
	
	//출근여부
	public WorkVO attendanceStatus(int userNo);
}
