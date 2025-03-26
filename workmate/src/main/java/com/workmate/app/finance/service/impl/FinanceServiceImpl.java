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
	
	
	// 입출금 리포트 등록
	@Override
	public String findReportInsertPage(ReportVO reportVO) {
		return financeMapper.selectReportInsertPage(reportVO);
	}

	// 입출금 리포트 - 한 보고서에 대한 모든 입출금 리스트
	@Override
	public List<ReportVO> findTransList(ReportVO reportVO) {
		return financeMapper.selectTransList(reportVO);
	}
	
}
