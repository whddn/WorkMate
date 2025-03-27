package com.workmate.app.approval.service;

import java.util.List;

public interface ReportAttachService {
	public List<ReportAttachVO> findApprovalRAList(ApprovalVO approvalVO);
	public ReportAttachVO findApprovalRA(Integer repattachNo);
	public int insertApprovalRA(ReportAttachVO reportAttachVO);
}
