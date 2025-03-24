package com.workmate.app.mainscreen.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.workmate.app.approval.service.ApprovalService;
import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.common.WhoAmI;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.mainscreen.service.MenuService;
import com.workmate.app.mainscreen.service.MenuVO;
import com.workmate.app.mainscreen.service.ScheduleService;
import com.workmate.app.mainscreen.service.ScheduleVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainscreenController {
	private final MenuService menuService;
	private final ApprovalService approvalService;
	private final ScheduleService scheduleService;
	private final WhoAmI whoAmI;
	
	@GetMapping("/")
	public String base(Model model) {
		EmpVO myself = whoAmI.whoAmI();
		
		ApprovalVO approvalVO = new ApprovalVO();
		approvalVO.setUserNo(myself.getUserNo());
		approvalVO.setApprStatus("a1");
		approvalVO.setStandard("toMe");
		model.addAttribute("waitingList", approvalService.findApprovalList(approvalVO));
		
		ScheduleVO scheduleVO = new ScheduleVO();
		scheduleVO.setUserNo(myself.getUserNo());
		scheduleVO.setDeptNo(myself.getDepartmentId());
		scheduleVO.setUnit("day");
		
		model.addAttribute("scheduleList", scheduleService.findScheduleList(scheduleVO));
		
		return "mainscreen/main";
	}
	
	@GetMapping("/search")
	public String getSearch(Model model) {
		return "mainscreen/search";
	}
	
	@PostMapping("/search/start")
	@ResponseBody
	public List<MenuVO> postSearchStart(@RequestBody MenuVO menuVO) {
		return menuService.findMenuList(menuVO);
	}
	
	@GetMapping("/calendar")
	public String getCalendar(Model model) {		
		return "mainscreen/calendar";
	}
	
	@GetMapping("/calendar/schedule")
	@ResponseBody
	public List<ScheduleVO> getCalendatSchedule() {
		EmpVO myself = whoAmI.whoAmI();
		
		ScheduleVO scheduleVO = new ScheduleVO();
		scheduleVO.setUserNo(myself.getUserNo());
		scheduleVO.setDeptNo(myself.getDepartmentId());
		
		return scheduleService.findScheduleList(scheduleVO);
	}
	
	@PostMapping("/calendar/schedule")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> postCalendarSchedule(@RequestBody ScheduleVO scheduleVO) {
		EmpVO myself = whoAmI.whoAmI();
		
		scheduleVO.setUserNo(myself.getUserNo());
		scheduleVO.setDeptNo(myself.getDepartmentId());
		int result = scheduleService.inputSchedule(scheduleVO);
		
		Map<String, Object> response = new HashMap<>();
		
		if(result > 0) {
			response.put("success", true);
			response.put("result", scheduleVO);
	        return ResponseEntity.ok(response);
		}
		else {
			response.put("success", false);
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@PutMapping("/calendar/schedule")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> putCalendarSchedule(@RequestBody ScheduleVO scheduleVO) {
		EmpVO myself = whoAmI.whoAmI();
		
		scheduleVO.setUserNo(myself.getUserNo());
		scheduleVO.setDeptNo(myself.getDepartmentId());
		int result = scheduleService.modifySchedule(scheduleVO);
		
		Map<String, Object> response = new HashMap<>();
		
		if(result > 0) {
			response.put("success", true);
			response.put("result", scheduleVO);
	        return ResponseEntity.ok(response);
		}
		else {
			response.put("success", false);
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@DeleteMapping("/calendar/schedule")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> deleteCalendarSchedule(@RequestBody ScheduleVO scheduleVO) {
		EmpVO myself = whoAmI.whoAmI();
		
		scheduleVO.setUserNo(myself.getUserNo());
		scheduleVO.setDeptNo(myself.getDepartmentId());
		int result = scheduleService.dropSchedule(scheduleVO);
		
		Map<String, Object> response = new HashMap<>();
		
		if(result > 0) {
			response.put("success", true);
			return ResponseEntity.ok(response);
		}
		else {
			response.put("success", false);
			return ResponseEntity.badRequest().body(response);
		}
	}
}
