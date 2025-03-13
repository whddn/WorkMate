package com.workmate.app.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.workmate.app.reservation.service.CommonItemVO;
import com.workmate.app.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {
	
	private final ReservationService reservationService;
	
	// 전자결재 관리
	
	// 예약관리
	@GetMapping("admin/commonItem")
	public String admin(Model model) {
		return "admin/commonItem";
	}
	
	@PostMapping("admin/commonItem")
	public CommonItemVO commonItemInsert(@RequestBody CommonItemVO commonitemVO) {
		int result = reservationService.createCommonInfo(commonitemVO);

		if (result > -1) {
			return commonitemVO;
		} else {
			return new CommonItemVO();
		}
	}
	
	// 부서관리
	
	
}
