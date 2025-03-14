package com.workmate.app.approval.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.approval.mapper.ApprLineMapper;
import com.workmate.app.approval.service.ApprElmntVO;
import com.workmate.app.approval.service.ApprLineService;
import com.workmate.app.approval.service.ApprLineVO;

@Service
public class ApprLineServiceImpl implements ApprLineService {
	private ApprLineMapper apprLineMapper;
	
	@Autowired
	ApprLineServiceImpl(ApprLineMapper apprLineMapper) {
		this.apprLineMapper = apprLineMapper;
	}
	
	@Override
	public List<ApprLineVO> selectApprLineList(ApprLineVO apprLineVO) {
		// TODO Auto-generated method stub
		return apprLineMapper.selectApprLineList(apprLineVO);
	}

	@Override
	public List<ApprElmntVO> selectApprLine(ApprLineVO apprLineVO) {
		// TODO Auto-generated method stub
		return apprLineMapper.selectApprLine(apprLineVO);
	}

	@Override
	public int insertApprLine(ApprLineVO apprLineVO) {
		// TODO Auto-generated method stub
		return 0;
	}

}
