package com.workmate.app.approval.service.impl;

import java.util.ArrayList;
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
	public List<ApprovalVO> findApprovalList(ApprovalVO approvalVO, String standard) {
		// TODO Auto-generated method stub
		List<ApprovalVO> result = new ArrayList<>();
		if(standard.equals("fromMe"))
			result = approvalMapper.selectApprovalListFromMe(approvalVO);
		else if(standard.equals("toMe"))
			result = approvalMapper.selectApprovalListToMe(approvalVO);
		else if(standard.equals("refMe"))
			result = approvalMapper.selectApprovalListRefMe(approvalVO);
		else if(standard.equals("allow"))
			result = approvalMapper.selectApprovalListAllow(approvalVO);
		else if(standard.equals("reject"))
			result = approvalMapper.selectApprovalListReject(approvalVO);
		return result;
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
		int result = approvalMapper.updateApprovalStatus(approvalVO);
		if(result < 1) {
			return 0;
		}
		return approvalMapper.updateApprovalDate(approvalVO);
	}

	@Override
	public int dropApproval(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		return approvalMapper.deleteApproval(approvalVO);
	}
}
