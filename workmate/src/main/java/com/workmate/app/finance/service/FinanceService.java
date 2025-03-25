package com.workmate.app.finance.service;

import java.util.List;

public interface FinanceService {
	// 리포트 전체 조회
	public List<FinanceVO> findReportList(FinanceVO financeVO);
	
	// 리포트 단건 조회
	public List<FinanceVO> findReportById(FinanceVO financeVO);
	
	// 입출금 리포트 등록 화면 
	public String findReportInsertPage(FinanceVO financeVO);
}
