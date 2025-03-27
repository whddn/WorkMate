package com.workmate.app.attendance.service;

import java.util.List;

public interface AttendanceService {
	
	//월별조회
	public List<WorkVO> findMonthWork(int userNo);
	
	//전체조회
	public List<WorkVO> findAllWork(WorkVO workVO);
	
	//출근등록
	public int startWork(WorkVO workVO);
	
	//퇴근등록
	public int afterWork(WorkVO workVO);
	
	//지각사유
	public int inputLateReason(WorkVO workVO);
	
	//출근여부
	public WorkVO attendanceStatus(int userNo);
	
	//연차 조회
	public List<WorkVO> findOccAnnual(WorkVO workVO);
	
	//연차 사용 내역 전체조회
	public List<WorkVO> findAllAnnual(WorkVO workVO);
	
	// 전체사원 이번달 근태 수	
	public List<WorkVO> findMothEmpWork(WorkVO workVO);
	
	//전체사원 근태조회
	public List<WorkVO> findAllEmpWork(WorkVO workVO);
	
	//필터링된 전체사원 엑셀
	public List<WorkVO> getFilterAttend(String stDate, String endDate, String userName, String teamName);

}
