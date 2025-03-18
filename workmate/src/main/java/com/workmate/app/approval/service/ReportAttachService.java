package com.workmate.app.approval.service;

import java.util.List;

public interface ReportAttachService {
	public List<ReportAttachVO> selectApprovalRAList(ApprovalVO approvalVO);
	public int insertApprovalRA(ReportAttachVO reportAttachVO);
}
