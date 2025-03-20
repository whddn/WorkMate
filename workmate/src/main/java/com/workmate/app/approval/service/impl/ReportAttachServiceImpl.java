package com.workmate.app.approval.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.approval.mapper.ReportAttachMapper;
import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.approval.service.ReportAttachService;
import com.workmate.app.approval.service.ReportAttachVO;

@Service
public class ReportAttachServiceImpl implements ReportAttachService {
	private ReportAttachMapper reportAttachMapper;
	
	@Autowired
	ReportAttachServiceImpl(ReportAttachMapper reportAttachMapper) {
		this.reportAttachMapper = reportAttachMapper;
	}

	@Override
	public List<ReportAttachVO> findApprovalRAList(ApprovalVO approvalVO) {
		// TODO Auto-generated method stub
		return reportAttachMapper.selectApprovalRAList(approvalVO);
	}

	@Override
	public int insertApprovalRA(ReportAttachVO reportAttachVO) {
		// TODO Auto-generated method stub
		return reportAttachMapper.insertApprovalRA(reportAttachVO);
	}

}
