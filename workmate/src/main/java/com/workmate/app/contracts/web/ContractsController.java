package com.workmate.app.contracts.web;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.workmate.app.common.WhoAmI;
import com.workmate.app.contracts.service.ContractsService;
import com.workmate.app.contracts.service.ContractsVO;
import com.workmate.app.employee.service.EmpService;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.security.service.LoginUserVO;

import lombok.RequiredArgsConstructor;

/**
 * 전자 계약 페이지
 * 
 * @author 이종우
 * @since 2025-03-20
 * 
 * <pre>
 * 수정일자	수정자	수정내용
 * -------------------------
 * 03-20	이종우	계약페이지 생성
 * 
 * 
 * 
 * </pre>
 */

@Controller
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractsController {

	private final ContractsService contractsService;
	private final EmpService empService;
	private final WhoAmI whoAmI = new WhoAmI();

	// 전자계약 조회
	@GetMapping("/main")
	public String contractsList(Model model) {
		List<ContractsVO> list = contractsService.findContractsList();
		model.addAttribute("contr", list);
		return "contracts/contractsList";
	}

	// 전자계약 상세 조회

	// 전자계약 템플릿
	@GetMapping("/template")
	public String contractsTemplate(Model model) {
		List<ContractsVO> list = contractsService.findContractsList();
		model.addAttribute("contr", list);
		return "contracts/contractsTemplate";
	}
	
	// 전자계약 템플릿 양식 불러오기
	 @GetMapping("/forms/{type}")
	    public String loadContractForm(@PathVariable String type) {
	        switch (type) {
	            case "standard":
	                return "forms/contracts/Standard_Contract_Form";
	            case "trade":
	                return "forms/contracts/Trade_Contract_Form";
	            case "electronic":
	                return "forms/contracts/Electronic_Contract_Form";
	            default:
	                return "error/404"; // 예외 처리도 가능
	        }
	    }
	
	 
	 // 테스트 
	 @GetMapping("/test-view")
	 public String testView() {
	     return "forms/contracts/Trade_Contract_Form"; // 바로 뷰 리턴
	 }
	
}
