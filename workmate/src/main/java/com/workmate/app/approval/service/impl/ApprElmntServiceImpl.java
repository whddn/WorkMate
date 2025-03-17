package com.workmate.app.approval.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.approval.mapper.ApprElmntMapper;
import com.workmate.app.approval.service.ApprElmntService;
import com.workmate.app.approval.service.ApprElmntVO;
import com.workmate.app.approval.service.ApprovalVO;

@Service
public class ApprElmntServiceImpl implements ApprElmntService {
	private ApprElmntMapper apprElmntMapper;
	
	@Autowired
	ApprElmntServiceImpl(ApprElmntMapper apprElmntMapper) {
		this.apprElmntMapper = apprElmntMapper;
	}
	
	@Override
	public List<ApprElmntVO> selectApprElmntList(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		return apprElmntMapper.selectApprElmntList(approvalVO);
	}

	@Override
	public int insertApprElmnt(ApprElmntVO apprElmntVO) {
		// TODO Auto-generated method stub
		return apprElmntMapper.insertApprElmnt(apprElmntVO);
	}

	@Override
	public int updateApprElmnt(ApprElmntVO apprElmntVO) {
		// TODO Auto-generated method stub
		return apprElmntMapper.updateApprElmnt(apprElmntVO);
	}

}
