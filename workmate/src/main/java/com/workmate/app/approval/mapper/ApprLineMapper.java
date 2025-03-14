package com.workmate.app.approval.mapper;

import java.util.List;

import com.workmate.app.approval.service.ApprElmntVO;
import com.workmate.app.approval.service.ApprLineVO;

public interface ApprLineMapper {
	public List<ApprLineVO> selectApprLineList(ApprLineVO apprLineVO);
	public List<ApprElmntVO> selectApprLine(ApprLineVO apprLineVO);
	public int insertApprLine(ApprLineVO apprLineVO);
}
