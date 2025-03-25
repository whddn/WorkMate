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
	public List<ReservationVO> findAllmyReserList(int userNo);
	// 수정
	public int modifyReserInfo(ReservationVO reservationVO);
	// 수정 전 한건 조회
	public ReservationVO findReserByNo(int reserNo);
	// 삭제
	public int dropReserInfo(int reserNo);
	// 시간중복체크
	public List<ReservationVO> findReservedTimesByCommonNo(Integer commonNo);

}
