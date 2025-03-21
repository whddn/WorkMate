package com.workmate.app.reservation.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workmate.app.approval.mapper.ApprElmntMapper;
import com.workmate.app.approval.mapper.ApprLineMapper;
import com.workmate.app.approval.mapper.ApprovalMapper;
import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.reservation.mapper.ReservationMapper;
import com.workmate.app.reservation.service.ReservationService;
import com.workmate.app.reservation.service.ReservationVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationImpl implements ReservationService {

	private final ReservationMapper reservationMapper;
	private final ApprovalMapper approvalMapper;
	private final ApprLineMapper apprLineMapper;
	private final ApprElmntMapper apprElmntMapper;


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
	@Transactional
	@Override
	public ReservationVO inputReserInfo(ReservationVO reservationVO) {
		// 예약 등록
		reservationMapper.insertReservationInfo(reservationVO);
		
		// 전자결재 등록
	    ApprovalVO approval = new ApprovalVO();
	    approval.setApprTitle("공용품 예약 결재 요청");
	    approval.setApprContent(reservationVO.getContent());
//	    approval.setCreateDate();
//	    approval.setExpireDate();
	    approval.setApprStatus("a1");
	    approval.setDeptNo("D004");  // 예시: 부서번호 (실제 값에 맞게 설정)
	    approval.setUserNo(reservationVO.getUserNo());  // 사원번호 전달받았을 경우
	    approval.setReserNo(reservationVO.getReserNo()); // 예약번호 연결
	    approval.setApprType("AF004");
		
		approvalMapper.insertApproval(approval);
		
		return reservationVO;
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
	
	// 시간 중복 체크
	@Override
	public List<ReservationVO> findReservedTimesByCommonNo(Integer commonNo) {
		return reservationMapper.selectReservedTimes(commonNo);
	}

}
