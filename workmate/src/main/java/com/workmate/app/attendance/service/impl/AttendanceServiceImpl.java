package com.workmate.app.attendance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.attendance.mapper.AttendMapper;
import com.workmate.app.attendance.service.AttendanceService;
import com.workmate.app.attendance.service.WorkVO;

@Service
public class AttendanceServiceImpl implements AttendanceService {
	
	private AttendMapper attendMapper;
	
	AttendanceServiceImpl(AttendMapper attendMapper){
		this.attendMapper = attendMapper;
	}
	//월별
	@Override
	public List<WorkVO> findMonthWork(int userNo) {
		return attendMapper.selectWorkList(userNo);
	}
	//전체
	@Override
	public List<WorkVO> findAllWork(WorkVO workVO) {	
		return attendMapper.allWorkList(workVO);
	}
	//출근여부
	@Override
	public WorkVO attendanceStatus(int userNo) {
		return attendMapper.attendanceStatus(userNo);
	}
	
	//출근
	@Override
	public int startWork(WorkVO workVO) {
		int result = attendMapper.insertStartInfo(workVO);
		
		return result;
	};
	
	//퇴근
	@Override
	public int afterWork(WorkVO workVO) {
		int result = attendMapper.insertAfterInfo(workVO);
		
		return result;
	}
	
	//내 연차 조회
	@Override
	public List<WorkVO> findOccAnnual(WorkVO workVO) {		
		return attendMapper.occAnnualList(workVO);
	}
	
	@Override
	//연차 단건 조회
	public WorkVO findAnnualByApprNo(WorkVO workVO) {
		return attendMapper.selectAnnualByApprNo(workVO);
	}
	
	//연차 사용내역 전체 조회
	@Override
	public List<WorkVO> findAllAnnual(WorkVO workVO) {
		return attendMapper.allAnnualList(workVO);
	}
	
	//연차 목록 업데이트
	@Override
	public Integer inputAnnual(WorkVO workVO) {
		return attendMapper.insertAnl(workVO);
	}
	
	// 전체사원 근태 조회
	@Override
	public List<WorkVO> findAllEmpWork(WorkVO workVO) {
		return attendMapper.selectAllEmpList(workVO);
	}
	// 전체사원 이번달 근태 수
	@Override
	public List<WorkVO> findMothEmpWork(WorkVO workVO) {
		return attendMapper.selectMonthEmpList();
	}
	
	// 전체사원 필터링 > 엑셀다운로드
	@Override
	public List<WorkVO> getFilterAttend(String stDate, String endDate, String userName, String teamName) {
		
		return attendMapper.selectFilterEmpList(stDate,endDate, userName,teamName);
	}
	
	// 지각 사유 업로드
	@Override
	public int inputLateReason(WorkVO workVO) {
		
		return attendMapper.insertLateReason(workVO);
	}	

	
}
