package com.workmate.app.approval.service;

import java.util.List;

import com.workmate.app.employee.service.EmpVO;

public interface ApprLineService {
	public List<ApprLineVO> findApprLineList(EmpVO empVO);
	public ApprLineVO findApprLineById(ApprLineVO apprLineVO);
	public int inputApprLine(ApprLineVO apprLineVO);
}
