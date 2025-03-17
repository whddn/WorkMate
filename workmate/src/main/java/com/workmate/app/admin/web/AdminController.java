package com.workmate.app.admin.web;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public String commonItemInset(CommonItemVO commonItemVO) {
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
	@ResponseBody
	@PostMapping("admin/commonItemUpdate")
	public Map<String, Object> ItemUpdateAJAX(@ModelAttribute CommonItemVO commonItemVO){
		 return adminService.modifyItemInfo(commonItemVO);
		 
	}
	// 공용품 삭제 - 처리
	@GetMapping("admin/commonItemDelete")
	public String commonItemDelete(Integer commonNo) {
		adminService.removeItemInfo(commonNo);
		return "redirect:commonItemList";
	}
	
	// 부서관리
	
	
}
