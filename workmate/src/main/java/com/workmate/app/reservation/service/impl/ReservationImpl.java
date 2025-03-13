package com.workmate.app.reservation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.reservation.mapper.ReservationMapper;
import com.workmate.app.reservation.service.CommonItemVO;
import com.workmate.app.reservation.service.ReservationService;

@Service
public class ReservationImpl implements ReservationService {

	private ReservationMapper reservationMapper;
	
	@Autowired
	ReservationImpl(ReservationMapper reservationMapper){
		this.reservationMapper = reservationMapper;
	}
	
	@Override
	public int createCommonInfo(CommonItemVO commonitemVO) {
		int result = reservationMapper.insertCommonItemInfo(commonitemVO);

		return result == 1 ? commonitemVO.getCommonNo() : -1;
	}
	
}
