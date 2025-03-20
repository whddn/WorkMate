package com.workmate.app.approval.mapper;

import java.util.List;

import com.workmate.app.approval.service.ApprovalVO;

public interface ApprovalMapper {
	// 작성자가 나인 결재문서 목록을 리턴
	public List<ApprovalVO> selectApprovalListFromMe(ApprovalVO approvalVO);
	// 결재자가 나인 결재문서 목록을 리턴
	public List<ApprovalVO> selectApprovalListToMe(ApprovalVO approvalVO);
	// 참조자가 나인 결재문서 목록을 리턴
	public List<ApprovalVO> selectApprovalListRefMe(ApprovalVO approvalVO);
	// 
	public List<ApprovalVO> selectApprovalListAllow(ApprovalVO approvalVO);
	// 반려된 문서 목록을 리턴
	public List<ApprovalVO> selectApprovalListReject(ApprovalVO approvalVO);
	public ApprovalVO selectApprovalById(ApprovalVO approvalVO);
	public int insertApproval(ApprovalVO approvalVO);
	public int updateApprovalStatus(ApprovalVO approvalVO);
	public int updateApprovalDate(ApprovalVO approvalVO);
	public int deleteApproval(ApprovalVO approvalVO);
}
