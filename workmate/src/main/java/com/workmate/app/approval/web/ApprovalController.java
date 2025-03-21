package com.workmate.app.approval.web;

import java.io.IOException;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.workmate.app.approval.service.SignVO;
import com.workmate.app.common.FileHandler;
import com.workmate.app.common.WhoAmI;
import com.workmate.app.employee.service.EmpService;
import com.workmate.app.employee.service.EmpVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ApprovalController {
	private final ApprElmntService apprElmntService;
	private final ApprFormService apprFormService;
	private final ApprLineService apprLineService;
	private final ApprovalService approvalService;
	private final ReportAttachService reportAttachService;
	private final SignService signService;
	private final EmpService empService;
	private final ObjectMapper objectMapper;
	private final FileHandler fileHandler = new FileHandler();
	private final WhoAmI whoAmI = new WhoAmI();
	
	private EmpVO whoAmI() {
		EmpVO empVO = new EmpVO();
		empVO.setUserNo(whoAmI.whoAmI().getUserNo());
		return empService.findEmpByEmpNo(empVO);
	}
	
	// 결재를 기다리는 문서 리스트를 보여준다
	@GetMapping("approval/waiting")
	public String getWaiting(Model model, ApprovalVO approvalVO, @RequestParam(defaultValue = "fromMe") String standard) {
		approvalVO.setUserNo(whoAmI().getUserNo());
		approvalVO.setApprStatus("a1");
		approvalVO.setStandard(standard);
		model.addAttribute("waitingList", approvalService.findApprovalList(approvalVO));
		return "approval/waiting";
	}
	
	// 결재 승인받은 문서 리스트를 보여준다
	@GetMapping("approval/allowance")
	public String getAllowance(Model model, ApprovalVO approvalVO) {
		approvalVO.setApprStatus("a2");
		model.addAttribute("allowanceList", approvalService.findApprovalList(approvalVO));
		return "approval/allowance";
	}
	
	// 결재 반려된 문서 리스트를 보여준다
	@GetMapping("approval/rejection")
	public String getRejection(Model model, ApprovalVO approvalVO) {
		approvalVO.setApprStatus("a3");
		model.addAttribute("rejectionList", approvalService.findApprovalList(approvalVO));
		return "approval/rejection";
	}
	
	// 결재 신청하려 할때 결재 양식 목록을 보여준다
	@GetMapping("approval/formList")
	public String getFormList(Model model) {
		model.addAttribute("formList", apprFormService.findFormList());
		return "approval/formList";
	}
	
	// 결재문서 쓰는 페이지를 보여준다
	@GetMapping("approval/write")
	public String getWrite(Model model, @RequestParam String apprType) {
		ApprFormVO apprFormVO = new ApprFormVO();
		apprFormVO.setApprType(apprType);
		model.addAttribute("apprForm", apprFormService.findFormById(apprFormVO));
		
		EmpVO myself = whoAmI();
		model.addAttribute("creator", empService.findEmpByEmpNo(myself));
		model.addAttribute("apprLineList", apprLineService.findApprLineList(myself));
		
		return "approval/write";
	}
	
	// 결재문서를 DB에 등록한다
	@PostMapping("approval/write")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> postWrite(
		@RequestPart("request") String requestString,
		@RequestPart(value="files", required=false) MultipartFile[] files
	) throws IOException {
		// JSON 객체에서 공통되는 키의 값만 해당 VO에 전이
		ApprovalVO approvalVO = objectMapper.readValue(requestString, ApprovalVO.class);
		System.out.println(requestString);
		System.out.println(approvalVO);
		
		// 결재문서를 DB에 등록
		EmpVO myself = whoAmI();
		myself.setUserNo(whoAmI.whoAmI().getUserNo());
		myself = empService.findEmpByEmpNo(myself);
		
		approvalVO.setUserNo(myself.getUserNo());
		approvalVO.setDeptNo(myself.getDepartmentId());

		Map<String, Object> response = new HashMap<>();
        
        int result = approvalService.inputApproval(approvalVO);
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
        	apprElmntService.inputApprElmnt(apprElmntVO);
        }
        
        // 파일 업로드 관리
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
            	String fileName = file.getOriginalFilename();
            	Path filePath = Paths.get("C://workmate/apprAttach/" + fileName);
            	fileHandler.fileUpload(file, fileName);
                
                ReportAttachVO reportAttachVO = new ReportAttachVO();
                reportAttachVO.setFileName(fileName);
                reportAttachVO.setFilePath(filePath.toString());
                reportAttachVO.setApprNo(approvalVO.getApprNo());
                
                reportAttachService.insertApprovalRA(reportAttachVO);
            }
        }
        
        // 응답을 반송한다
        response.put("success", true);
        return ResponseEntity.ok(response);
	}
	
	// 사용자가 즐겨찾기한 결재선 목록들을 가져온다.
	@PostMapping("approval/summonApprElmnts")
	public ResponseEntity<List<EmpVO>> postSummonApprElmnts(@RequestBody Map<String, Object> request) {
		ApprLineVO apprLineVO = new ApprLineVO();
		apprLineVO.setApprlineNo(Integer.parseInt(request.get("apprLineNo").toString()));
		
        apprLineVO = apprLineService.findApprLineById(apprLineVO);
        List<EmpVO> list = new ArrayList<>();
        for(Integer empNo : apprLineVO.getComponentsByList()) {
        	EmpVO empVO = new EmpVO();
        	empVO.setUserNo(empNo);
        	list.add(empService.findEmpByEmpNo(empVO));
        }
        return ResponseEntity.ok(list);
	}
	
	// 작성한 결재문서 읽는 페이지를 보여준다.
	@GetMapping("approval/read")
	public String getRead(Model model, @RequestParam String apprNo) {
		EmpVO myself = whoAmI();
		ApprovalVO approvalVO = new ApprovalVO();
		approvalVO.setApprNo(apprNo);
		model.addAttribute("approval", approvalService.findApprovalById(approvalVO));
		model.addAttribute("apprLine", apprElmntService.findApprElmntList(approvalVO));
		model.addAttribute("fileList", reportAttachService.findApprovalRAList(approvalVO));
		model.addAttribute("mySigns", signService.findSignList(myself));
		
		return "approval/read";
	}
	
	// 작성한 결재문서의 결재여부 등을 수정한다.
	@PutMapping("approval/read")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> putRead(@RequestBody ApprElmntVO apprElmntVO) 
	throws IOException {
		// 결재결과를 수정
		EmpVO myself = whoAmI();
		apprElmntVO.setApprover(myself.getUserNo());
		
		Map<String, Object> response = new HashMap<>();
		
		int result = apprElmntService.modifyApprElmnt(apprElmntVO);
        if(result <= 0) {
        	response.put("success", false);
        	return ResponseEntity.badRequest().body(response);
        }
		
        System.out.println("응애");
        
        // 결재문서의 결재결과를 결정
		ApprovalVO approvalVO = new ApprovalVO();
		approvalVO.setApprNo(apprElmntVO.getApprNo());
		approvalService.modifyApproval(approvalVO);
		
		response.put("success", true);
        return ResponseEntity.ok(response);
	}
	
	// 아직 결재 완료되지 않은 문서를 삭제한다.
	@DeleteMapping("approval/read/{apprNo}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> deleteRead(@PathVariable String apprNo) {
		EmpVO myself = whoAmI();
		
		ApprovalVO approvalVO = new ApprovalVO();
		approvalVO.setApprNo(apprNo);
		approvalVO.setUserNo(myself.getUserNo());
		
		Map<String, Object> response = new HashMap<>();
		
		int result = approvalService.dropApproval(approvalVO);
		if(result > 0) {
			response.put("success", true);
	        return ResponseEntity.ok(response);
		}
		else {
			response.put("success", false);
        	return ResponseEntity.badRequest().body(response);
		}
	}
	
	// 파일을 다운로드한다.
	@GetMapping("approval/download")
    public ResponseEntity<FileSystemResource> getDownload(
    	@RequestParam("filePath") String filePath, 
    	@RequestParam("fileName") String fileName
    ) throws IOException {
		FileSystemResource resource = fileHandler.fileDownload(fileName, filePath);
		
		if(!resource.exists()) {
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

	// 결재문서의 PDF버전을 보여주고 자동 다운로드한다.
	@GetMapping("approval/pdf")
	public String getPdf(Model model, @RequestParam String apprNo) {
		ApprovalVO approvalVO = new ApprovalVO();
		approvalVO.setApprNo(apprNo);
		model.addAttribute("approval", approvalService.findApprovalById(approvalVO));
		model.addAttribute("apprLine", apprElmntService.findApprElmntList(approvalVO));
		
		return "approval/pdf";
	}
	
	// 서명, 즐겨찾기 결재선을 관리하는 페이지를 보여준다.
	@GetMapping("approval/manage")
	public String getManage(Model model) {
		EmpVO myself = whoAmI();
		model.addAttribute("signs", signService.findSignList(myself));
		model.addAttribute("apprLines", apprLineService.findApprLineList(myself));
		
		return "approval/manage";
	}
	
	// 서명을 등록한다.
	@PostMapping("approval/sign")
	public ResponseEntity<String> postSign(@RequestParam("file") MultipartFile file) throws IOException {
        // 파일 저장 및 DB에 서명 정보 저장 로직 구현
        if (file != null) {
        	String filePath = fileHandler.fileUpload(file, "C://workmate/sign/");
            
            EmpVO myself = whoAmI();
            SignVO signVO = new SignVO();
            signVO.setSignTitle(file.getOriginalFilename());
            signVO.setSignPath(filePath);
            signVO.setUserNo(myself.getUserNo());
            
            signService.inputSign(signVO);
        }
        return ResponseEntity.ok("서명 추가 성공");
	}
	
	// 서명을 삭제한다.
	@DeleteMapping("approval/sign/{signNo}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> deleteSign(@PathVariable Integer signNo) {
		Map<String, Object> response = new HashMap<>();
		
		EmpVO myself = whoAmI();
		SignVO signVO = new SignVO();
		signVO.setSignNo(signNo);
		signVO.setUserNo(myself.getUserNo());
		
		int result = signService.dropSign(signVO);
		if(result > 0) {
			response.put("success", true);
	        return ResponseEntity.ok(response);
		}
		else {
			response.put("success", false);
        	return ResponseEntity.badRequest().body(response);
		}
	}
	
	/*
	@PostMapping("approval/apprLine")
	public ResponseEntity<?> postApprLine(@RequestBody ApprLineVO apprLineVO) {
	    // DB에 결재선 정보 저장 로직 구현
	    // ...
	    return ResponseEntity.ok("결재선 추가 성공");
	}
	*/
}
