package com.workmate.app.reservation.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.workmate.app.reservation.service.ReservationVO;

public interface ReservationMapper {
	
	//  공용품 목록 전체조회
	public List<ReservationVO> selectReservationList();
	// 공용품 목록 단건 조회(공용품 번호로 조회)
	public ReservationVO selectReservationById(ReservationVO reservationVO);
	// 내 예약 목록 조회
	public List<ReservationVO> selectmyReservationList(int userNo);
	// 예약신청
	public int insertReservationInfo(ReservationVO reservationVO);
	// 예약수정
	public int updateReserInfo(ReservationVO reservationVO);
	// 예약수정 전 한건 조회(예약번호로 조회)
	public ReservationVO selectReserByNo(int reserNo);
	// 예약취소
	public int deleteReservationInfo(int reserNo);
	// 시간 중복 체크
	public List<ReservationVO> selectReservedTimes(Integer commonNo);
}
