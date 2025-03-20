package com.workmate.app.contracts.web;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.workmate.app.contracts.service.ContractsService;
import com.workmate.app.contracts.service.ContractsVO;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.security.service.LoginUserVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ContractsController {

	private final ContractsService contractsService;

	// 현재 로그인한 사람의 개인정보를 empVO로 불러온다.
	public EmpVO whoAmI() {
		LoginUserVO loginUserVO = (LoginUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmpVO empVO = new EmpVO();

		empVO.setUserNo(loginUserVO.getUserVO().getUserNo());
//		empVO = empService.findEmpByEmpNo(empVO);

		return empVO;
	}

	// 전자계약 조회
	@GetMapping("Contracts/main")
	public String contractsList(Model model) {
		List<ContractsVO> list = contractsService.findContractsList();
		model.addAttribute("contr", list);
		return "contracts/contractsList";
	}

	// 전자계약 상세 조회

	// 전자계약 등록 페이지 요청 (GET)
	@GetMapping("Contracts/insert")
	public String contractsInsert() {
		return "contracts/contracts";
	}

	// 전자계약 처리
	@PostMapping("Contracts/insert")
    public String contractsInput(ContractsVO contractsVO){
    	int contrNo = contractsService.inputContracts(contractsVO);
    	String url = null;
    	if(contrNo > -1) {
			//정상적으로 등록된 경우
			url = "redirect:contractsInfo?contrNo="+contrNo;
		}else {
			//등록되지 않은 경우
			url = "redirect:Contracts/main";
		}
		return url;
	}
       
    
    

}
