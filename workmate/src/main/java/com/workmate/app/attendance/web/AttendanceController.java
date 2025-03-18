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
		//근태 데이터 조회
		List<WorkVO> list = attendService.findMonthWork(); 
		double addWorkTime = list.stream().mapToDouble(WorkVO::getAddWorkTime).sum();
		double totalWorkTime = list.stream().mapToDouble(WorkVO::getWorkTime).sum();
		double remainWorkTime = 208 - totalWorkTime;
		
		//2-1)Service > View 전달
		model.addAttribute("works", list);
		//출근여부 userNO에 담기
		model.addAttribute("attend",attendService.attendanceStatus(userNo));
		model.addAttribute("totalWorkTime", totalWorkTime);
		model.addAttribute("addWorkTime", addWorkTime); 
		model.addAttribute("remainWorkTime", remainWorkTime);
		
		return "attendance/monthList";
	}
	
	//전체 근태 조회
	@GetMapping("attendance/allList")
	public String allAttendanceList(Model model, WorkVO workVO) {
		
		List<WorkVO> list = attendService.findAllWork(workVO);
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
	
	//내 발생 연차 / 내 발생 연차
	@GetMapping("attendance/annual")
	public String annualList(Model model, WorkVO workVO) {
		workVO.setUserNo(201);	
		
		List<WorkVO> list = attendService.findOccAnnual(workVO);
		List<WorkVO> alist = attendService.findAllAnnual(workVO);
		
		model.addAttribute("occs", list);
		model.addAttribute("anls", alist);
		
		return "attendance/annual";
	}
	
}
