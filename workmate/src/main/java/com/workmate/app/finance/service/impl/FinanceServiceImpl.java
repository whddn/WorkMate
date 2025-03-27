package com.workmate.app.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.finance.mapper.FinanceMapper;
import com.workmate.app.finance.service.FinanceService;
import com.workmate.app.finance.service.ReportVO;

@Service
public class FinanceServiceImpl implements FinanceService {
	private FinanceMapper financeMapper;

	@Autowired
	public FinanceServiceImpl(FinanceMapper financeMapper) {
		this.financeMapper = financeMapper;
	}
	
	// 입출금 리포트 조회
	@Override
	public List<ReportVO> findReportList(ReportVO reportVO) {
		return financeMapper.selectReportList(reportVO);
	}
	


	// 입출금 리포트 - 한 보고서에 대한 모든 입출금 리스트
	@Override
	public List<ReportVO> findTransList(ReportVO reportVO) {
		return financeMapper.selectTransList(reportVO);
	}
	
	// 입출금 리포트 등록 
	@Override
	public int inputReportPage(ReportVO reportVO) {
	    int result = 0;

	    // 1. 리포트 등록
	    int reportInsert = financeMapper.insertReportOne(reportVO);

	    if (reportInsert > 0) {
	    	Integer reportNo = reportVO.getReportNo();
	        // 2. 거래 내역 리스트 insert
	    	List<ReportVO> transList = reportVO.getTransHistoryList();
	    	 if (transList != null && !transList.isEmpty()) {
	             for (ReportVO trans : transList) {
	                 // 각각의 거래내역에 reportNo 세팅
	                 trans.setReportNo(reportNo);
	                 // 거래내역 insert (trans_history 테이블)
	                 result += financeMapper.insertReportTransOne(trans);
	             }
	         }    
	    }

	    return result;
	}
	
}
