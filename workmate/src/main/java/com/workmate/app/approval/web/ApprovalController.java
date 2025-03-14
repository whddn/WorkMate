package com.workmate.app.approval.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workmate.app.approval.service.ApprFormVO;
import com.workmate.app.approval.service.ApprovalListService;
import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.approval.service.FormListService;

import lombok.RequiredArgsConstructor;

@Controller
public class ApprovalController {
	private final FormListService formListService;
	private final ApprovalListService approvalListService;
	
	@Autowired
	public ApprovalController(FormListService formListService, ApprovalListService approvalListService) {
		this.formListService = formListService;
		this.approvalListService = approvalListService;
	}
	
	@GetMapping("approval/waiting")
	public String waiting(Model model, ApprovalVO approvalVO) {
		approvalVO.setApprStatus("a1");
		model.addAttribute("waitingList", approvalListService.selectApprovalList(approvalVO));
		return "approval/waiting";
	}
	
	@GetMapping("approval/allowance")
	public String allowance(Model model, ApprovalVO approvalVO) {
		approvalVO.setApprStatus("a2");
		model.addAttribute("allowanceList", approvalListService.selectApprovalList(approvalVO));
		return "approval/allowance";
	}
	
	@GetMapping("approval/rejection")
	public String rejection(Model model, ApprovalVO approvalVO) {
		approvalVO.setApprStatus("a3");
		model.addAttribute("rejectionList", approvalListService.selectApprovalList(approvalVO));
		return "approval/rejection";
	}
	
	@GetMapping("approval/formList")
	public String formList(Model model) {
		model.addAttribute("formList", formListService.loadFormList());
		return "approval/formList";
	}
	
	@GetMapping("approval/formWrite")
	public String formWriteGet(Model model) {
		return "approval/formWrite";
	}
	
	@PostMapping("approval/formWrite")
	public String formWritePost(Model model) {
		return "approval/formList";
	}
	
	@GetMapping("approval/formRead")
	public String formReadGet(Model model) {
		return "approval/formRead";
	}
	
	@PutMapping("approval/formRead")
	public String formReadPut(Model model) {
		return "approval/formList";
	}
	
	@DeleteMapping("approval/formRead")
	public String formReadDelete(Model model) {
		return "approval/formList";
	}
	
	@GetMapping("approval/manage")
	public String manage(Model model) {
		return "approval/manage";
	}
}
