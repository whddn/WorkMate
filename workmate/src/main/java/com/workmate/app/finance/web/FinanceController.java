package com.workmate.app.finance.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.workmate.app.finance.service.FinanceService;
import com.workmate.app.finance.service.FinanceVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FinanceController {
	private final FinanceService financeService;
	
	@GetMapping("finance/report")
	public String financeMainPage(FinanceVO financeVO) {
		return "finance/financeMain";
	}
}
