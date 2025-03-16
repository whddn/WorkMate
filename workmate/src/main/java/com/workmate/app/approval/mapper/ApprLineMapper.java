package com.workmate.app.approval.mapper;

import java.util.List;

import com.workmate.app.approval.service.ApprLineVO;
import com.workmate.app.employee.service.EmpVO;

public interface ApprLineMapper {
	public List<ApprLineVO> selectApprLineList(EmpVO empVO);
	public ApprLineVO selectApprLine(ApprLineVO apprLineVO);
	public int insertApprLine(ApprLineVO apprLineVO);
}
