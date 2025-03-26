package com.workmate.app.approval.mapper;

import java.util.List;

import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.approval.service.ReferenceVO;

public interface ReferenceMapper {
	public List<ReferenceVO> selectReferenceList(ApprovalVO approvalVO);
	public int insertReference(ReferenceVO referenceVO);
}
