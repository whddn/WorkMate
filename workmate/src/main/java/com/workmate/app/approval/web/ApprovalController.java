package com.workmate.app.approval.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
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
import com.workmate.app.approval.service.ReferenceService;
import com.workmate.app.approval.service.ReferenceVO;
import com.workmate.app.approval.service.ReportAttachService;
import com.workmate.app.approval.service.ReportAttachVO;
import com.workmate.app.approval.service.SignService;
import com.workmate.app.approval.service.SignVO;
import com.workmate.app.attendance.service.AttendanceService;
import com.workmate.app.attendance.service.WorkVO;
import com.workmate.app.common.FileHandler;
import com.workmate.app.common.WhoAmI;
import com.workmate.app.employee.service.EmpService;
import com.workmate.app.employee.service.EmpVO;

import lombok.RequiredArgsConstructor;

/** 전자결재
 * @author 이지응
 * @since 2025-03-10
 * <pre>
 * <pre>
 * 수정일자	수정자	수정내용
 * ----------------------
 * 03-12	이지응	전자결재 기능 제작 시작
 * 03-26	이지응	전자결재 결재자 지정 기능 추가
 * 
 * </pre>
 */
@Controller
@RequiredArgsConstructor
public class ApprovalController {
	private final ApprElmntService apprElmntService;
	private final ApprFormService apprFormService;
	private final ApprLineService apprLineService;
	private final ApprovalService approvalService;
	private final ReferenceService referenceService;
	private final ReportAttachService reportAttachService;
	private final SignService signService;
	private final AttendanceService attendantService;
	private final EmpService empService;
	private final ObjectMapper objectMapper;
	private final FileHandler fileHandler;
	private final WhoAmI whoAmI;
	
	private final String signDir = "sign/";
	private final String apprAttachDir = "apprAttach/";
	
	// ✅ `application.properties`에서 파일 저장 경로 가져오기
	@Value("${file.upload-dir}")
	private String uploadDir;
	
	/**
	 * 결재를 기다리는 문서 리스트를 보여준다(자신과 관련된)
	 * @param model
	 * @param approvalVO
	 * @param standard
	 * @return 결재 진행목록 페이지명
	 */
	@GetMapping("approval/waiting")
	public String getWaiting(Model model, ApprovalVO approvalVO, @RequestParam(defaultValue="fromMe") String standard) {
		approvalVO.setUserNo(whoAmI.whoAmI().getUserNo());
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
		
		EmpVO myself = whoAmI.whoAmI();
		model.addAttribute("creator", empService.findEmpByEmpNo(myself));
		model.addAttribute("apprLineList", apprLineService.findApprLineList(myself));
		
		// 조직도 정보를 전부 불러온다.
		model.addAttribute("names", empService.findDeptEmpNameList());
		
		return "approval/write";
	}
	
	// 결재문서를 DB에 등록한다
	@PostMapping("approval/write")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> postWrite(
		@RequestParam("request") String requestString,
		@RequestPart(value="files", required=false) MultipartFile[] files) throws IOException {
		System.out.print("requestString은 : ");
		System.out.println(requestString);
		
		// JSON 객체에서 공통되는 키의 값만 해당 VO에 전이
		ApprovalVO approvalVO = objectMapper.readValue(requestString, ApprovalVO.class);
		
		// 결재문서를 DB에 등록
		EmpVO myself = whoAmI.whoAmI();
		approvalVO.setUserNo(myself.getUserNo());
		approvalVO.setDeptNo(myself.getDepartmentId());
        
		// 자기 기안 올리는 거
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
        
        // 참조자(의 번호)들을 DB에 등록
        for(Integer empNo : (List<Integer>) approvalVO.getReferrerList()) {
        	// 참조요소에 참조자 번호, 결재문서 번호를 입력
        	ReferenceVO referenceVO = new ReferenceVO();
        	referenceVO.setUserNo(empNo);
        	referenceVO.setApprNo(approvalVO.getApprNo());
        	
        	// 참조요소 등록
        	referenceService.inputReference(referenceVO);
        }
        
        // 파일 업로드 관리
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
            	String fileName = file.getOriginalFilename();
            	Path filePath = Paths.get(uploadDir, apprAttachDir);
            	fileName = fileHandler.fileUpload(file, filePath.toString(), false);
            	// uploadDir = "C:/workmate/"
            	// apprAttachDir = "apprAttach/"
            	// filePath = Path("C:/workmate/apprAttach/")
                
                ReportAttachVO reportAttachVO = new ReportAttachVO();
                reportAttachVO.setFileName(file.getOriginalFilename());
                reportAttachVO.setFilePath(fileName);
                reportAttachVO.setApprNo(approvalVO.getApprNo());
                
                reportAttachService.insertApprovalRA(reportAttachVO);
            }
        }
        
        // 결재유형이 휴가이면 연차내역(?)을 삽입한다
        if(approvalVO.getApprType().equals("AF001")) {
        	WorkVO workVO = objectMapper.readValue(requestString, WorkVO.class);
        	workVO.setApprNo(approvalVO.getApprNo());
        	workVO.setUserNo(myself.getUserNo());

        	attendantService.inputAnnual(workVO);
        }
        
        // 응답을 반송한다
        response.put("success", true);
        return ResponseEntity.ok(response);
	}
	
	// 작성한 결재문서 읽는 페이지를 보여준다.
	@GetMapping("approval/read")
	public String getRead(Model model, @RequestParam String apprNo) {
		EmpVO myself = whoAmI.whoAmI();
		ApprovalVO approvalVO = new ApprovalVO();
		approvalVO.setApprNo(apprNo);
		
		approvalVO = approvalService.findApprovalById(approvalVO);
		System.out.println(apprNo);
		
		model.addAttribute("approval", approvalVO);
		model.addAttribute("apprLine", apprElmntService.findApprElmntList(approvalVO));
		model.addAttribute("refLine", referenceService.findReferenceList(approvalVO));
		model.addAttribute("fileList", reportAttachService.findApprovalRAList(approvalVO));
		model.addAttribute("mySigns", signService.findSignList(myself));
		
		if(approvalVO.getApprType().equals("AF001")) {
			WorkVO workVO = new WorkVO();
        	workVO.setApprNo(approvalVO.getApprNo());

        	workVO = attendantService.findAnnualByApprNo(workVO);
        	model.addAttribute("annual", workVO);
		}
		
		System.out.println(approvalService.findApprovalById(approvalVO));
		
		return "approval/read";
	}
	
	// 작성한 결재문서의 결재여부 등을 수정한다.
	@PutMapping("approval/read")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> putRead(@RequestBody ApprElmntVO apprElmntVO) 
	{
		// 결재결과를 수정
		EmpVO myself = whoAmI.whoAmI();
		apprElmntVO.setApprover(myself.getUserNo());
		
		Map<String, Object> response = new HashMap<>();
		
		int result = apprElmntService.modifyApprElmnt(apprElmntVO);
        if(result <= 0) {
        	response.put("success", false);
        	return ResponseEntity.badRequest().body(response);
        }
        
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
		EmpVO myself = whoAmI.whoAmI();
		
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
    public ResponseEntity<FileSystemResource> getDownload(@RequestParam("fileNo") Integer fileNo) throws IOException {
		ReportAttachVO reportAttachVO = reportAttachService.findApprovalRA(fileNo);
		
		FileSystemResource resource = fileHandler.fileDownload(reportAttachVO.getFilePath(), uploadDir + apprAttachDir);
		
		if(!resource.exists()) {
			return ResponseEntity.notFound().build();
		}
		
		HttpHeaders headers = new HttpHeaders();
		String encodedFileName = URLEncoder.encode(reportAttachVO.getFileName(), StandardCharsets.UTF_8);
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName);
		
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
		EmpVO myself = whoAmI.whoAmI();
		model.addAttribute("signs", signService.findSignList(myself));
		model.addAttribute("apprLines", apprLineService.findApprLineList(myself));
		
		// 조직도 정보를 전부 불러온다.
		model.addAttribute("names", empService.findDeptEmpNameList());
		
		return "approval/manage";
	}
	
	// 서명을 등록한다.
	@PostMapping("approval/sign")
	public ResponseEntity<String> postSign(@RequestParam("file") MultipartFile file) throws IOException {
        // 파일 저장 및 DB에 서명 정보 저장 로직 구현
        if (file != null) {
        	String fileName = file.getOriginalFilename();
        	Path filePath = Paths.get(uploadDir, signDir);
        	fileName = fileHandler.fileUpload(file, filePath.toString(), false);
            
            EmpVO myself = whoAmI.whoAmI();
            SignVO signVO = new SignVO();
            signVO.setSignTitle(file.getOriginalFilename());
            signVO.setSignPath("upload/" + signDir + fileName);
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
		
		EmpVO myself = whoAmI.whoAmI();
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
	
	// 사용자가 즐겨찾기한 결재선의 결재요소들을 가져온다.   나중에 GET으로 바꾸든지 말든지
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
	
	// 결재선을 새로 등록한다.
	@PostMapping("approval/apprLine")
	public ResponseEntity<?> postApprLine(@RequestBody ApprLineVO apprLineVO) {
		EmpVO myself = whoAmI.whoAmI();
		apprLineVO.setInserter(myself.getUserNo());
		int result = apprLineService.inputApprLine(apprLineVO);
		
		if(result > 0) {
			return ResponseEntity.ok("결재선 추가 성공");
		}
		else {
			return ResponseEntity.badRequest().body("결재선 추가 실패");
		}
	}
	
	// 결재선을 변경한다.
	@PutMapping("approval/apprLine")
	public ResponseEntity<?> putApprLine(@RequestBody ApprLineVO apprLineVO) {
		EmpVO myself = whoAmI.whoAmI();
		apprLineVO.setInserter(myself.getUserNo());
		int result = apprLineService.modifyApprLine(apprLineVO);
		
		if(result > 0) {
			return ResponseEntity.ok("결재선 변경 성공");
		}
		else {
			return ResponseEntity.badRequest().body("결재선 변경 실패");
		}
	}
	
	// 결재선을 삭제한다.
	@DeleteMapping("approval/apprLine")
	public ResponseEntity<?> deleteApprLine(@RequestBody ApprLineVO apprLineVO) {
		EmpVO myself = whoAmI.whoAmI();
		apprLineVO.setInserter(myself.getUserNo());
		int result = apprLineService.dropApprLine(apprLineVO);
		
		if(result > 0) {
			return ResponseEntity.ok("결재선 제거 성공");
		}
		else {
			return ResponseEntity.badRequest().body("결재선 제거 실패");
		}
	}
}
