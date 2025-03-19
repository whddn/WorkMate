package com.workmate.app.reservation.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.reservation.mapper.ReservationMapper;
import com.workmate.app.reservation.service.ReservationService;
import com.workmate.app.reservation.service.ReservationVO;

@Service
public class ReservationImpl implements ReservationService {

	private ReservationMapper reservationMapper;
	
	@Autowired
	ReservationImpl(ReservationMapper reservationMapper){
		this.reservationMapper = reservationMapper;
	}

	// 전체
	@Override
	public List<ReservationVO> findAllReserList() {
		return reservationMapper.selectReservationList();
	}

	// 단건
	@Override
	public ReservationVO findReserInfo(int reserNo) {
		return reservationMapper.selectReservationById(reserNo);
	}

	// 예약 등록
	@Override
	public int createReserInfo(ReservationVO reservationVO) {
		return reservationMapper.insertReservationInfo(reservationVO);
	}

	// 수정
	@Override
	public int modifyReserInfo(ReservationVO reservationVO) {
		int result = reservationMapper.updateReservationInfo(reservationVO);
		if(result < 1) {
			return 0;
		}
		return reservationMapper.updateReservationInfo(reservationVO);
	}

	// 삭제
	@Override
	public Map<String, Object> removeReserInfo(int commonNo) {
		Map<String, Object> map = new HashMap<>();
		int result = reservationMapper.deleteReservationInfo(commonNo);
		if(result == 1) {
			map.put("commonNo", commonNo);
		}
		return map;
	}

}
