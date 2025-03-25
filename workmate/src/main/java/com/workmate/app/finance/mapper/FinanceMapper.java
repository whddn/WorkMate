package com.workmate.app.finance.mapper;

import java.util.List;

import com.workmate.app.finance.service.FinanceVO;

public interface FinanceMapper {
	// 보고서 전체 조회
	public List<FinanceVO> selectReportList(FinanceVO financeVO);
	
	// 보고서 단건 조회
	public List<FinanceVO> selectReportById(FinanceVO financeVO);
	
	// 입출금 보고서 작성 페이지
	public String selectReportInsertPage(FinanceVO financeVO);
}
