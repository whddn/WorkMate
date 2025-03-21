package com.workmate.app.mainscreen.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.workmate.app.approval.service.ApprovalService;
import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.common.WhoAmI;
import com.workmate.app.mainscreen.service.MenuService;
import com.workmate.app.mainscreen.service.MenuVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainscreenController {
	private final MenuService menuService;
	private final ApprovalService approvalService;
	private final WhoAmI whoAmI = new WhoAmI();
	
	@GetMapping("/")
	public String base(Model model) {
		ApprovalVO approvalVO = new ApprovalVO();
		approvalVO.setUserNo(whoAmI.whoAmI().getUserNo());
		approvalVO.setApprStatus("a1");
		approvalVO.setStandard("toMe");
		model.addAttribute("waitingList", approvalService.findApprovalList(approvalVO));
		return "mainscreen/main";
	}
	
	@GetMapping("/search")
	public String getSearch(Model model) {
		return "mainscreen/search";
	}
	
	@PostMapping("/search/start")
	@ResponseBody
	public List<MenuVO> postSearchStart(@RequestBody MenuVO menuVO) {
		return menuService.findMenuList(menuVO);
	}
}
