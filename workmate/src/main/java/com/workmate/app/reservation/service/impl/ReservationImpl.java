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
	ReservationImpl(ReservationMapper reservationMapper) {
		this.reservationMapper = reservationMapper;
	}

	// 전체
	@Override
	public List<ReservationVO> findAllReserList() {
		return reservationMapper.selectReservationList();
	}

	// 단건
	public ReservationVO findReserById(ReservationVO reservationVO) {
		return reservationMapper.selectReservationById(reservationVO);
	}

	// 예약 등록
	@Override
	public int inputReserInfo(ReservationVO reservationVO) {
		return reservationMapper.insertReservationInfo(reservationVO);
	}

	// 수정
	@Override
	public Map modifyReserInfo(ReservationVO reservationVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;
		int result = reservationMapper.updateReservationInfo(reservationVO);
		if(result == 1) {
			isSuccessed = true;
		}
		map.put("result", isSuccessed);
		map.put("target", reservationVO);
		
		return map;
	}

	// 삭제
	@Override
	public Map<String, Object> dropReserInfo(int commonNo) {
		Map<String, Object> map = new HashMap<>();
		int result = reservationMapper.deleteReservationInfo(commonNo);
		if (result == 1) {
			map.put("commonNo", commonNo);
		}
		return map;
	}

}
