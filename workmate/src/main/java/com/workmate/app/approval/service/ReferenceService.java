package com.workmate.app.approval.service;

import java.util.List;

public interface ReferenceService {
	List<ReferenceVO> findReferenceList(ApprovalVO approvalVO);
	int inputReference(ReferenceVO referenceVO);
}
