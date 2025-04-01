package com.workmate.app.finance.mapper;

import java.util.List;

import com.workmate.app.finance.service.CorcardVO;
import com.workmate.app.finance.service.ReportVO;

public interface FinanceMapper {
	// 보고서 전체 조회
	public List<ReportVO> selectReportList(ReportVO reportVO);
	
	// 입출금 거래 내역 보고서 하나에 대한 전체 입/출금 리스트 조회
	public List<ReportVO> selectTransList(ReportVO reportVO);
	
	// 입출금 보고서 작성 페이지
	public String selectReportInsertPage(ReportVO reportVO);
	
	// 입출금 보고서 insert 기능
	public int insertReportOne(ReportVO reportVO);
	
	// 입출금 보고서의 거래 내역 insert 기능
	public int insertReportTransOne(ReportVO reportVO);
	
	// 입출금 보고서 수정 기능 (Report 테이블)
	public int updateReportOne(ReportVO reportVO);
	
	// 거래 내역 수정 기능 (TransHistory 테이블)
	public int updateTransHistory(ReportVO reportVO);
	
	// 법인 카드 전체 조회 (관리자만 가능)
	public List<CorcardVO> selectCorCardList(CorcardVO corcardVO);
	
	// 법인 카드 등록 
	public void insertCorCard(CorcardVO card);
	
	// 법인 카드 상세 조회 
	public CorcardVO selectCorcardById(CorcardVO corcardVO);
	
	public int updateReportTotalAmounts(ReportVO reportVO);
	
	
}
