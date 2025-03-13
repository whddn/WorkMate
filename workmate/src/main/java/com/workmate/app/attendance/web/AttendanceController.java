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
	
	//근태 전체조회 : GET	
	@GetMapping("attendance/allList")
	public String allAttendanceList(Model model) {
		//2)Service
		List<WorkVO> list = attendService.findAllWork();
		
		//2-1)Service > View 전달
		model.addAttribute("works", list);
		return "attendance/allList";
	}
	
	@GetMapping("attendance/annual")
	public String annualList(Model model) {
		return "attendance/annual";
	}
	
}
