package com.workmate.app.reservation.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.workmate.app.reservation.service.ReservationService;
import com.workmate.app.reservation.service.ReservationVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	// 예약 리스트 페이지
	@GetMapping("reservation/main")
	public String ReserList(Model model) {
		List<ReservationVO> list = reservationService.findAllReserList();
		model.addAttribute("reser", list);
		return "reservation/reservation";
	}

	// 예약 상세 페이지
	@GetMapping("reservation/detail/{commonNo}")
	public String ReserDetail(ReservationVO reservationVO, Model model) {
		ReservationVO detailVO = reservationService.findReserById(reservationVO);
		model.addAttribute("reser", detailVO);
	    return "reservation/reservationDetail";
	}
	
	
	// 예약 현황 페이지
	@GetMapping("reservation/List")
	public String myReserList(Model model) {
		return "reservation/reservationList";
	}

}
