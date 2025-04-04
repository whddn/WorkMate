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
	public List<ApprFormVO> findFormList() {
		// TODO Auto-generated method stub
		return apprFormMapper.selectApprFormList();
	}

	@Override
	public ApprFormVO findFormById(ApprFormVO apprFormVO) {
		// TODO Auto-generated method stub
		return apprFormMapper.selectApprFormById(apprFormVO);
	}

	@Override
	public int inputForm(ApprFormVO apprFormVO) {
		// TODO Auto-generated method stub
		return apprFormMapper.insertApprForm(apprFormVO);
	}

	@Override
	public int modifyForm(ApprFormVO apprFormVO) {
		// TODO Auto-generated method stub
		return apprFormMapper.updateApprForm(apprFormVO);
	}

	@Override
	public int dropForm(ApprFormVO apprFormVO) {
		// TODO Auto-generated method stub
		return apprFormMapper.deleteApprForm(apprFormVO);
	}

}
