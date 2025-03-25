package com.workmate.app.approval.mapper;

import java.util.List;

import com.workmate.app.approval.service.ApprFormVO;

public interface ApprFormMapper {
	public List<ApprFormVO> selectApprFormList();
	public ApprFormVO selectApprFormById(ApprFormVO apprFormVO);
	public int insertApprForm(ApprFormVO apprFormVO);
	public int updateApprForm(ApprFormVO apprFormVO);
	public int deleteApprForm(ApprFormVO apprFormVO);
}
