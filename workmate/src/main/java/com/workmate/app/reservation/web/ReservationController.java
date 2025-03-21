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
	public String ReserDetail(@PathVariable Integer commonNo, Model model) throws JsonProcessingException {
	    ReservationVO vo = new ReservationVO();
	    vo.setCommonNo(commonNo);
	    // 1. 상세조회
	    ReservationVO detailVO = reservationService.findReserById(vo);
	    model.addAttribute("reser", detailVO);
	    // 2. 모듈등록
	    List<ReservationVO> reservedList = reservationService.findReservedTimesByCommonNo(commonNo);
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
	    String reservedListJson = objectMapper.writeValueAsString(reservedList);
	    // 3. view로 보여주기
	    model.addAttribute("reservedListJson", reservedListJson);
	    return "reservation/reservationDetail";
	}

	
	// 예약 신청
	@PostMapping("reservation/Info")
	public String insertReserInfo(@ModelAttribute ReservationVO reservationVO) {
	    reservationService.inputReserInfo(reservationVO);
	    return "redirect:List";
	}
	
	
	// 예약 현황 페이지
	@GetMapping("reservation/List")
	public String myReserList(Model model) {
		return "reservation/reservationList";
	}
	

}
