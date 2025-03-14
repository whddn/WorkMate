package com.workmate.app.approval.service;

import java.util.List;

public interface ApprLineService {
	public List<ApprLineVO> selectApprLineList(ApprLineVO apprLineVO);
	public List<ApprElmntVO> selectApprLine(ApprLineVO apprLineVO);
	public int insertApprLine(ApprLineVO apprLineVO);
}
