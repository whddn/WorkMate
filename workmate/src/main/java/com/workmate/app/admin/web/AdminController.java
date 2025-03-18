package com.workmate.app.admin.web;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.workmate.app.admin.service.AdminService;
import com.workmate.app.reservation.service.CommonItemVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminService adminService;
	
	// 전자결재 관리
	
	
	// 공용품 관리
	@GetMapping("admin/commonItemList")
	public String commonItemList(Model model) {
		List<CommonItemVO> list = adminService.findAllItem();
		model.addAttribute("item", list);
		return "admin/commonItemList";
	}
	
	// 공용품 등록 - 페이지
	@GetMapping("admin/commonItem")
	public String commonItemInsertForm() {
		return "admin/commonItem";
	}
	
	// 공용품 등록 - 처리
	@PostMapping("admin/commonItem")
	public String commonItemInsert(@ModelAttribute CommonItemVO commonItemVO,
								   @RequestParam("itemImage") MultipartFile file) {
		// ✅ 1. Windows에서 파일 저장할 경로 설정
	    String uploadDir = "C:/CommonItemImage/";
	    File dir = new File(uploadDir);
	    if (!dir.exists()) dir.mkdirs(); 

	    // ✅ 2. 파일이 비어 있는 경우 예외 처리
	    if (file.isEmpty()) {
	        System.out.println("🚨 업로드된 파일이 없습니다.");
	        return "redirect:commonItemList";
	    }

	    // ✅ 3. 파일명 중복 방지 및 확장자 검사
	    String originalFilename = file.getOriginalFilename();
	    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
	    String fileName = System.currentTimeMillis() + "_" + originalFilename; // 중복 방지

	    // ✅ 4. 허용된 확장자인지 확인
	    List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");
	    if (!allowedExtensions.contains(fileExtension)) {
	        System.out.println("🚨 허용되지 않은 파일 형식입니다: " + fileExtension);
	        return "redirect:commonItemList";
	    }

	    // ✅ 5. 파일 저장 및 DB에 저장할 경로 설정
	    File dest = new File(uploadDir + fileName);
	    try {
	        file.transferTo(dest);
	        String imagePath = "/assets/img/reservation/" + fileName;  // DB 저장용 경로
	        commonItemVO.setImage(imagePath);

	        // 🔍 값이 정상적으로 저장되었는지 확인
	        System.out.println("✅ 저장된 이미지 경로: " + commonItemVO.getImage());

	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("🚨 파일 저장 중 오류 발생!");
	        return "redirect:commonItemList";
	    }

	    // ✅ 6. 데이터 저장
	    adminService.createCommonItemInfo(commonItemVO);
		return "redirect:commonItemList";
	}
	
	// 공용품 수정 - 페이지
	@GetMapping("admin/commonItemUpdate")
	public String ItemUpdate(CommonItemVO commonItemVO, Model model) {
		CommonItemVO updateVO = adminService.findItemInfo(commonItemVO);
		model.addAttribute("update", updateVO);
		return "admin/commonItemUpdate";
	}
	// 공용품 수정 - 처리 : AJAX => JSON
	@PostMapping("admin/commonItemUpdate")
	public String ItemUpdateAJAX(@ModelAttribute CommonItemVO commonItemVO,
											@RequestParam("itemImage") MultipartFile file){
		
		 adminService.modifyItemInfo(commonItemVO);
		 return "redirect:commonItemList";
		 
	}
	// 공용품 삭제 - 처리
	@GetMapping("admin/commonItemDelete")
	public String commonItemDelete(Integer commonNo) {
		adminService.removeItemInfo(commonNo);
		return "redirect:commonItemList";
	}
	
	// 부서관리
	
	
}
