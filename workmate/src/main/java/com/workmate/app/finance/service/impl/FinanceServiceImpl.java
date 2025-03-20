package com.workmate.app.finance.service.impl;

import org.springframework.stereotype.Service;

import com.workmate.app.finance.mapper.FinanceMapper;
import com.workmate.app.finance.service.FinanceService;
import com.workmate.app.finance.service.FinanceVO;

@Service
public class FinanceServiceImpl implements FinanceService {
	private FinanceMapper financeMapper;
	
	
	// 입출금 리포트 조회
	@Override
	public FinanceVO findReportList(FinanceVO financeVO) {
		return financeMapper.selectReportList(financeVO);
	}

}
