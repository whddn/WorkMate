package com.workmate.app.finance.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
	
	// 입출금 보고서 등록
	@PostMapping("finance/reportInsert")
	public ResponseEntity<ReportVO> ReportInsertAjax(ReportVO reportVO) {
		financeService.inputReportPage(reportVO);
		return ResponseEntity.ok().build();
	}
	
	// 엑셀 파일 업로드 
	@PostMapping("finance/fileUpload")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> uploadExcel(@RequestParam("file") MultipartFile file) {
		List<Map<String, Object>> result = new ArrayList<>();
		
		try (InputStream is = file.getInputStream();
				Workbook workbook = new XSSFWorkbook(is)) {
			
			Sheet sheet = workbook.getSheetAt(0); // 첫번째 시트
			
			for (int i = 1; i <= sheet.getLastRowNum(); i ++) {
				Row row = sheet.getRow(i);
				if(row == null) continue;
				
				Map<String, Object> rowData = new HashMap<>();
				//rowData.put("date", getCellValue(row.getCell(0)));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// 엑셀 셀의 값 받아오기
	//public String getCellValue(Cell cell) {

	//}
	
	
	// 법인카드 전체 조회 페이지 
	@GetMapping("finance/corcardList")
	public String CorcardListPage(ReportVO reportVO) {
		return "finance/corcard"; 
	}
}
