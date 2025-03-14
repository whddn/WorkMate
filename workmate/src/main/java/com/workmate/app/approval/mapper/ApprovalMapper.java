package com.workmate.app.approval.mapper;

import java.util.List;

import com.workmate.app.approval.service.ApprovalVO;

public interface ApprovalMapper {
	public List<ApprovalVO> selectApprovalList(ApprovalVO approvalVO);
	public ApprovalVO selectApproval(ApprovalVO approvalVO);
	public int insertApproval(ApprovalVO approvalVO);
	public int updateApproval(ApprovalVO approvalVO);
	public int deleteApproval(String apprNo);
}
