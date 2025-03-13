package com.workmate.app.employee.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.workmate.app.employee.service.EmpService;
import com.workmate.app.employee.service.EmpVO;

@Controller
public class EmpController {
	private EmpService empService;
	
	@Autowired
	public EmpController(EmpService empService) {
		this.empService = empService;
	}
	
	
		// 1) 단건조회 : URL + Method
	@GetMapping("emp/organ") // 조직도 
	public String empOrganPage(EmpVO empVO, Model model) {
		// 2) 서비스 
		EmpVO findVO = empService.findEmpByEmpNo(empVO);
		model.addAttribute("oneinfo", findVO);
		return "employees/empOrgan";
	}
	
	@GetMapping("emp/evalu") // 평가 
	public String empEvaluPage(Model model) {
		return "employees/evalumain";
	}
	
	@GetMapping("emp/newemp") // 사원 등록 페이지 
	public String empNewInsertPage(Model model) {
		model.addAttribute("teams", empService.selectAllTeam());
		model.addAttribute("rank", empService.selectAllPosition());
		return "employees/newEmp";
	}
	
	@PutMapping("emp/newemp") // 사원등록
	public String empNew(Model model) {
		return "employees/newEmp";
	}
	
}
