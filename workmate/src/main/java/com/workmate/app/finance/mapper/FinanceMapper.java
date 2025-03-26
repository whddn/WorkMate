package com.workmate.app.finance.mapper;

import java.util.List;

import com.workmate.app.finance.service.ReportVO;

public interface FinanceMapper {
	// 보고서 전체 조회
	public List<ReportVO> selectReportList(ReportVO financeVO);
	
	// 입출금 거래 내역 보고서 하나에 대한 전체 입/출금 리스트 조회
	public List<ReportVO> selectTransList(ReportVO financeVO);
	
	
	// 입출금 보고서 작성 페이지
	public String selectReportInsertPage(ReportVO financeVO);
	
	// 법인 카드 전체 조회 (관리자만 가능)
	public List<ReportVO> selectCorCardList(ReportVO financeVO);
	
	// 법인 카드 등록 
	public int insertCorCardInfo(ReportVO financeVO);
	
	// 법인 카드 상세 조회 
	public List<ReportVO> selectCorCardById(ReportVO financeVO);
}
