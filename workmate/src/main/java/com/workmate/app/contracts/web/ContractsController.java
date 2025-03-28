package com.workmate.app.contracts.web;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
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
 * 03-27	이종우	계약폼 불러오기
 * 
 * 
 * </pre>
 */

@Controller
@RequiredArgsConstructor
public class ContractsController {

	private final ContractsService contractsService;
	private final ApprFormService apprFormService; 
	private final EmpService empService;
	private final WhoAmI whoAmI;

	/**
	 * 전자계약 폼 불러옴
	 * @param model
	 * @param approvalVO, contractsVO
	 * @param standard
	 * @return 전자계약 페이지
	 */
	
	// 결재 신청하려 할때 결재 양식 목록 불러옴
	@GetMapping("contracts/form")
	public String getFormList(Model model) {
		model.addAttribute("formList", contractsService.findFormList());
		return "contracts/contractsForm";
	}
	
	// 전자계약 조회
	@GetMapping("contracts/main")
	public String contractsList(Model model) {
		List<ContractsVO> list = contractsService.findContractsList();
		model.addAttribute("contr", list);
		return "contracts/contractsList";
	}

	
	// 전자계약 템플릿 양식 불러오기
	 @GetMapping("contracts/forms/{type}")
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
	
	 
	
}
