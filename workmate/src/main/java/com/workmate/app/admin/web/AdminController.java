package com.workmate.app.admin.web;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.workmate.app.admin.service.AdminService;
import com.workmate.app.common.FileHandler;
import com.workmate.app.reservation.service.CommonItemVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;
	private final FileHandler fileHandler = new FileHandler();

	// ✅ `application.properties`에서 파일 저장 경로 가져오기
	@Value("${file.upload-dir}")
	private String uploadDir;

	private String subDir ="CommonItemImage/";
	
	// 전자결재 관리

	// 공용품 관리
	@GetMapping("admin/commonItemList")
	public String commonItemList(Model model) {
		List<CommonItemVO> list = adminService.findItemList();
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
			@RequestParam("itemImage") MultipartFile file, RedirectAttributes redirectAttributes) {

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
			commonItemVO.setImage(uniqueFileName); // DB에 저장될 경로
			adminService.inputCommonItem(commonItemVO);
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "🚨 파일 저장 중 오류 발생!");
			return "redirect:commonItemList";
		}

		return "redirect:commonItemList";
	}

	// 공용품 수정 - 페이지
	@GetMapping("admin/commonItemUpdate")
	public String ItemUpdate(CommonItemVO commonItemVO, Model model) {
		CommonItemVO updateVO = adminService.findItemById(commonItemVO);
		model.addAttribute("commonItemVO", updateVO);
		return "admin/commonItemUpdate";
	}

	// 공용품 수정 - 처리 : AJAX => JSON
	@PostMapping("admin/commonItemUpdate")
	public String ItemUpdateAJAX(@ModelAttribute CommonItemVO commonItemVO,
			@RequestParam(value = "itemImage") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		// ✅ 1. 기존 데이터 조회
		CommonItemVO existingItem = adminService.findItemById(commonItemVO);
		// ✅ 2-2. 기존 파일 삭제 (새 파일 업로드 시)
		if (existingItem.getImage() != null) {
			File oldFile = new File(existingItem.getImage());
			if (oldFile.exists()) {
				oldFile.delete(); // 기존 파일 삭제
				System.out.println("🗑 기존 파일 삭제됨: " + oldFile.getAbsolutePath());
			}
		}

		// ✅ 2. 새 파일이 업로드된 경우 처리
		if (file != null && !file.isEmpty()) {
			fileHandler.fileUpload(file);
		} else {
			// ✅ 3. 새 파일이 없으면 기존 파일 유지
			commonItemVO.setImage(existingItem.getImage());
		}
//		commonItemVO.setImage(uniqueFileName);

		// ✅ 4. DB 업데이트 실행
		adminService.modifyItem(commonItemVO);
		return "redirect:commonItemList";
	}

	// 공용품 삭제 - 처리
	@GetMapping("admin/commonItemDelete")
	public String commonItemDelete(Integer commonNo) {
		adminService.dropItem(commonNo);
		return "redirect:commonItemList";
	}

	// 부서관리

}
