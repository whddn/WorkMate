package com.workmate.app.reservation.web;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.workmate.app.reservation.service.ReservationService;
import com.workmate.app.reservation.service.ReservationVO;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class RestReservationController {

	// Rest API
	private final ReservationService reservationService;

	// 예약목록 전체조회 - api요청
	@GetMapping("api/reservation/main")
	public List<ReservationVO> ReserList() {
		return reservationService.findAllReserList();
	}

	// 예약목록 단건조회
	@GetMapping("api/reservation/detail/{commonNo}")
	public ReservationVO ReserDetail(@PathVariable Integer commonNo) {
		ReservationVO reservationVO = new ReservationVO();
		reservationVO.setCommonNo(commonNo);
		return reservationService.findReserById(reservationVO);
	}

	// 예약 입력

	// 예약 수정

	// 예약 삭제

	// 예약 목록 전체 조회

}
