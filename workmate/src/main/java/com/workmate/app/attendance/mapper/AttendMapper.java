package com.workmate.app.attendance.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.workmate.app.attendance.service.WorkVO;

public interface AttendMapper {
	
	// 월별 근태 조회
	public List<WorkVO> selectWorkList(@Param("userNo") int userNo);
	
	// 전체 근태 조회
	public List<WorkVO> allWorkList(WorkVO workVO);
	
	// 출근 등록
	public int insertStartInfo(WorkVO workVO);
	
	// 퇴근 등록 = 수정
	public int insertAfterInfo(WorkVO workVO);
	
	// 지각 사유 = 수정
	public int insertLateReason(WorkVO workVO);
	
	// 출퇴근 여부
	public WorkVO attendanceStatus(int userNo);
	
	// 내 연차 조회
	public List<WorkVO> occAnnualList(WorkVO workVO);
	
	// 연차 사용내역 전체 조회
	public List<WorkVO> allAnnualList(WorkVO workVO);
	
	// 연차 목록 업데이트
	public int insertAnl(int userNo);
	
	// 연차 갯수 업데이트
	public int updateOccList(int userNo);
	
	// 전체사원 이번달 근태 수
	public List<WorkVO> selectMonthEmpList();
	
	// 전체사원 근태 조회
	public List<WorkVO> selectAllEmpList(WorkVO workVO);
	
	// 전체사원 필터 엑셀 목록
	public List<WorkVO> selectFilterEmpList(@Param("stDate") String stDate,
								            @Param("endDate") String endDate,
								            @Param("name") String userName,
								            @Param("team") String teamName);

}
