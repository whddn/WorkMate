package com.workmate.app.approval.mapper;

import java.util.List;

import com.workmate.app.approval.service.ApprovalVO;

public interface ApprovalMapper {
	public List<ApprovalVO> selectApprovalList(ApprovalVO approvalVO);
	public ApprovalVO selectApprovalById(ApprovalVO approvalVO);
	public int insertApproval(ApprovalVO approvalVO);
	public int updateApprovalStatus(ApprovalVO approvalVO);
	public int updateApprovalDate(ApprovalVO approvalVO);
	public int deleteApproval(ApprovalVO approvalVO);
}
