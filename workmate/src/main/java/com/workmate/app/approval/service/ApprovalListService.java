package com.workmate.app.approval.service;

import java.util.List;

import org.springframework.stereotype.Service;

public interface ApprovalListService {
	public List<ApprovalVO> selectApprovalList(ApprovalVO approvalVO);
	public ApprovalVO selectApproval(ApprovalVO approvalVO);
}
