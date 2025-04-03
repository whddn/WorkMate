package com.workmate.app.admin.web;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.workmate.app.admin.service.AdminService;
import com.workmate.app.approval.service.ApprFormService;
import com.workmate.app.approval.service.ApprFormVO;
import com.workmate.app.common.FileHandler;
import com.workmate.app.employee.service.DepartmentVO;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.employee.service.TeamVO;
import com.workmate.app.reservation.service.CommonItemVO;

import lombok.RequiredArgsConstructor;

/** 관리자  페이지 : 공용품, 전자결재 템플릿, 부서관리
 * @author 이종우
 * @since 2025-03-10
 * <pre>
 * 수정일자	수정자	수정내용
 * -------------------------
 * 03-13	이종우	공용품추가기능
 * 03-14	이종우	수정기능
 * 03-17	이종우	삭제,목록기능
 * 03-19	이종우	사진/날짜 추가
 * 03-25	이지응	결재문서 양식 조작기능 추가
 * 03-27	이종우	관리자만 사용 권한 추가
 * 03-29	박혜원	부서 관리 기능 구현
 * </pre>
 */

@Controller
@RequiredArgsConstructor
public class AdminController {
	private final AdminService adminService;
	private final ApprFormService apprFormService;
	private final FileHandler fileHandler;
	// ✅ `application.properties`에서 파일 저장 경로 가져오기
	@Value("${file.upload-dir}")
	private String uploadDir;
	private final String subDir = "CommonItemImage/";
	private final String apprFormDir = "forms/approval/";
	
	/**
	 * 공용품 관리
	 * @param model
	 * @return
	 */
	@GetMapping("admin/commonItemList")
	public String commonItemList(Model model) {
		List<CommonItemVO> list = adminService.findItemList();
		model.addAttribute("item", list);
		return "admin/commonItemList";
	}

	/**
	 *  공용품 등록 - 페이지
	 * @return
	 */
	@GetMapping("admin/commonItem")
	public String commonItemInsertForm() {
		return "admin/commonItem";
	}

	/**
	 * 공용품 등록 - 처리
	 * @param commonItemVO
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("admin/commonItem")
	public String commonItemInsert( @ModelAttribute CommonItemVO commonItemVO,
									@RequestParam("itemImage") MultipartFile file, 
									RedirectAttributes redirectAttributes) {

		// ✅ 1. 파일 저장 폴더가 없으면 생성
		File dir = new File(uploadDir + subDir);
		if (!dir.exists())
			dir.mkdirs();

		// ✅ 2. 파일이 없을 경우 예외 처리
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "🚨 업로드된 파일이 없습니다.");
			return "redirect:commonItemList";
		}

		// ✅ 3. 파일명 설정 (UUID 사용)
		String originalFilename = file.getOriginalFilename();
		String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
		List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");

		if (!allowedExtensions.contains(fileExtension)) {
			redirectAttributes.addFlashAttribute("errorMessage", "🚨 허용되지 않은 파일 형식입니다.");
			return "redirect:commonItemList";
		}

		String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
		File dest = new File(uploadDir + subDir + uniqueFileName);

		// ✅ 4. 파일 저장 및 DB 저장
		try {
			file.transferTo(dest);
			commonItemVO.setImage(subDir + uniqueFileName); // DB에 저장될 경로
			adminService.inputCommonItem(commonItemVO);
		} catch (IOException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMessage", "🚨 파일 저장 중 오류 발생!");
			return "redirect:commonItemList";
		}

		return "redirect:commonItemList";
	}

	/**
	 * 공용품 수정 - 페이지
	 * @param commonItemVO
	 * @param model
	 * @return
	 */
	@GetMapping("admin/commonItemUpdate")
	public String ItemUpdate(CommonItemVO commonItemVO, Model model) {
		CommonItemVO updateVO = adminService.findItemById(commonItemVO);
		model.addAttribute("commonItemVO", updateVO);
		return "admin/commonItemUpdate";
	}

	/**
	 * 공용품 수정 - 처리
	 * @param commonItemVO
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("admin/commonItemUpdate")
	public String ItemUpdateAJAX(@ModelAttribute CommonItemVO commonItemVO,
								@RequestParam(value = "itemImage") MultipartFile file,
								RedirectAttributes redirectAttributes) {
		// ✅ 1. 기존 데이터 조회
	    CommonItemVO existingItem = adminService.findItemById(commonItemVO);

	    // ✅ 2. 파일 업로드 폴더 생성
	    File dir = new File(uploadDir + subDir);
	    if (!dir.exists()) dir.mkdirs();

	    // ✅ 3. 새 파일이 업로드된 경우 처리
	    if (file != null && !file.isEmpty()) {
	        // 3-1. 확장자 확인
	        String originalFilename = file.getOriginalFilename();
	        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
	        List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");

	        if (!allowedExtensions.contains(fileExtension)) {
	            redirectAttributes.addFlashAttribute("errorMessage", "🚨 허용되지 않은 파일 형식입니다.");
	            return "redirect:commonItemList";
	        }

	        // 3-2. 기존 이미지 삭제
	        if (existingItem.getImage() != null) {
	            File oldFile = new File(uploadDir + subDir + existingItem.getImage());
	            if (oldFile.exists()) oldFile.delete();
	        }

	        // 3-3. 새 파일 저장
	        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
	        File dest = new File(uploadDir + subDir + uniqueFileName);

	        try {
	            file.transferTo(dest);
	            commonItemVO.setImage(subDir + uniqueFileName); // 새 이미지로 설정
	        } catch (IOException e) {
	            redirectAttributes.addFlashAttribute("errorMessage", "🚨 파일 저장 중 오류 발생!");
	            return "redirect:commonItemList";
	        }
	    } else {
	        // ✅ 4. 새 파일이 없으면 기존 이미지 유지
	        commonItemVO.setImage(existingItem.getImage());
	    }

	    // ✅ 5. DB 업데이트 실행
	    adminService.modifyItem(commonItemVO);
	    return "redirect:commonItemList";
	}

	/**
	 *  공용품 삭제 - 처리
	 * @param commonNo
	 * @return
	 */
	@GetMapping("admin/commonItemDelete")
	public String commonItemDelete(Integer commonNo) {
		// 1. 기존 데이터 조회
		CommonItemVO commonItemVO = new CommonItemVO();
		commonItemVO.setCommonNo(commonNo);
	    CommonItemVO existingItem = adminService.findItemById(commonItemVO);
	    // 2. 기존 이미지 삭제
        if (existingItem.getImage() != null) {
            File oldFile = new File(uploadDir + subDir + existingItem.getImage());
            if (oldFile.exists()) oldFile.delete();
        }
        
		adminService.dropItem(commonNo);
		
		return "redirect:commonItemList";
	}

	// 부서관리
	// 1) 부서 조회
	@GetMapping("admin/deptList")
	public String getAdminDeptList(DepartmentVO deptVO, Model model) {
		model.addAttribute("dept", adminService.findDeptList(deptVO));
		return "admin/deptList";
	}
	
	// 2) 부서 단건 조회
	@GetMapping("admin/deptInfo/{dpNo}")
	public String getAdminDeptById(DepartmentVO deptVO, Model model, @PathVariable String dpNo) {
		deptVO.setDepartmentId(dpNo);
		model.addAttribute("dept", adminService.findDeptById(deptVO));
		return "admin/deptInfo";
	}
	
	// 3) 부서 등록 페이지
	@GetMapping("admin/deptInsert")
	public String getAdminNewDepartPage(DepartmentVO deptVO, Model model) {
		EmpVO empVO = new EmpVO();
	    List<EmpVO> empList = adminService.findAllEmployees(empVO); // 사원 목록
	    List<Integer> existingHeads = adminService.findCurrentHeads(); 
	    model.addAttribute("headList", existingHeads); // 부서장 목록
	    model.addAttribute("empList", empList);                // → select용
	    
		return "admin/newDept";
	}
	
	// ✅ [1] 부서장 중복 확인 Controller (AJAX)
	@GetMapping("/admin/checkHead/{userNo}")
	@ResponseBody
	public boolean checkIfAlreadyHead(@PathVariable int userNo) {
	    List<Integer> headList = adminService.findCurrentHeads(); // 기존 부서장 리스트
	    return headList.contains(userNo); // 포함 여부 반환
	}

	// ✅ [2] 부서 등록 Controller (AJAX)
	@PostMapping("/admin/deptInsert")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> insertNewDept(@RequestBody DepartmentVO deptVO, TeamVO teamVO) {
	    Map<String, Object> result = new HashMap<>();

	    try {
	        int insertResult = adminService.inputNewDeptAndTeam(deptVO, teamVO);
	        result.put("success", insertResult > 0);
	    } catch (Exception e) {
	        result.put("success", false);
	        result.put("message", e.getMessage());
	    }

	    return ResponseEntity.ok(result);
	}
	
	// 결재문서 리스트 보기/삭제 페이지
	@GetMapping("admin/apprFormList")
	public String getAdminApprFormList(Model model) {
		model.addAttribute("apprFormList", apprFormService.findFormList());
		return "admin/apprFormList";
	}
	
	// 결재문서 추가/수정 페이지
	@GetMapping("admin/apprFormOne")
	public String getAdminApprFormOne(@RequestParam(required=false) String apprType, Model model) {
	    ApprFormVO apprFormVO = new ApprFormVO();
	    if (apprType != null && !apprType.isEmpty()) {
	        apprFormVO.setApprType(apprType);
	        apprFormVO = apprFormService.findFormById(apprFormVO);
	        model.addAttribute("apprForm", apprFormVO);
	        System.out.println(apprFormVO);
	        
	        // HTML 파일 경로 지정
	        Path filePath = Paths.get(uploadDir, apprFormDir, apprFormVO.getFormPath() + ".html");
			
	        try {
	            String content = Files.readString(filePath, StandardCharsets.UTF_8);
	            model.addAttribute("editorContent", content);
	        } catch (IOException e) {
	            e.printStackTrace();
	            model.addAttribute("editorContent", "파일을 불러올 수 없습니다.\n" + e.getMessage());
	        }
	    }
	    return "admin/apprFormOne";
	}
	
	// 결재문서 추가
	@PostMapping("admin/apprForm")
	@ResponseBody
	public ResponseEntity<Boolean> postAdminApprForm(@RequestBody ApprFormVO apprFormVO) {
		Path fileDir = Paths.get(uploadDir, apprFormDir);
		String pathResult = fileHandler.htmlStrUpload(
			apprFormVO.getFormPath(), 
			apprFormVO.getContent(), 
			fileDir.toString(), 
			true);
		
		if(pathResult == null || pathResult.equals("")) {
			return ResponseEntity.badRequest().body(false);
		}
		
		System.out.println(apprFormVO);
		int result = apprFormService.inputForm(apprFormVO);
		return ResponseEntity.ok(result > 0);
	}
	
	// 결재문서 수정
	@PutMapping("admin/apprForm")
	@ResponseBody
	public ResponseEntity<Boolean> putAdminApprForm(@RequestBody ApprFormVO apprFormVO) {
		Path fileDir = Paths.get(uploadDir, apprFormDir);
		String pathResult = fileHandler.htmlStrUpload(
			apprFormVO.getFormPath(), 
			apprFormVO.getContent(), 
			fileDir.toString(), 
			true);
		
		if(pathResult == null || pathResult.equals("")) {
			return ResponseEntity.badRequest().body(false);
		}
		
		System.out.println(apprFormVO);
		int result = apprFormService.modifyForm(apprFormVO);
		return ResponseEntity.ok(result > 0);
	}
	
	// 결재문서 삭제
	@DeleteMapping("admin/apprForm")
	@ResponseBody
	public ResponseEntity<Boolean> deleteAdminApprForm(@RequestBody ApprFormVO apprFormVO) {
		int result = apprFormService.dropForm(apprFormVO);
		return ResponseEntity.ok(result > 0);
	}
}
