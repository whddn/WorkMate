package com.workmate.app.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.finance.mapper.FinanceMapper;
import com.workmate.app.finance.service.FinanceService;
import com.workmate.app.finance.service.FinanceVO;

@Service
public class FinanceServiceImpl implements FinanceService {
	private FinanceMapper financeMapper;

	@Autowired
	public FinanceServiceImpl(FinanceMapper financeMapper) {
		this.financeMapper = financeMapper;
	}
	
	// 입출금 리포트 조회
	@Override
	public List<FinanceVO> findReportList() {
		return financeMapper.selectReportList();
	}
	
	// 입출금 리포트 상세 조회
//	@Override
//	public List<FinanceVO> findReportById(FinanceVO financeVO) {
//		return financeMapper.selectReportById(FinanceVO financeVO);
//	}

}
