package com.workmate.app.contracts.web;

import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.workmate.app.approval.service.ApprFormService;
import com.workmate.app.common.WhoAmI;
import com.workmate.app.contracts.service.ContractsService;
import com.workmate.app.contracts.service.ContractsVO;
import com.workmate.app.employee.service.EmpService;

import lombok.RequiredArgsConstructor;

/**
 * 전자계약 페이지
 * 
 * @author 이종우
 * @since 2025-03-28
 * 
 * <pre>
 * 수정일자	수정자	수정내용
 * ----------------------
 * 03-28	이종우	전자계약 목록
 * 03-30	이종우	전자계약 등록, 조회
 * 04-01	이종우	전자계약 상세페이지
 * 
 * </pre>
 */

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
	 * 
	 * @param contrNo
	 * @return
	 */
	@GetMapping("/view/{contrNo}")
	public String viewContractPage(@PathVariable String contrNo) {
	    switch (contrNo) {
	        case "standard":
	            return "forms/contracts/Standard_Contract_Form"; // 전체 페이지
	        case "trade":
	            return "forms/contracts/Trade_Contract_Form";    // 전체 페이지
	        default:
	            return "error/404";
	    }
	}
	/**
	 * 
	 * @param contract
	 * @param model
	 * @return
	 */
	@PostMapping("/submit")
	public String submitContract(@ModelAttribute ContractsVO contract, Model model) {

	    // Base64 서명 이미지 디코딩 처리
	    if (contract.getSignImageBase64() != null && contract.getSignImageBase64().startsWith("data:image")) {
	        try {
	            String base64 = contract.getSignImageBase64().split(",")[1];
	            byte[] decodedImage = Base64.getDecoder().decode(base64);
	            contract.setSignImage(decodedImage);
	        } catch (IllegalArgumentException e) {
	            model.addAttribute("error", "서명 이미지 처리 중 오류가 발생했습니다.");
	            return "error/500";
	        }
	    }
	    if (contract.getSignImage() != null) {
	        String base64Image = Base64.getEncoder().encodeToString(contract.getSignImage());
	        contract.setSignImageBase64(base64Image); // 뷰용 필드에 주입
	    }
	    model.addAttribute("contr", contract);

	    // 계약 등록 처리
	    int result = contractsService.inputContracts(contract);

	    if (result > 0) {
	        return "redirect:/contracts/main"; // 등록 후 목록으로 이동
	    } else {
	        model.addAttribute("error", "계약서 등록에 실패했습니다.");
	        return "error/500";
	    }
	}

	/**
	 * 전자계약한 목록들 조회
	 * @param model
	 * @return
	 */
	@GetMapping("/main")
	public String contractsList(Model model) {
		List<ContractsVO> list = contractsService.findContractsList();
		model.addAttribute("contr", list);
		return "contracts/contractsList";
	}
	
	// 표준근로계약서 상세
    @GetMapping("/Standard_Detail_Form/{contrNo}")
    public String standardDetail(ContractsVO contractsVO, Model model) {
        ContractsVO contract = contractsService.findContractsById(contractsVO);
        model.addAttribute("contr", contract);
        return "contracts/Standard_Detail_Form";
    }

    // 거래처계약서 상세
    @GetMapping("/Trade_Detail_Form/{contrNo}")
    public String tradeDetail(ContractsVO contractsVO, Model model) {
        ContractsVO contract = contractsService.findContractsById(contractsVO);
        model.addAttribute("contr", contract);
        return "contracts/Trade_Detail_Form";
    }
	
}
