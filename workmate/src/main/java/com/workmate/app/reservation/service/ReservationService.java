package com.workmate.app.reservation.service;

import java.util.List;
import java.util.Map;

public interface ReservationService {

	// 전체조회
	public List<ReservationVO> findAllReserList();

	// 단건조회
	public ReservationVO findReserInfo(int reserNo);

	// 예약 등록
	public int createReserInfo(ReservationVO reservationVO);

	// 수정
	public int modifyReserInfo(ReservationVO reservationVO);

	// 삭제
	public Map<String, Object> removeReserInfo(int commonNo);

}
