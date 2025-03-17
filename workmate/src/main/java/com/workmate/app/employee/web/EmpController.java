package com.workmate.app.employee.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.workmate.app.employee.service.EmpService;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.employee.service.EvaluVO;

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
	
	// 조직도 단건 조회
	@GetMapping("emp/Organ/{userNo}") // 조직도 AJAX 단건 조회
	public ResponseEntity<EmpVO> selectOne(@PathVariable int userNo, EmpVO empVO){
		empVO.setUserNo(userNo);
		EmpVO userVO = empService.findEmpByEmpNo(empVO);
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json;charset=UTF-8");
		return new ResponseEntity<>(userVO, header, HttpStatus.OK); 
	}
	
	// 평가 양식 페이지
	@GetMapping("emp/evalu") 
	public String empEvaluPage(EvaluVO evaluVO, Model model) {
		List<EvaluVO> findEvalu = empService.findOneEvaluPage(evaluVO);
		model.addAttribute("evalu", findEvalu);
		return "employees/evalumain";
	}
	
	@GetMapping("emp/bfevalu") // 지난 평가 리스트 전체 조회 (관리자)
	public String beforeEvaluListPage(EvaluVO evaluVO, Model model) {
		List<EvaluVO> findAllEvalu = empService.findBeforeEvaluList(evaluVO);
		model.addAttribute("list", findAllEvalu);
		return "employees/evalulist";
	}
	
	// 지난 평가 리스트 중 단건 조회 (관리자)
	@GetMapping("emp/bfoneevalu")
	//@GetMapping("emp/bfoneevalu/${evaluFormNo}")
	public String beforeEvaluOneResultPage(@PathVariable int evaluFormNo, EvaluVO evaluVO, Model model) {
		evaluVO.setEvaluFormNo(evaluFormNo);
		List<EvaluVO> findOneEvaluResult = empService.findBeforeEvaluOne(evaluVO);
		model.addAttribute("one", findOneEvaluResult);
		return "employees/beforeEvaluOneInList"; // 페이지 이동 방식 @PathVariable : 한 건만 넘길 때 / @requestParam : 여러 건 넘길 때
	}
	
	// 내 평가 관리 (일반 사용자)
	@GetMapping("emp/myEvalu")
	public String myEvaluListPage(EvaluVO evaluVO) {
		return "employees/myEvalu";
	}
	
	// 내 평가 단건 조회
	@GetMapping("emp/result")
	public String evaluResultOnePage(EvaluVO evaluVO, Model model) {
		List<EvaluVO> findResult = empService.findOneEvaluResult(evaluVO);
		model.addAttribute("result", findResult);
		return "employees/evaluResult";
	}
	
	// 평가 등록 (관리자) 
	@GetMapping("emp/neweva")
	public String evaluNewOneInsert(EvaluVO evaluVO) {
		return "employees/newEvalu";
	}
	
	// 사원 등록 페이지 
	@GetMapping("emp/newemp") 
	public String empNewInsertPage(Model model) {
		model.addAttribute("teams", empService.selectAllTeam()); // 팀명 
		model.addAttribute("rank", empService.selectAllPosition()); // 직급명
		return "employees/newEmp";
	}
	
	 // 사원등록
	@PostMapping("emp/newemp")
	public String empNew(@RequestBody EmpVO empVO) {
		empService.inputNewEmp(empVO);
		//return "employees/newEmp";
		 return "redirect:organ"; // 등록 후 조직도로 반환, 페이지 이동
	}
	
	@GetMapping("emp/update") // 조직도 수정 페이지 
	public String empUpdatePage(EmpVO empVO, Model model) {
		// 2) 서비스 
		EmpVO findVO = new EmpVO();
		model.addAttribute("update", findVO);
		model.addAttribute("teams", empService.selectAllTeam()); // 팀명 
		model.addAttribute("names", empService.empNameList()); // 부서명 
		model.addAttribute("rank", empService.selectAllPosition()); // 직급명
		return "redirect:update"; // redirect 링크에는 context-path도 포함되어야 한다. 공통의 url이라면 생략 가능
		// redirect:/workmate/emp/update
	}
	
	// 조직도 수정 AJAX 단건 조회
	@GetMapping("emp/update/{userNo}") 
	public ResponseEntity<EmpVO> selectUpdate(@PathVariable int userNo, EmpVO empVO){
		empVO.setUserNo(userNo);
		EmpVO userVO = empService.findEmpByEmpNo(empVO);
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json;charset=UTF-8");
		return new ResponseEntity<>(userVO, header, HttpStatus.OK);  // AJAX 방식 
	}
	
	// 수정 기능 
	@PutMapping("emp/update/{userNo}")
	public ResponseEntity<EmpVO> empUpdate(@RequestBody EmpVO empVO, @PathVariable int userNo) {
	    empVO.setUserNo(userNo); // URL에서 받은 userNo를 VO에 설정
	    empService.updateEmp(empVO); // 서비스에서 수정 처리
	    EmpVO updatedEmp = empService.findEmpByEmpNo(empVO); // 수정된 사원 조회
	    return ResponseEntity.ok(updatedEmp); // 수정된 사원 정보 반환
	}
	



	
}
