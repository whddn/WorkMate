package com.workmate.app.finance.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.workmate.app.finance.service.FinanceService;
import com.workmate.app.finance.service.ReportVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FinanceController {
	private final FinanceService financeService;
	
	// 입출금 보고서 메인 
	@GetMapping("finance/report") 
	public String ReportMainList(ReportVO reportVO, Model model) {
		// 2) 서비스 
		model.addAttribute("report", financeService.findReportList(reportVO)); 
		return "finance/financeMain";
	}
	
	// 입출금 보고서 단건 조회
	@GetMapping("finance/reportInfo/{reportNo}")
	public String ReportOneInfo(ReportVO reportVO, Model model, @PathVariable int reportNo) {
		reportVO.setReportNo(reportNo); 
		// 입/출금 쿼리문 
	    List<ReportVO> transList = financeService.findTransList(reportVO); 
	    model.addAttribute("trans", transList);
	   
	    return "finance/reportInfo"; 
	}
	
	// 입출금 보고서 등록 화면 페이지 
	@GetMapping("finance/reportInsert")
	public String ReportInsertPage(ReportVO reportVO) {
		return "finance/reportInsert";
	}
	
	// 법인카드 전체 조회 페이지 
	@GetMapping("finance/corcardList")
	public String CorcardListPage(ReportVO reportVO) {
		return "finance/corcard"; 
	}
}
