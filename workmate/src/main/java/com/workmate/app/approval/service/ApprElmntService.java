package com.workmate.app.approval.service;

import java.util.List;

public interface ApprElmntService {
	public List<ApprElmntVO> findApprElmntList(ApprovalVO approvalVO);
	public int inputApprElmnt(ApprElmntVO apprElmntVO);
	public int modifyApprElmnt(ApprElmntVO apprElmntVO);
}
