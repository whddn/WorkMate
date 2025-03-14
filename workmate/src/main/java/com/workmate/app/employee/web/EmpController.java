package com.workmate.app.employee.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.workmate.app.employee.service.EmpService;
import com.workmate.app.employee.service.EmpVO;

@Controller
public class EmpController {
	private EmpService empService;
	
	@Autowired
	public EmpController(EmpService empService) {
		this.empService = empService;
	}

		// 1) 조직도 페이지 (페이지 불러냄)
	@GetMapping("emp/organ") // 조직도 
	public String empOrganPage(EmpVO empVO, Model model) {
		// 2) 서비스 
		EmpVO findVO = new EmpVO();
		model.addAttribute("oneinfo", findVO);
		model.addAttribute("names", empService.empNameList()); // 부서명 
		return "employees/empOrgan";
	}
	
	@GetMapping("emp/Organ/{userNo}") // 조직도 AJAX 단건 조회
	public ResponseEntity<EmpVO> selectOne(@PathVariable int userNo, EmpVO empVO){
		empVO.setUserNo(userNo);
		EmpVO userVO = empService.findEmpByEmpNo(empVO);
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json;charset=UTF-8");
		return new ResponseEntity<>(userVO, header, HttpStatus.OK); 
	}
	
	
	@GetMapping("emp/evalu") // 평가 
	public String empEvaluPage(Model model) {
		return "employees/evalumain";
	}
	
	@GetMapping("emp/newemp") // 사원 등록 페이지 
	public String empNewInsertPage(Model model) {
		model.addAttribute("teams", empService.selectAllTeam()); // 팀명 
		model.addAttribute("rank", empService.selectAllPosition()); // 직급명
		return "employees/newEmp";
	}
	
	@PostMapping("emp/newemp") // 사원등록
	public String empNew(@RequestBody EmpVO empVO) {
		empService.inputNewEmp(empVO);
		return "employees/newEmp";
		 //return "redirect:/employees/empOrgan"; // 등록 후 조직도로 반환
	}
	
}
