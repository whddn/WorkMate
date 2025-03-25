package com.workmate.app.reservation.web;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	// 공용품목록 전체조회 - api요청
	@GetMapping("api/reservation/main")
	public List<ReservationVO> ReserList() {
		return reservationService.findAllReserList();
	}

	// 공용품목록 단건조회
	@GetMapping("api/reservation/detail/{commonNo}")
	public ReservationVO ReserDetail(@PathVariable Integer commonNo) {
		ReservationVO reservationVO = new ReservationVO();
		reservationVO.setCommonNo(commonNo);
		return reservationService.findReserById(reservationVO);
	}

	// 예약 삭제
	@DeleteMapping("/reservation/Delete/{reserNo}")
	public ResponseEntity<String> deleteReservation(@PathVariable int reserNo) {
	    int result = reservationService.dropReserInfo(reserNo);
	    if (result > 0) {
	        return ResponseEntity.ok("삭제 성공");
	    } else {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
	    }
	}


}
