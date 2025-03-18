package com.workmate.app.reservation.web;

import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.workmate.app.reservation.service.ReservationService;
import com.workmate.app.reservation.service.ReservationVO;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class ReservationController {
	
	private final ReservationService reservationService;
	
	// 예약목록 전체조회
	@GetMapping("reservation")
	public List<ReservationVO> ReserList(){
		return reservationService.findAllReserList();
	}
	
//	// 예약목록 단건조회
//	@GetMapping("reservationDetail")
//	public String reservationDetail(Model model) {
//		return "reservation/reservationDetail";
//	}
	
	// 예약 입력
	
	// 예약 수정
	
	// 예약 삭제
	
	// 예약 목록 전체 조회
	@GetMapping("reservationList")
	public String reservationList(Model model) {
		return "reservation/reservationList";
	}
	
}
