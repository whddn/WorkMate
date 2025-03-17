package com.workmate.app.reservation.mapper;

import java.util.List;
import com.workmate.app.reservation.service.CommonItemVO;
import com.workmate.app.reservation.service.ReservationVO;

public interface ReservationMapper {
	
	// 전체조회 - 목록
	public List<ReservationVO> selectReservationList();
	
	// 단건조회 - 예약정보
	public ReservationVO selectReservationInfo(ReservationVO reservationVO);
	
	// 예약신청
	public int insertReservationInfo(ReservationVO reservationVO);
	
	// 예약취소
	
}
