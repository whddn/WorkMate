package com.workmate.app.approval.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.approval.mapper.ApprovalMapper;
import com.workmate.app.approval.service.ApprovalService;
import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.attendance.mapper.AttendMapper;
import com.workmate.app.reservation.mapper.ReservationMapper;

@Service
public class ApprovalServiceImpl implements ApprovalService {
	private ApprovalMapper approvalMapper;
	private AttendMapper attendMapper;
	private ReservationMapper reservationMapper;
  
	@Autowired
	ApprovalServiceImpl(ApprovalMapper approvalMapper, AttendMapper attendMapper) {
		this.approvalMapper = approvalMapper;
		this.attendMapper = attendMapper;
	}
	
	@Override
	public List<ApprovalVO> findApprovalList(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		return approvalMapper.selectApprovalList(approvalVO);
	}

	@Override
	public List<ApprovalVO> findApprovalListAboutMe(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		return approvalMapper.selectApprovalListAboutMe(approvalVO);
	}
	
	@Override
	public ApprovalVO findApprovalById(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		return approvalMapper.selectApprovalById(approvalVO);
	}

	@Override
	public int inputApproval(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		return approvalMapper.insertApproval(approvalVO);
	}

	@Override
	public int modifyApproval(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		//최종결재완료
		int result = approvalMapper.updateApprovalStatus(approvalVO);
		if(result < 1) {
			return 0;
		}
		// 최종 결재 완료 후 휴가/연차 인 경우 연차갯수 차감
		ApprovalVO approval = approvalMapper.selectApprovalById(approvalVO);
		if ( "a2".equals(approval.getApprStatus())  && "AF001".equals(approval.getApprType()) ) {
			attendMapper.updateOccList(approvalVO.getUserNo());
		}
		
		// 예약 결재 후 / 승인변경
		if("a2".equals(approval.getApprStatus()) && "AF004".equals(approval.getApprType())) {
			reservationMapper.updateReserStatus(approvalVO.getReserNo());
		}
		
		return approvalMapper.updateApprovalDate(approvalVO);
	}

	@Override
	public int dropApproval(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		return approvalMapper.deleteApproval(approvalVO);
	}
}
