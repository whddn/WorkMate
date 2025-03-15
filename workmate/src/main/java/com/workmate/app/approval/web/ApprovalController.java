package com.workmate.app.approval.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.workmate.app.approval.service.ApprElmntService;
import com.workmate.app.approval.service.ApprFormService;
import com.workmate.app.approval.service.ApprFormVO;
import com.workmate.app.approval.service.ApprLineService;
import com.workmate.app.approval.service.ApprLineVO;
import com.workmate.app.approval.service.ApprovalService;
import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.employee.service.EmpService;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.security.service.LoginUserVO;
import com.workmate.app.security.service.UserVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ApprovalController {
	private final ApprovalService approvalService;
	private final ApprFormService apprFormService;
	private final ApprLineService apprLineService;
	private final ApprElmntService apprElmntService;
	private final EmpService empService;
	
	@GetMapping("approval/waiting")
	public String waiting(Model model, ApprovalVO approvalVO) {
		approvalVO.setApprStatus("a1");
		model.addAttribute("waitingList", approvalService.selectApprovalList(approvalVO));
		return "approval/waiting";
	}
	
	@GetMapping("approval/allowance")
	public String allowance(Model model, ApprovalVO approvalVO) {
		approvalVO.setApprStatus("a2");
		model.addAttribute("allowanceList", approvalService.selectApprovalList(approvalVO));
		return "approval/allowance";
	}
	
	@GetMapping("approval/rejection")
	public String rejection(Model model, ApprovalVO approvalVO) {
		approvalVO.setApprStatus("a3");
		model.addAttribute("rejectionList", approvalService.selectApprovalList(approvalVO));
		return "approval/rejection";
	}
	
	@GetMapping("approval/formList")
	public String formList(Model model) {
		model.addAttribute("formList", apprFormService.selectFormList());
		return "approval/formList";
	}
	
	@GetMapping("approval/write")
	public String writeGet(Model model, @RequestParam String apprType) {
		ApprFormVO apprFormVO = new ApprFormVO();
		apprFormVO.setApprType(apprType);
		model.addAttribute("apprForm", apprFormService.selectForm(apprFormVO));
		
		LoginUserVO loginUserVO = (LoginUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = new EmpVO();
		empVO.setUserNo(Integer.parseInt(loginUserVO.getUserVO().getUserNo()));
		model.addAttribute("creator", empService.findEmpByEmpNo(empVO));
		
		model.addAttribute("apprLineList", apprLineService.selectApprLineList(empVO));
		
		return "approval/write";
	}
	
	@PostMapping("approval/write")
	public Integer writePost(@RequestBody ApprovalVO approvalVO) {
		System.out.println(approvalVO);
		//int result = approvalService.insertApproval(approvalVO);
		return 0;
	}
	
	@GetMapping("approval/read")
	public String readGet(Model model, @RequestParam String apprNo) {
		ApprovalVO approvalVO = new ApprovalVO();
		approvalVO.setApprNo(apprNo);
		model.addAttribute("approval", approvalService.selectApproval(approvalVO));
		model.addAttribute("apprLine", apprElmntService.selectApprElmntList(approvalVO));
		
		return "approval/read";
	}
	
	@PutMapping("approval/read")
	public String readPut(Model model) {
		return "approval/read";
	}
	
	@GetMapping("approval/manage")
	public String manage(Model model) {
		return "approval/manage";
	}
}
