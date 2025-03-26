package com.workmate.app.finance.service;

import java.util.List;

public interface FinanceService {
	// 리포트 전체 조회
	public List<ReportVO> findReportList(ReportVO reportVO);
	
	// 입출금 리포트 등록 화면 
	public String findReportInsertPage(ReportVO reportVO);
	
	// 입금 리스트 조회
	public List<ReportVO> findTransList(ReportVO reportVO);
	
}
