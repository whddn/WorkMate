package com.workmate.app.approval.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.approval.mapper.ApprovalMapper;
import com.workmate.app.approval.service.ApprovalService;
import com.workmate.app.approval.service.ApprovalVO;

@Service
public class ApprovalServiceImpl implements ApprovalService {
	private ApprovalMapper approvalMapper;
	
	@Autowired
	ApprovalServiceImpl(ApprovalMapper approvalMapper) {
		this.approvalMapper = approvalMapper;
	}
	
	@Override
	public List<ApprovalVO> selectApprovalList(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		return approvalMapper.selectApprovalList(approvalVO);
	}

	@Override
	public ApprovalVO selectApproval(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		return approvalMapper.selectApproval(approvalVO);
	}

	@Override
	public String insertApproval(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		return approvalMapper.insertApproval(approvalVO);
	}

	@Override
	public int updateApproval(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		return approvalMapper.updateApproval(approvalVO);
	}
}
