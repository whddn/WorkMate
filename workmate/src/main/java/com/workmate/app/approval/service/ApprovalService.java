package com.workmate.app.approval.service;

import java.util.List;

import org.springframework.stereotype.Service;

public interface ApprovalService {
	public List<ApprovalVO> findApprovalList(ApprovalVO approvalVO, String standard);
	public ApprovalVO findApprovalById(ApprovalVO approvalVO);
	public int inputApproval(ApprovalVO approvalVO);
	public int modifyApproval(ApprovalVO approvalVO);
	public int dropApproval(ApprovalVO approvalVO);
}
