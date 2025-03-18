package com.workmate.app.approval.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workmate.app.approval.service.ApprElmntService;
import com.workmate.app.approval.service.ApprElmntVO;
import com.workmate.app.approval.service.ApprFormService;
import com.workmate.app.approval.service.ApprFormVO;
import com.workmate.app.approval.service.ApprLineService;
import com.workmate.app.approval.service.ApprLineVO;
import com.workmate.app.approval.service.ApprovalService;
import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.approval.service.ReportAttachService;
import com.workmate.app.approval.service.ReportAttachVO;
import com.workmate.app.approval.service.SignService;
import com.workmate.app.employee.service.EmpService;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.security.service.LoginUserVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ApprovalController {
	private final static String UPLOAD_FOLDER = "D:/workmateUploads/";
	private final ApprElmntService apprElmntService;
	private final ApprFormService apprFormService;
	private final ApprLineService apprLineService;
	private final ApprovalService approvalService;
	private final ReportAttachService reportAttachService;
	private final SignService signService;
	private final EmpService empService;
	private final ObjectMapper objectMapper;
	
	// 현재 로그인한 사람의 개인정보를 empVO로 불러온다.
	public EmpVO whoAmI() {
		LoginUserVO loginUserVO = (LoginUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = new EmpVO();
		
		empVO.setUserNo(loginUserVO.getUserVO().getUserNo());
    	empVO = empService.findEmpByEmpNo(empVO);
		
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
	@ResponseBody
	public ResponseEntity<Map<String, Object>> writePost(
		@RequestPart("request") String requestString,
		@RequestPart(value="files", required=false) MultipartFile[] files
	) throws IOException {
		// JSON 객체에서 공통되는 키의 값만 해당 VO에 전이
		ApprovalVO approvalVO = objectMapper.readValue(requestString, ApprovalVO.class);
		
		// 결재문서를 DB에 등록
		EmpVO myself = whoAmI();
		approvalVO.setUserNo(myself.getUserNo());
		approvalVO.setDeptNo(myself.getDepartmentId());

		Map<String, Object> response = new HashMap<>();
        
        int result = approvalService.insertApproval(approvalVO);
        if(result <= 0) {
        	response.put("success", false);
        	return ResponseEntity.badRequest().body(response);
        }
        
        // 결재선(정확히 말하면 결재요소들)을 DB에 등록
        for(Integer empNo : (List<Integer>) approvalVO.getApproverList()) {
        	// 결재자 번호를 통해 결재자 정보 불러오기
        	EmpVO empVO = new EmpVO();
        	empVO.setUserNo(empNo);
        	empVO = empService.findEmpByEmpNo(empVO);
        	
        	// 결재요소에 결재자 번호, 결재자의 부서, 결재문서 번호를 입력
        	ApprElmntVO apprElmntVO = new ApprElmntVO();
        	apprElmntVO.setApprover(empNo);
        	apprElmntVO.setDeptNo(empVO.getDepartmentId());
        	apprElmntVO.setApprNo(approvalVO.getApprNo());
        	
        	// 결재요소 등록
        	apprElmntService.insertApprElmnt(apprElmntVO);
        }
        
        // 파일 업로드 관리
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
            	String fileName = file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_FOLDER + fileName);
                Files.write(filePath, file.getBytes());
                System.out.println("파일 저장 완료: " + fileName);
                
                ReportAttachVO reportAttachVO = new ReportAttachVO();
                reportAttachVO.setFileName(fileName);
                reportAttachVO.setFilePath(filePath.toString());
                reportAttachVO.setApprNo(approvalVO.getApprNo());
                System.out.println(reportAttachVO);
                
                reportAttachService.insertApprovalRA(reportAttachVO);
            }
        }
        
        // 응답을 반송한다
        response.put("success", true);
        return ResponseEntity.ok(response);
	}
	
	@PostMapping("approval/summonApprElmnts")
	public ResponseEntity<List<EmpVO>> summonApprElmnts(@RequestBody Map<String, Object> request) {
		ApprLineVO apprLineVO = new ApprLineVO();
		apprLineVO.setApprlineNo(Integer.parseInt(request.get("apprLineNo").toString()));
		
        apprLineVO = apprLineService.selectApprLine(apprLineVO);
        List<EmpVO> list = new ArrayList<>();
        for(Integer empNo : apprLineVO.getComponentsByList()) {
        	EmpVO empVO = new EmpVO();
        	empVO.setUserNo(empNo);
        	list.add(empService.findEmpByEmpNo(empVO));
        }
        return ResponseEntity.ok(list);
	}
	
	@GetMapping("approval/read")
	public String readGet(Model model, @RequestParam String apprNo) {
		ApprovalVO approvalVO = new ApprovalVO();
		approvalVO.setApprNo(apprNo);
		model.addAttribute("approval", approvalService.selectApproval(approvalVO));
		model.addAttribute("apprLine", apprElmntService.selectApprElmntList(approvalVO));
		model.addAttribute("fileList", reportAttachService.selectApprovalRAList(approvalVO));
		
		return "approval/read";
	}
	
	@PutMapping("approval/read")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> readPut(@RequestBody ApprElmntVO apprElmntVO) 
	throws IOException {
		// 결재결과를 수정
		apprElmntVO.setApprover(whoAmI().getUserNo());
		System.out.println(apprElmntVO);
		
		Map<String, Object> response = new HashMap<>();
		
		int result = apprElmntService.updateApprElmnt(apprElmntVO);
        if(result <= 0) {
        	response.put("success", false);
        	return ResponseEntity.badRequest().body(response);
        }
		
        System.out.println("응애");
        
        // 결재문서의 결재결과를 결정
		ApprovalVO approvalVO = new ApprovalVO();
		approvalVO.setApprNo(apprElmntVO.getApprNo());
		approvalService.updateApproval(approvalVO);
		
		response.put("success", true);
        return ResponseEntity.ok(response);
	}
	
	// 파일 다운로드 컨트롤러
	@GetMapping("approval/download")
    public ResponseEntity<FileSystemResource> downloadFile(
    	@RequestParam("filePath") String filePath, 
    	@RequestParam("fileName") String fileName
    ) throws IOException {
        Path path = Paths.get(filePath);
        FileSystemResource resource = new FileSystemResource(path.toFile());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
	
	@GetMapping("approval/manage")
	public String manage(Model model) {
		EmpVO myself = whoAmI();
		model.addAttribute("signs", signService.selectSignList(myself));
		model.addAttribute("apprLines", apprLineService.selectApprLineList(myself));
		
		return "approval/manage";
	}

	@GetMapping("approval/pdf")
	public String moveToPdf(Model model, @RequestParam String apprNo) {
		ApprovalVO approvalVO = new ApprovalVO();
		approvalVO.setApprNo(apprNo);
		model.addAttribute("approval", approvalService.selectApproval(approvalVO));
		model.addAttribute("apprLine", apprElmntService.selectApprElmntList(approvalVO));
		
		return "approval/pdf";
	}
	
	/*
	@PostMapping("approval/sign")
	public ResponseEntity<?> addSign(
		@RequestParam("signFile") MultipartFile signFile, 
		@RequestParam("signTitle") String signTitle
	) {
	    try {
	        // 파일 저장 및 DB에 서명 정보 저장 로직 구현
	        // ...
	        return ResponseEntity.ok("서명 추가 성공");
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서명 추가 실패");
	    }
	}
	
	@PostMapping("approval/apprLine")
	public ResponseEntity<?> addApprLine(@RequestBody ApprLineVO apprLineVO) {
	    // DB에 결재선 정보 저장 로직 구현
	    // ...
	    return ResponseEntity.ok("결재선 추가 성공");
	}
	*/
}
