package com.workmate.app.approval.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.workmate.app.approval.service.ApprFormService;
import com.workmate.app.approval.service.ApprFormVO;
import com.workmate.app.approval.service.ApprLineService;
import com.workmate.app.approval.service.ApprLineVO;
import com.workmate.app.approval.service.ApprovalService;
import com.workmate.app.approval.service.ApprovalVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ApprovalController {
	private final ApprFormService apprFormService;
	private final ApprovalService approvalService;
	private final ApprLineService apprLineService;
	
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
		apprFormVO = apprFormService.selectForm(apprFormVO);
		model.addAttribute("apprForm", apprFormVO);
		
		//System.out.println(apprFormService.selectForm(apprFormVO));
		
		현재사용자VO 현재사용자 = new 현재사용자VO();
		현재사용자.setUserNo()
		model.addAttribute("creator", 현재사용자);
		
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
		
		//System.out.println(approvalService.selectApproval(approvalVO).getFormPath());
		
		ApprLineVO apprLineVO = new ApprLineVO();
		apprLineVO.setApprNo(apprNo);
		model.addAttribute("apprLine", apprLineService.selectApprLine(apprLineVO));
		
		//System.out.println(apprLineService.selectApprLine(apprLineVO));
		
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
