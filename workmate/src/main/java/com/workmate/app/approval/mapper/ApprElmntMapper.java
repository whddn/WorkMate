package com.workmate.app.approval.mapper;

import java.util.List;

import com.workmate.app.approval.service.ApprElmntVO;
import com.workmate.app.approval.service.ApprovalVO;

public interface ApprElmntMapper {
	public List<ApprElmntVO> selectApprElmntList(ApprovalVO approvalVO);
	public int insertApprElmnt(ApprElmntVO apprElmntVO);
	public int updateApprElmnt(ApprElmntVO apprElmntVO);
}
