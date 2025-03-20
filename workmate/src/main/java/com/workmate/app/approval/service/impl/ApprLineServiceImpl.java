package com.workmate.app.approval.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.approval.mapper.ApprLineMapper;
import com.workmate.app.approval.service.ApprLineService;
import com.workmate.app.approval.service.ApprLineVO;
import com.workmate.app.employee.service.EmpVO;

@Service
public class ApprLineServiceImpl implements ApprLineService {
	private ApprLineMapper apprLineMapper;
	
	@Autowired
	ApprLineServiceImpl(ApprLineMapper apprLineMapper) {
		this.apprLineMapper = apprLineMapper;
	}
	
	@Override
	public List<ApprLineVO> findApprLineList(EmpVO empVO) {
		// TODO Auto-generated method stub
		return apprLineMapper.selectApprLineList(empVO);
	}
	
	@Override
	public ApprLineVO findApprLineById(ApprLineVO apprLineVO) {
		// TODO Auto-generated method stub
		return apprLineMapper.selectApprLineById(apprLineVO);
	}
	
	@Override
	public int inputApprLine(ApprLineVO apprLineVO) {
		// TODO Auto-generated method stub
		return 0;
	}

}
