package com.workmate.app.approval.mapper;

import java.util.List;

import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.approval.service.ReportAttachVO;

public interface ReportAttachMapper {
	public List<ReportAttachVO> selectApprovalRAList(ApprovalVO approvalVO);
	public int insertApprovalRA(ReportAttachVO reportAttachVO);
}
