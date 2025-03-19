package com.workmate.app.reservation.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.workmate.app.reservation.service.ReservationService;
import com.workmate.app.reservation.service.ReservationVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	// 예약 리스트 페이지
	@GetMapping("reservation")
	public String ReserList(Model model) {
		List<ReservationVO> list = reservationService.findAllReserList();
		model.addAttribute("reser", list);
		return "reservation/reservation";
	}

	// 예약 상세 페이지
	@GetMapping("reservationDetail/{reserNo}")
	public String reservationDetail(@PathVariable("reserNo") int reserNo, Model model) {
		ReservationVO reservation = reservationService.findReserInfo(reserNo);
		model.addAttribute("reser", reservation);
		return "reservation/reservationDetail"; // reservationDetail.html 반환
	}
	
	
	
//	@GetMapping("reservationList")
//	public String reservationList(Model model) {
//		return "reservation/reservationList";
//	}

}
