package com.workmate.app.employee.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmpController {
	@GetMapping("organ") // 조직도 
	public String empOrganPage(Model model) {
		return "employees/empOrgan";
	}
	
	@GetMapping("info") // 상세 
	public String empInfoPage(Model model) {
		return "employees/empInfo";
	}
	
	@GetMapping("evalu") // 평가 
	public String empEvaluPage(Model model) {
		return "employees/evalumain";
	}
	
	@GetMapping("newEmp") // 사원 등록
	public String empNewInsertPage(Model model) {
		return "employees/newEmp";
	}
	
}
