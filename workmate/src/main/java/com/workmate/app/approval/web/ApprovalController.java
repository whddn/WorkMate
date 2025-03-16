package com.workmate.app.approval.web;

import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workmate.app.approval.service.ApprElmntService;
import com.workmate.app.approval.service.ApprFormService;
import com.workmate.app.approval.service.ApprFormVO;
import com.workmate.app.approval.service.ApprLineService;
import com.workmate.app.approval.service.ApprovalService;
import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.approval.service.SignService;
import com.workmate.app.employee.service.EmpService;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.security.service.LoginUserVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ApprovalController {
	private final ApprovalService approvalService;
	private final ApprFormService apprFormService;
	private final ApprLineService apprLineService;
	private final ApprElmntService apprElmntService;
	private final SignService signService;
	private final EmpService empService;
	
	private EmpVO whoAmI() {
		LoginUserVO loginUserVO = (LoginUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer currUserNo =  Integer.parseInt(loginUserVO.getUserVO().getUserNo());
		
		EmpVO empVO = new EmpVO();
		empVO.setUserNo(currUserNo);
		return empVO;
	}
	
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
		
		EmpVO myself = whoAmI();
		model.addAttribute("creator", empService.findEmpByEmpNo(myself));
		model.addAttribute("apprLineList", apprLineService.selectApprLineList(myself));
		
		return "approval/write";
	}
	
	@PostMapping("approval/write")
	public String writePost(@RequestParam Map<String, Object> map, @RequestPart(value="files", required=false) MultipartFile[] files) {
		System.out.println("map is :");
		System.out.println(map);
		
		ApprovalVO approvalVO = new ApprovalVO();
		BeanUtils.copyProperties(map, approvalVO);
		
		EmpVO myself = whoAmI();
		approvalVO.setUserNo(myself.getUserNo());
		approvalVO.setDeptNo(myself.getDepartmentId());
		
		System.out.println("approvalVO is :");
		System.out.println(approvalVO);
        //int result = approvalService.insertApproval(approvalVO);

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
            	/*
                if (!file.isEmpty()) {
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
                    Files.write(path, bytes);
                    System.out.println("File uploaded: " + file.getOriginalFilename());
                }
                */
            }
        }

        if (/*result > 0*/false) {
            return "approval/waiting";
        }
        else {
            return "approval/write";
        }
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
		EmpVO myself = whoAmI();
		model.addAttribute("signs", signService.selectSignList(myself));
		model.addAttribute("apprLines", apprLineService.selectApprLineList(myself));
		
		return "approval/manage";
	}

}
