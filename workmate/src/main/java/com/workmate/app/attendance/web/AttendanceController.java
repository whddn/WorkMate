package com.workmate.app.attendance.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.workmate.app.attendance.service.AttendanceService;
import com.workmate.app.attendance.service.WorkVO;
import com.workmate.app.security.service.LoginUserVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AttendanceController {
		
	private AttendanceService attendService; 
	
	@Autowired
	public AttendanceController(AttendanceService attendService) {
		this.attendService = attendService;
	}
	

	//월별 근태 전체조회 : GET	
	@GetMapping("attendance/monthList")
	public String monthAttendanceList(Model model, @AuthenticationPrincipal LoginUserVO loginUser) {		
	    
		//로그인 여부 확인
		if (loginUser == null || loginUser.getUserVO() == null) {
	        return "redirect:/login";  // 로그인 페이지로 리다이렉트
	    }
	    
		int userNo = loginUser.getUserVO().getUserNo(); 		
		String userName = loginUser.getUserVO().getUserName(); 		
		//2)Service
		//근태 데이터 조회
		List<WorkVO> list = attendService.findMonthWork(userNo); 
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
		model.addAttribute("userNo", userNo);
		model.addAttribute("userName", userName);
		
		return "attendance/monthList";
	}
	
	//전체 근태 조회
	@GetMapping("attendance/allList")
	public String allAttendanceList(Model model, WorkVO workVO, @AuthenticationPrincipal LoginUserVO loginUser) {
		
		//로그인 여부
		if (loginUser == null || loginUser.getUserVO() == null) {
		 return "redirect:/login";
		}
		
		int userNo = loginUser.getUserVO().getUserNo();
		workVO.setUserNo(userNo);
		
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
	
	//내 발생 연차, 연차사용내역전체조회
	@GetMapping("attendance/annual")
	public String annualList(Model model, WorkVO workVO, @AuthenticationPrincipal LoginUserVO loginUser) {
		//로그인 여부
		if (loginUser == null || loginUser.getUserVO() == null) {
			return "redirect:/login";
			}
				
		int userNo = loginUser.getUserVO().getUserNo();
		workVO.setUserNo(userNo);	
		
		List<WorkVO> list = attendService.findOccAnnual(workVO);
		List<WorkVO> alist = attendService.findAllAnnual(workVO);		
		
		model.addAttribute("occs", list);
		model.addAttribute("anls", alist);
		
		return "attendance/annual";
	}
	
	
	
	//전체사원 근태조회()
	@GetMapping("attendance/attendanceManage")
	public String attendEmpList(Model model, WorkVO workVO) {
		
		List<WorkVO> count = attendService.findMothEmpWork(workVO);
		List<WorkVO> list = attendService.findAllEmpWork(workVO);
		
		model.addAttribute("works", list);
		model.addAttribute("counts", count);
		
		return "attendance/attendanceManage";
	}
	
	
	
}
