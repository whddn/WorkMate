package com.workmate.app.finance.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.workmate.app.finance.service.FinanceService;
import com.workmate.app.finance.service.FinanceVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FinanceController {
	private final FinanceService financeService;
	
	// 입출금 보고서 메인 
	@GetMapping("finance/report") 
	public String ReportMainList(FinanceVO financeVO, Model model) {
		// 2) 서비스 
		model.addAttribute("report", financeService.findReportList(financeVO)); 
		return "finance/financeMain";
	}
	
	// 입출금 보고서 단건 조회
	@GetMapping("finance/reportInfo/{reportNo}")
	public String ReportOneInfo(FinanceVO financeVO, Model model, @PathVariable int reportNo) {
		financeVO.setReportNo(reportNo); 
	    List<FinanceVO> oneReport = financeService.findReportById(financeVO); 
	    model.addAttribute("one", oneReport);
	    return "finance/reportInfo"; 
	}
	
	// 입출금 보고서 등록 화면 페이지 
	@GetMapping("finance/reportInsert")
	public String ReportInsertPage(FinanceVO financeVO) {
		return "finance/reportInsert";
	}
}
