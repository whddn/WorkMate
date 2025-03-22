package com.workmate.app.reservation.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.workmate.app.reservation.service.ReservationService;
import com.workmate.app.reservation.service.ReservationVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	// 공용품 리스트 페이지
	@GetMapping("reservation/main")
	public String ReserList(Model model) {
		List<ReservationVO> list = reservationService.findAllReserList();
		model.addAttribute("reser", list);
		return "reservation/reservation";
	}

	// 공용품 상세 페이지
	@GetMapping("/reservation/detail/{commonNo}")
	public String reservationDetail(@PathVariable Integer commonNo, Model model) {
	    ReservationVO vo = new ReservationVO();
	    vo.setCommonNo(commonNo);

	    ReservationVO detailVO = reservationService.findReserById(vo);
	    model.addAttribute("reser", detailVO);

	    List<ReservationVO> reservedList = reservationService.findReservedTimesByCommonNo(commonNo);
	    model.addAttribute("reservedList", reservedList); // ✅ 바로 넘기기

	    return "reservation/reservationDetail";
	}
	
	// 예약 신청
	@PostMapping("reservation/input")
	public String insertReserInfo(@ModelAttribute ReservationVO reservationVO) {
	    reservationService.inputReserInfo(reservationVO);
	    return "redirect:List";
	}
	
	// 예약 목록 페이지
	@GetMapping("reservation/List")
	public String myReserList(Model model) {
		List<ReservationVO> list = reservationService.findAllmyReserList();
		model.addAttribute("reser", list);
		return "reservation/reservationList";
	}
	

}
