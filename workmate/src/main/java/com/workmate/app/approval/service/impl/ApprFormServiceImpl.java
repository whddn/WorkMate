package com.workmate.app.approval.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.approval.mapper.ApprFormMapper;
import com.workmate.app.approval.service.ApprFormService;
import com.workmate.app.approval.service.ApprFormVO;

@Service
public class ApprFormServiceImpl implements ApprFormService {
	private ApprFormMapper apprFormMapper;
	
	@Autowired
	ApprFormServiceImpl(ApprFormMapper apprFormMapper) {
		this.apprFormMapper = apprFormMapper;
	}
	
	@Override
	public List<ApprFormVO> selectFormList() {
		// TODO Auto-generated method stub
		return apprFormMapper.selectApprFormList();
	}

	@Override
	public ApprFormVO selectForm(ApprFormVO apprFormVO) {
		// TODO Auto-generated method stub
		return apprFormMapper.selectApprForm(apprFormVO);
	}

}
