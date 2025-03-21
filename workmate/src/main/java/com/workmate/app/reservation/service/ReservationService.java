package com.workmate.app.reservation.service;

import java.util.List;
import java.util.Map;

import com.workmate.app.approval.service.ApprovalVO;

public interface ReservationService {
	// 전체조회
	public List<ReservationVO> findAllReserList();
	// 단건조회
	public ReservationVO findReserById(ReservationVO reservationVO);
	// 예약 등록
	public ReservationVO inputReserInfo(ReservationVO reservationVO);
	// 내 예약 목록 조회
	public List<ReservationVO> findAllmyReserList();
	// 수정
	public Map<String, Object> modifyReserInfo(ReservationVO reservationVO);
	// 삭제
	public Map<String, Object> dropReserInfo(int reserNo);
	// 시간중복체크
	public List<ReservationVO> findReservedTimesByCommonNo(Integer commonNo);

}
