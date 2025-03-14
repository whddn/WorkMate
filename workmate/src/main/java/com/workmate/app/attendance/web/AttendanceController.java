package com.workmate.app.attendance.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.workmate.app.attendance.service.AttendanceService;
import com.workmate.app.attendance.service.WorkVO;

@Controller
public class AttendanceController {
	
	private AttendanceService attendService; 
	
	@Autowired
	public AttendanceController(AttendanceService attendService) {
		this.attendService = attendService;
	}
	
	//월별 근태 전체조회 : GET	
	@GetMapping("attendance/monthList")
	public String monthAttendanceList(Model model) {
		
		int userNo = 201; 
		//2)Service
		List<WorkVO> list = attendService.findMonthWork();
		//2-1)Service > View 전달
		model.addAttribute("works", list);
		model.addAttribute("attend",attendService.attendanceStatus(userNo));
		
		return "attendance/monthList";
	}
	
	//전체 근태 조회
	@GetMapping("attendance/allList")
	public String allAttendanceList(Model model) {
		
		List<WorkVO> list = attendService.findAllWork();
		model.addAttribute("works", list);

		return "attendance/allList";
	}
	
	
	//출근 등록
	@GetMapping("attendance/startWork")
	public String startWork(WorkVO workVO) {		
		workVO.setUserNo(201);		
		int start = attendService.startWork(workVO);
		
		return "redirect:/attendance/monthList";		
	}
	
	//퇴근 등록
	@GetMapping("attendance/afterWork")
	public String afterWork(WorkVO workVO) {
		workVO.setUserNo(201);
		int after = attendService.afterWork(workVO);
		
		return "redirect:/attendance/monthList";
	}
	
	//내 연차 내역
	@GetMapping("attendance/annual")
	public String annualList(Model model) {
		return "attendance/annual";
	}
	
}
