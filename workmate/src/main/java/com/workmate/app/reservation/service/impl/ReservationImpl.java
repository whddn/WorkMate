package com.workmate.app.reservation.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workmate.app.approval.mapper.ApprElmntMapper;
import com.workmate.app.approval.mapper.ApprLineMapper;
import com.workmate.app.approval.mapper.ApprovalMapper;
import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.common.WhoAmI;
import com.workmate.app.employee.service.EmpVO;
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
		// 현재 로그인한 유저 정보 가져오기
		WhoAmI whoAmI = new WhoAmI();
		EmpVO currentUser = whoAmI.whoAmI();
		
		// ✅ reservationVO에 사원번호 주입
	    if (reservationVO.getUserNo() == null) {
	        reservationVO.setUserNo(currentUser.getUserNo());
	    }
		
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
	public int modifyReserInfo(ReservationVO reservationVO) {
		 return reservationMapper.updateReserInfo(reservationVO);
	}
	// 수정 전 한건 조회
	@Override
	public ReservationVO findReserByNo(int reserNo) {
	    return reservationMapper.selectReserByNo(reserNo);
	}

	// 삭제
	@Override
	public int dropReserInfo(int reserNo) {
		 return reservationMapper.deleteReservationInfo(reserNo);
	}

	
	// 시간 중복 체크
	@Override
	public List<ReservationVO> findReservedTimesByCommonNo(Integer commonNo) {
		return reservationMapper.selectReservedTimes(commonNo);
	}
	
	// 내 예약 목록 조회
	@Override
	public List<ReservationVO> findAllmyReserList() {
		return reservationMapper.selectmyReservationList();
	}

	

}
