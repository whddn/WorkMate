package com.workmate.app.employee.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.workmate.app.security.service.LoginUserVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class EmpController {
	private final EmpService empService;
	
//	@Autowired
//	public EmpController(EmpService empService) {
//		this.empService = empService;
//	}

		// 1) 조직도 페이지 (페이지 불러냄)
	@GetMapping("emp/organ") // 조직도 
	public String empOrganPage(EmpVO empVO, Model model) {
		// 2) 서비스 
		model.addAttribute("oneinfo", empVO);
		model.addAttribute("names", empService.findDeptEmpNameList()); // 부서명 
		return "employees/organ";
	}
	
	// 조직도 단건 조회
	@GetMapping("emp/Organ/{userNo}") // 조직도 AJAX 단건 조회
	public ResponseEntity<EmpVO> selectOrganEmpById(@PathVariable int userNo, EmpVO empVO){
		empVO.setUserNo(userNo);
		EmpVO userVO = empService.findEmpByEmpNo(empVO);
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json;charset=UTF-8");
		return new ResponseEntity<>(userVO, header, HttpStatus.OK); 
	}
	
	// 사원 등록 페이지 
	@GetMapping("emp/newemp") 
	public String empNewInsertPage(Model model) {
		model.addAttribute("teams", empService.findTeamList()); // 팀명 
		model.addAttribute("rank", empService.findPositionList()); // 직급명
		return "employees/newEmp";
	}
	
	 // 사원등록
	@PostMapping("emp/newemp")
	public String empNew(@RequestBody EmpVO empVO) {
		empService.inputNewEmp(empVO);
		//return "employees/newEmp";
		 return "redirect:/emp/organ"; // 등록 후 조직도로 반환, 페이지 이동
	}

	// 조직도 수정 페이지 - 수정된 사원 정보 조회
	@GetMapping("emp/update") // 조직도 수정 페이지 
	public String empUpdatePage(EmpVO empVO, Model model) {
		// 2) 서비스 
		EmpVO findVO = new EmpVO();
		model.addAttribute("update", findVO);
		model.addAttribute("teams", empService.findTeamList()); // 팀명 
		model.addAttribute("names", empService.findDeptEmpNameList()); // 부서명 
		model.addAttribute("rank", empService.findPositionList()); // 직급명
		return "employees/empUpdate"; // redirect 링크에는 context-path도 포함되어야 한다. 공통의 url이라면 생략 가능
	    //return "redirect:/workmate/emp/update/" + empVO.getUserNo();
	}
	
	// 조직도 수정 페이지 - 수정된 사원 정보 조회
	@PostMapping("emp/update/{userNo}") // 조직도 수정 페이지 
	public String empUpdatePage(EmpVO empVO, Model model, @PathVariable int userNo) {
		// 2) 서비스 
		empVO.setUserNo(userNo);
		EmpVO findVO = new EmpVO();
		model.addAttribute("update", findVO);
		model.addAttribute("teams", empService.findTeamList()); // 팀명 
		model.addAttribute("names", empService.findDeptEmpNameList()); // 부서명 
		model.addAttribute("rank", empService.findPositionList()); // 직급명
		return "employees/update"; // redirect 링크에는 context-path도 포함되어야 한다. 공통의 url이라면 생략 가능
	    //return "redirect:/workmate/emp/update/" + empVO.getUserNo();
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
	
	
	/////////////////////// 평가 ////////////////////////// 

	// 내 평가 리스트 (일반 사용자)
	@GetMapping("emp/myEvalu")
	public String myEvaluListPage(EvaluVO evaluVO, Model model, @AuthenticationPrincipal LoginUserVO loginUser) {
		
		// 로그인한 사용자 정보 
		if (loginUser == null || loginUser.getUserVO() == null) {
			return "redirect:/login";
		}
		
		int userNo = loginUser.getUserVO().getUserNo();
		evaluVO.setUserNo(userNo);
		
		List<EvaluVO> oneEvalu = empService.findMyEvaluList(evaluVO);
		model.addAttribute("evlist", oneEvalu);
		return "evalu/myEvaluList"; // 나의 평가 조회
	}
	
	// 내 평가 단건 조회
	@GetMapping("emp/result/{evaluFormNo}")
	public String evaluResultOnePage(@PathVariable int evaluFormNo, EvaluVO evaluVO, Model model) {
		evaluVO.setEvaluFormNo(evaluFormNo);
		List<EvaluVO> findResult = empService.findMyEvaluById(evaluVO);
		List<EvaluVO> evalu = empService.findEvaluInfo(evaluVO);
		List<EvaluVO> evaluatee = empService.findEvaluateeInfo(evaluVO);
		model.addAttribute("evalu", evalu); // 평가자 정보 
		model.addAttribute("evaluatee", evaluatee); // 피평가자 정보
		model.addAttribute("result", findResult);
		return "evalu/evaluMyResult";
	}
	
	
//	// 평가하기 페이지
//		@GetMapping("emp/evalu") 
//		public String empEvaluPage(@PathVariable int formNo, EvaluVO evaluVO, Model model) {
////			evaluVO.setEvaluFormNo(formNo);
////			List<EvaluVO> findEvalu = empService.findOneEvaluPage(evaluVO);
////			List<EvaluVO> findOneEvalu = empService.findMyEvaluById(evaluVO);
////			model.addAttribute("evalu", findOneEvalu);
////			// model.addAttribute("evalu", findEvalu);
//			return "employees/evalu";
//		}
		
//		// 평가하기 페이지
//		@PostMapping("emp/myEvalu/{formNo}") 
//		public String myempEvaluOnePage(@PathVariable int formNo, EvaluVO evaluVO, Model model) {
//			evaluVO.setEvaluFormNo(formNo);
//			List<EvaluVO> findEvalu = empService.findOneEvaluPage(evaluVO);
//			List<EvaluVO> findOneEvalu = empService.findMyEvaluById(evaluVO);
//			model.addAttribute("evalu", findOneEvalu);
//			// model.addAttribute("evalu", findEvalu);
//			return "evalu/evalu";
//		}
		
		@GetMapping("emp/bfevalu") // 지난 평가 리스트 전체 조회 (관리자)
		public String selectBeforeEvaluListPage(EvaluVO evaluVO, Model model) {
			List<EvaluVO> findAllEvalu = empService.findBeforeEvaluList(evaluVO);
			model.addAttribute("list", findAllEvalu);
			return "evalu/evaluBeforeList";
		}
		
		// 지난 평가 리스트 중 단건 조회 (관리자)
		//@GetMapping("emp/bfoneevalu")
		@GetMapping("emp/bfoneevalu/{evaluFormNo}")
		public String selectBeforeEvaluResultById(@PathVariable int evaluFormNo, EvaluVO evaluVO, Model model) {
			evaluVO.setEvaluFormNo(evaluFormNo);
			List<EvaluVO> findOneEvaluResult = empService.findBeforeEvaluById(evaluVO);
			List<EvaluVO> evalu = empService.findEvaluInfo(evaluVO);
			List<EvaluVO> evaluatee = empService.findEvaluateeInfo(evaluVO);
			model.addAttribute("evalu", evalu); // 평가자 정보 
			model.addAttribute("evaluatee", evaluatee); // 피평가자 정보
			model.addAttribute("one", findOneEvaluResult);
			model.addAttribute("names", empService.findDeptEmpNameList()); // 부서명 	
			return "evalu/beforeEvaluOneInList"; // 페이지 이동 방식 @PathVariable : 한 건만 넘길 때 / @requestParam : 여러 건 넘길 때
		}
		

		// 평가 등록 페이지 (관리자) 
		@GetMapping("emp/neweva")
		public String evaluNewOneInsert(EvaluVO evaluVO, Model model) {
			model.addAttribute("teams", empService.findTeamList());
			model.addAttribute("content", empService.findEvaluContentList(evaluVO));
			model.addAttribute("names", empService.findDeptEmpNameList()); // 부서명 
			 
			return "evalu/newEvalu";
		}
		
		// 평가 등록 AJAX 
		@PostMapping("emp/neweva")
		public ResponseEntity<Map<String, String>> evaluInsertAJAX(@RequestBody EvaluVO evaluVO) {
		    // 서비스 로직 실행
		    empService.inputNewEvaluForm(evaluVO);
		    // 성공 메시지 또는 처리된 데이터를 JSON 형식으로 반환
		    Map<String, String> response = new HashMap<>();
		    response.put("message", "성공적으로 등록되었습니다.");
		    return ResponseEntity.ok(response);  // JSON 응답을 반환
		}
		

		// 평가 진행 
		@GetMapping("emp/evalu/{evaNo}")
		public String evaluOneProcess(EvaluVO evaluVO, Model model, @AuthenticationPrincipal LoginUserVO loginUser, @PathVariable int evaNo) {
			// 로그인한 사용자 정보 
			if (loginUser == null || loginUser.getUserVO() == null) {
				return "redirect:/login";
			}
			
			int userNo = loginUser.getUserVO().getUserNo();
			evaluVO.setEvaluFormNo(evaNo); 
			evaluVO.setUserNo(userNo);
			List<EvaluVO> oneEvalu = empService.findMyEvaluProcess(evaluVO);
			model.addAttribute("one", oneEvalu);
			return "evalu/evalu";
		}

}
