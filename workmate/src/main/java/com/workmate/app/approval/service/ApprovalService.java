package com.workmate.app.approval.service;

import java.util.List;

import org.springframework.stereotype.Service;

public interface ApprovalService {
	public List<ApprovalVO> selectApprovalList(ApprovalVO approvalVO);
	public ApprovalVO selectApproval(ApprovalVO approvalVO);
	public int insertApproval(ApprovalVO approvalVO);
	public int updateApproval(ApprovalVO approvalVO);
	public int deleteApproval(ApprovalVO approvalVO);
}
