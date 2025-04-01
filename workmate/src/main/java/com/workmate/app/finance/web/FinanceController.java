package com.workmate.app.finance.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.workmate.app.finance.security.AES256Util;
import com.workmate.app.finance.service.CorcardVO;
import com.workmate.app.finance.service.FinanceService;
import com.workmate.app.finance.service.ReportVO;
import com.workmate.app.security.service.LoginUserVO;

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
	public String ReportInsertPage(ReportVO reportVO, @AuthenticationPrincipal LoginUserVO loginUser, Model model) {
		int userNo = loginUser.getUserVO().getUserNo();
		String userName = loginUser.getUserVO().getUserName();
		String teamName = loginUser.getUserVO().getTeamName();
		String teamNo = loginUser.getUserVO().getTeamNo();
		String userPosition = loginUser.getUserVO().getUserPosition();
		
		model.addAttribute("userNo", userNo);
		model.addAttribute("userName", userName);
		model.addAttribute("teamName", teamName);
		model.addAttribute("teamNo", teamNo);
		model.addAttribute("position", userPosition);

		
		return "finance/reportInsert";
	}
	
	// 리포트 등록 AJAX 
	@PostMapping("finance/reportInsert")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> ReportInsertAjax(@RequestBody ReportVO reportVO) {
	    financeService.inputReportPage(reportVO);

	    Map<String, Object> result = new HashMap<>();
	    result.put("success", true);

	    return ResponseEntity.ok(result); // JSON 리턴!
	}
	
	// 리포트 수정 페이지
	@GetMapping("finance/reportUpdate/{reportNo}")
	public String ReportUpdatePage(ReportVO reportVO, @PathVariable int reportNo,
								   Model model, @AuthenticationPrincipal LoginUserVO loginUser) {
		
		List<ReportVO> reportList = financeService.findTransList(reportVO); // 리포트 조회 쿼리문
		
	    System.out.println("📌 수정된 리포트 제목: " + reportVO.getReportTitle());
		int userNo = loginUser.getUserVO().getUserNo();
		String userName = loginUser.getUserVO().getUserName();
		String teamName = loginUser.getUserVO().getTeamName();
		String teamNo = loginUser.getUserVO().getTeamNo();
		String userPosition = loginUser.getUserVO().getUserPosition();
		

		model.addAttribute("report", reportList);
		model.addAttribute("userNo", userNo);
		model.addAttribute("userName", userName);
		model.addAttribute("teamName", teamName);
		model.addAttribute("teamNo", teamNo);
		model.addAttribute("position", userPosition);
		
		return "finance/reportUpdate";
	}
	
	// 리포트 수정 AJAX
	@PutMapping("finance/reportUpdate/{reportNo}")
	@ResponseBody
	public ResponseEntity <Map<String, Object>> ReportUpdateAjax(@RequestBody  ReportVO reportVO) {
		financeService.modifyReportPage(reportVO); 	// 수정 쿼리문
		 Map<String, Object> result = new HashMap<>();
		    result.put("success", true);
		    return ResponseEntity.ok(result); // JSON 리턴!
	}
	
	
	// 법인카드 전체 조회 페이지 
	@GetMapping("finance/corcardList")
	public String CorcardListPage(CorcardVO corcardVO, Model model) {
		model.addAttribute("card", financeService.findCorcardList(corcardVO));
		return "finance/corcard"; 
	}
	
	
	// 법인카드 등록
	@GetMapping("finance/newCard")
	public String CorcardInsertPage() {
		return "finance/newCard"; 
	}
	
		 
	 // 법인카드 등록
	@PostMapping("finance/newCard")
	public ResponseEntity<?> register(@RequestBody CorcardVO card, @AuthenticationPrincipal LoginUserVO loginUser) {
			int userNo = loginUser.getUserVO().getUserNo();
		    card.setUserNo(userNo);
		    try {
		        // 카드번호에서 하이픈 제거
		        String rawCardNum = card.getCorcardNum().replaceAll("-", "");
		        // 암호화 수행
		        String encryptedCardNum = AES256Util.encrypt(rawCardNum);
		        // VO에 암호화된 값 다시 세팅
		        card.setCorcardNum(encryptedCardNum);

		        financeService.inputCorCard(card);

		        return ResponseEntity.ok("법인카드 등록 완료");
		    } catch (Exception e) {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body("등록 실패: " + e.getMessage());
		    }
	}
	
	// 법인 카드 상세
	@GetMapping("finance/cardDetail/{corcardNo}")
	public String selectCorcardById(CorcardVO corcardVO, @PathVariable int corcardNo, Model model) {
		corcardVO.setCorcardNo(corcardNo);
		model.addAttribute("card", financeService.findCorcardById(corcardVO));
		return "finance/corcardById";
	}
}
