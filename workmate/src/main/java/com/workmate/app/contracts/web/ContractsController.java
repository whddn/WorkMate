package com.workmate.app.contracts.web;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.workmate.app.approval.service.ApprFormService;
import com.workmate.app.common.WhoAmI;
import com.workmate.app.contracts.service.ContractsService;
import com.workmate.app.contracts.service.ContractsVO;
import com.workmate.app.employee.service.EmpService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/contracts")
public class ContractsController {

	private final ContractsService contractsService;
	private final ApprFormService apprFormService; 
	private final EmpService empService;
	private final WhoAmI whoAmI;

	/**
	 * 전자계약 양식 리스트 페이지 (표 테이블로 나옴)
	 */
	@GetMapping("/form")
	public String getFormList(Model model) {
		model.addAttribute("formList", contractsService.findContractsList());
		return "contracts/contractsForm"; // 리스트 페이지
	}

	/**
	 * ✅ Ajax로 불러올 모달 HTML 조각 반환
	 */
	@GetMapping("/modal/{contrNo}")
	public String loadModal(@PathVariable String contrNo) {
	    switch (contrNo) {
	        case "standard": return "forms/contracts/Standard_Contract_Form";
	        case "trade": return "forms/contracts/Trade_Contract_Form";
	        default: return "error/404";
	    }
	}
	

	/**
	 * 전자계약 메인 리스트
	 */
	@GetMapping("/main")
	public String contractsList(Model model) {
		List<ContractsVO> list = contractsService.findContractsList();
		model.addAttribute("contr", list);
		return "contracts/contractsList";
	}
}
