package com.workmate.app.finance.mapper;

import java.util.List;

import com.workmate.app.finance.service.FinanceVO;

public interface FinanceMapper {
	// 보고서 전체 조회
	public List<FinanceVO> selectReportList();
	
	// 보고서 단건 조회
	public FinanceVO selectReportById();
}
