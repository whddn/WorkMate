package com.workmate.app.reservation.mapper;

import java.util.List;
import com.workmate.app.reservation.service.CommonItemVO;
import com.workmate.app.reservation.service.ReservationVO;

public interface ReservationMapper {
	
	// 전체조회
	public List<ReservationVO> selectReservationList();
	
	// 관리자 - 공용품등록
	public int insertCommonItemInfo(CommonItemVO commonitemVO);
	
	// 관리자 - 삭제
	public int deleteCommonItemInfo(CommonItemVO commonitemVO);
	
	// 예약신청
	public int insertReservationInfo(ReservationVO reservationVO);
	
}
