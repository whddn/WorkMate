package com.workmate.app.approval.service;

import java.util.List;

import com.workmate.app.employee.service.EmpVO;

public interface ApprLineService {
	public List<ApprLineVO> selectApprLineList(EmpVO empVO);
	public int insertApprLine(ApprLineVO apprLineVO);
}
