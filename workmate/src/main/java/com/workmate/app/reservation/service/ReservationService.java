package com.workmate.app.reservation.service;

import java.util.List;
import java.util.Map;

public interface ReservationService {
	// 전체조회
	public List<ReservationVO> findAllReserList();
	// 단건조회
	public ReservationVO findReserById(ReservationVO reservationVO);
	// 예약 등록
	public int inputReserInfo(ReservationVO reservationVO);
	// 수정
	public Map<String, Object> modifyReserInfo(ReservationVO reservationVO);
	// 삭제
	public Map<String, Object> dropReserInfo(int reserNo);

}
