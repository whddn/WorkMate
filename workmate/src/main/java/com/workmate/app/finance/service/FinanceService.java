package com.workmate.app.finance.service;

import java.util.List;

public interface FinanceService {
	// 리포트 전체 조회
	public List<ReportVO> findReportList(ReportVO reportVO);
	
	// 입출금 리포트 등록 화면 
	//public String findReportInsertPage(ReportVO reportVO);
	
	// 입금 리스트 조회
	public List<ReportVO> findTransList(ReportVO reportVO);
	
	// 리포트 insert 기능
	public int inputReportPage(ReportVO reportVO);
	
	// 리포트 수정 기능
	public int modifyReportPage(ReportVO reportVO);
	
	// 법인카드 등록
	public void inputCorCard(CorcardVO card) throws Exception;
	
	// 법인카드 조회
	public List<CorcardVO> findCorcardList(CorcardVO corcardVO);
	
	// 법인카드 상세
	public CorcardVO findCorcardById(CorcardVO corcardVO);
}
