package com.workmate.app.finance.service;

import java.util.List;

public interface FinanceService {
	// 리포트 전체 조회
	public List<FinanceVO> findReportList();
	
//	// 리포트 단건 조회
//	public List<FinanceVO> findReportById();
}
