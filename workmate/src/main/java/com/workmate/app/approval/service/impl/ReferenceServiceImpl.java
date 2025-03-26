package com.workmate.app.approval.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.approval.mapper.ReferenceMapper;
import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.approval.service.ReferenceService;
import com.workmate.app.approval.service.ReferenceVO;

@Service
public class ReferenceServiceImpl implements ReferenceService {
	private ReferenceMapper referenceMapper;
	
	@Autowired
	public ReferenceServiceImpl(ReferenceMapper referenceMapper) {
		this.referenceMapper = referenceMapper;
	}
	
	@Override
	public List<ReferenceVO> findReferenceList(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		return referenceMapper.selectReferenceList(approvalVO);
	}

	@Override
	public int inputReference(ReferenceVO referenceVO) {
		// TODO Auto-generated method stub
		return referenceMapper.insertReference(referenceVO);
	}

}
