package com.workmate.app.approval.service;

import java.util.List;

public interface ApprElmntService {
	public List<ApprElmntVO> selectApprElmntList(ApprovalVO approvalVO);
	public int insertApprElmnt(ApprElmntVO apprElmntVO);
	public int updateApprElmnt(ApprElmntVO apprElmntVO);
}
