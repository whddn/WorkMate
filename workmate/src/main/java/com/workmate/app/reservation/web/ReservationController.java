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
/** 예약 페이지
 * @author 이종우
 * @since 2025-03-20
 * <pre>
 * <pre>
 * 수정일자	수정자	수정내용
 * -------------------------
 * 03-19	이종우	예약페이지 생성
 * 03-20	이종우	예약상세 생성
 * 03-21	이종우	예약-전자결재 연결, 예약 중복체크
 * 03-24	이종우	수정,삭제
 * 
 * </pre>
 */

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
	    model.addAttribute("reservedList", reservedList);

	    return "reservation/reservationDetail";
	}
	
	// 예약 신청
	@PostMapping("reservation/input")
	public String saveReservation(@ModelAttribute ReservationVO reservationVO) {
	    if (reservationVO.getReserNo() == null) {
	        // 등록
	        reservationService.inputReserInfo(reservationVO);
	    } else {
	        // 수정
	        reservationService.modifyReserInfo(reservationVO);
	    }
	    return "redirect:/reservation/List";
	}

	
	// 예약 목록 페이지
	@GetMapping("reservation/List")
	public String myReserList(Model model) {
		List<ReservationVO> list = reservationService.findAllmyReserList();
		model.addAttribute("reser", list);
		return "reservation/reservationList";
	}
	
	// 예약 수정
	@GetMapping("/reservation/edit/{reserNo}")
	public String editReservationForm(@PathVariable int reserNo, Model model) {
	    ReservationVO reser = reservationService.findReserByNo(reserNo); // 조회 메서드로 변경
	    List<ReservationVO> reservedList = reservationService.findReservedTimesByCommonNo(reser.getCommonNo()); // 기존 예약 목록
	    
	    model.addAttribute("reser", reser);
	    model.addAttribute("reservedList", reservedList);
	    return "reservation/reservationDetail";
	}


}
