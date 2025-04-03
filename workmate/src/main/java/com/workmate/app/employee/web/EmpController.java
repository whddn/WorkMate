package com.workmate.app.employee.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workmate.app.employee.service.EmpService;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.employee.service.EvaluVO;
import com.workmate.app.security.service.LoginUserVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class EmpController {
	private final EmpService empService;

	private final PasswordEncoder passwordEncoder;
	/**
	 * 사원 관리 및 평가
	 * 
	 * @author 박혜원
	 * @since 2025-03-24
	 */

//	@Autowired
//	public EmpController(EmpService empService) {
//		this.empService = empService;
//	}

	/**
	 * 조직도 페이지로 이동
	 * 
	 * @param empVO
	 * @param model
	 * @return organ
	 */
	// 1) 조직도 페이지 (페이지 불러냄)
	@GetMapping("emp/organ") // 조직도
	public String getEmpOrganPage(EmpVO empVO, Model model) {
		// 2) 서비스
		model.addAttribute("oneinfo", empVO);
		model.addAttribute("names", empService.findDeptEmpNameList()); // 부서명
		return "employees/organ";
	}

	// 조직도 수정 페이지 - 사원의 정보 수정 가능한 페이지 (input 활성화)
	@GetMapping("emp/update") // 조직도 수정 페이지
	public String getEmpUpdatePage(EmpVO empVO, Model model) {

		EmpVO userVO = empService.findEmpByEmpNo(empVO);
		// 2) 서비스
		model.addAttribute("update", userVO);
		model.addAttribute("teams", empService.findTeamList()); // 팀명
		model.addAttribute("names", empService.findDeptEmpNameList()); // 부서명
		model.addAttribute("rank", empService.findPositionList()); // 직급명
		return "employees/empUpdate::employeeFrag"; // redirect 링크에는 context-path도 포함되어야 한다. 공통의 url이라면 생략 가능
	}

	// 조직도 수정 페이지 - 수정 완료 후 단건 조회로 이동 (fragment)
	@GetMapping("emp/organInfo")
	public String getEmpUpdateResultPage(EmpVO empVO, Model model) {
		EmpVO userVO = empService.findEmpByEmpNo(empVO);
		model.addAttribute("oneinfo", empVO);
		// 2) 서비스
		model.addAttribute("update", userVO);
		model.addAttribute("teams", empService.findTeamList()); // 팀명
		model.addAttribute("names", empService.findDeptEmpNameList()); // 부서명
		model.addAttribute("rank", empService.findPositionList()); // 직급명
		return "employees/organ::employeeFrag"; // redirect 링크에는 context-path도 포함되어야 한다. 공통의 url이라면 생략 가능
	}

	// 조직도 단건 조회
	@GetMapping("emp/organ/{userNo}") // 조직도 AJAX 단건 조회
	public ResponseEntity<EmpVO> selectOrganEmpById(@PathVariable int userNo, EmpVO empVO) {
		empVO.setUserNo(userNo);
		EmpVO userVO = empService.findEmpByEmpNo(empVO);
		HttpHeaders header = new HttpHeaders();
		return new ResponseEntity<>(userVO, header, HttpStatus.OK);
	}

	// 사원 등록 페이지
	@GetMapping("emp/newemp")
	public String getNewEmpInsertPage(Model model) {
		model.addAttribute("teams", empService.findTeamList()); // 팀명
		model.addAttribute("rank", empService.findPositionList()); // 직급명
		return "employees/newEmp";
	}

	// 사원등록
	@PostMapping("emp/newemp")
	@ResponseBody // AJAX (or ResponseEntity)
	public boolean insertEmpNewEmp(@RequestBody EmpVO empVO) {
		// 사용자가 입력한 비밀번호
		String rawPassword = empVO.getUserPwd();
		// 비밀번호 암호화
		String endocodedPassword = passwordEncoder.encode(rawPassword);
		// 암호화된 비밀번호로 덮어쓰기
		empVO.setUserPwd(endocodedPassword);
		// 서비스로 전달해 DB 저장
		empService.inputNewEmp(empVO);
		return true; // 등록 후 조직도로 반환 (fetch의 location.href), 페이지 이동
	}

	// 조직도 수정 AJAX 단건 조회
	@GetMapping("emp/update/{userNo}")
	public ResponseEntity<EmpVO> selectUpdateResultById(@PathVariable int userNo, EmpVO empVO) {
		empVO.setUserNo(userNo);
		EmpVO userVO = empService.findEmpByEmpNo(empVO);
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json;charset=UTF-8");
		return new ResponseEntity<>(userVO, header, HttpStatus.OK); // AJAX 방식
	}

	// 수정 기능
	@PutMapping("emp/update/{userNo}")
	public ResponseEntity<EmpVO> updateEmpInfo(@RequestBody EmpVO empVO, @PathVariable int userNo) {
		empVO.setUserNo(userNo); // URL에서 받은 userNo를 VO에 설정
		empService.updateEmp(empVO); // 서비스에서 수정 처리
		EmpVO updatedEmp = empService.findEmpByEmpNo(empVO); // 수정된 사원 조회
		return ResponseEntity.ok(updatedEmp); // 수정된 사원 정보 반환
	}

	// 내가 평가해야 할 목록 리스트 (일반 사용자)
	@GetMapping("emp/myEvalu")
	public String getMyEvaluListPage(EvaluVO evaluVO, Model model, @AuthenticationPrincipal LoginUserVO loginUser) {

		// 로그인한 사용자 정보
		if (loginUser == null || loginUser.getUserVO() == null) {
			return "redirect:/login";
		}
		int userNo = loginUser.getUserVO().getUserNo();
		evaluVO.setUserNo(userNo);

		List<EvaluVO> oneEvalu = empService.findMyEvaluList(evaluVO);
		model.addAttribute("evlist", oneEvalu);
		return "evalu/myEvaluList";
	}
	
	// 평가 Evalu 
	
	// 일반 사용자 : 평가 상세 페이지 진입 (평가 진행 전 : evalu.html로 / 진행 후 : result.html)
	@GetMapping("emp/evalu/{formNo}")
	public String getEvaluPage(@PathVariable int formNo, EvaluVO evaluVO, Model model,
			@AuthenticationPrincipal LoginUserVO loginUser) {

		// 1. 로그인 체크
		if (loginUser == null || loginUser.getUserVO() == null) {
			return "redirect:/login";
		}

		// 2. 평가 폼 번호 & 로그인 사용자 번호 설정
		evaluVO.setEvaluFormNo(formNo);
		evaluVO.setUserNo(loginUser.getUserVO().getUserNo());

		// 3. EVALU_GROUP의 usage_status 가져오기
		String usageStatus = Optional.ofNullable(empService.findEvaluStatusById(evaluVO)).orElse("진행 중");

		if ("제출 완료".equals(usageStatus)) {
			// 제출 완료된 경우 : 결과 페이지로 이동
			List<EvaluVO> findResult = empService.findMyEvaluById(evaluVO);
			List<EvaluVO> evalu = empService.findEvaluInfo(evaluVO);
			List<EvaluVO> evaluatee = empService.findEvaluateeInfo(evaluVO);
			List<EvaluVO> fullList = empService.findMyEvaluProcess(evaluVO);

			// 사용자, 항목, 점수 매핑 처리
			Set<Integer> userSet = new HashSet<>();
			List<EvaluVO> userList = new ArrayList<>();
			Set<String> keySet = new HashSet<>();
			List<EvaluVO> evaluList = new ArrayList<>();
			Map<Integer, Map<String, Integer>> userScore = new LinkedHashMap<>();
			
			for (EvaluVO vo : fullList) {
				if (userSet.add(vo.getUserNo()))
					userList.add(vo);
				String key = vo.getEvaluContent() + "|" + vo.getEvaluCompet();
				if (keySet.add(key))
					evaluList.add(vo);
				// 항목과 내용으로 key 설정
				String scoreKey = vo.getEvaluCompet().trim() + " - " + vo.getEvaluContent().trim();
				userScore.computeIfAbsent(vo.getUserNo(), k -> new LinkedHashMap<>()).put(scoreKey, vo.getEvaluScore());
			}

			model.addAttribute("score", userScore);
			model.addAttribute("userList", userList);
			model.addAttribute("evaluList", evaluList);
			model.addAttribute("result", findResult);
			model.addAttribute("evalu", evalu);
			model.addAttribute("evaluatee", evaluatee);
			// 내가 제출한 평가 단건 조회 페이지
			return "evalu/evaluResult";
		} else {
			// 임시 저장 또는 진행 중인 경우 : 작성 페이지로 이동
			List<EvaluVO> fullList = empService.findMyEvaluById(evaluVO);

			Set<Integer> userSet = new HashSet<>();
			List<EvaluVO> userList = new ArrayList<>();
			Set<String> keySet = new HashSet<>();
			List<EvaluVO> evaluList = new ArrayList<>();

			for (EvaluVO vo : fullList) {
				if (userSet.add(vo.getUserNo()))
					userList.add(vo);
				String key = vo.getEvaluContent() + "|" + vo.getEvaluCompet();
				if (keySet.add(key))
					evaluList.add(vo);
			}

			model.addAttribute("userList", userList);
			model.addAttribute("evaluList", evaluList);
			model.addAttribute("usageStatus", usageStatus);

			// 임시 저장 점수 가져오기 (Map<"userNo-itemNo", score>)
			Map<String, Integer> tempScoreMap = empService.findTempEvaluScore(evaluVO);
			model.addAttribute("tempScores", tempScoreMap);

			return "evalu/evalu"; // 작성 + 임시저장 
		}
	}

	// AJAX 결과 (평가 점수 저장, 평가 진행)
	@PostMapping("emp/evalu/{formNo}")
	public ResponseEntity<Map<String, String>> insertEvaluResult(@RequestBody Map<String, Object> data,
			@PathVariable int formNo, @AuthenticationPrincipal LoginUserVO loginUser) {
		Map<String, String> response = new HashMap<>();
		response.put("result", "success");

		String mode = String.valueOf(data.get("mode"));

		ObjectMapper mapper = new ObjectMapper();
		List<EvaluVO> evaList = mapper.convertValue(data.get("scores"), new TypeReference<List<EvaluVO>>() {
		});

		int evaluatorUserNo = loginUser.getUserVO().getUserNo();

		for (EvaluVO vo : evaList) {
			vo.setEvaluFormNo(formNo);
			vo.setUserNo(evaluatorUserNo);
			vo.setResultStatus(mode); // 임시 저장/제출 완료 저장에 꼭 필요!

			String teamNo = vo.getTeamNo();
			if (teamNo != null) {
				String numeric = teamNo.replaceAll("[^0-9]", "");
				if (!numeric.isEmpty()) {
					vo.setEvaluGroupId(numeric);
					vo.setEvaluateeGroupId(numeric);
				}
			}
		}

		empService.inputEvaluResultScore(evaList, mode);

		EvaluVO statusVO = new EvaluVO();
		statusVO.setEvaluFormNo(formNo);
		statusVO.setUserNo(evaluatorUserNo);
		statusVO.setUsageStatus(mode); // "임시 저장" or "제출 완료"
		empService.modifyEvaluStatus(statusVO);

		return ResponseEntity.ok(response);
	}

	// 내가 평가받은 평가 리스트 (일반 사용자)
	@GetMapping("emp/myResult")
	public String getMyEvaluateeResultListPage(EvaluVO evaluVO, Model model,
			@AuthenticationPrincipal LoginUserVO loginUser) {

		// 로그인한 사용자 정보
		if (loginUser == null || loginUser.getUserVO() == null) {
			return "redirect:/login";
		}

		int userNo = loginUser.getUserVO().getUserNo();
		evaluVO.setUserNo(userNo);
		List<EvaluVO> oneEvalu = empService.findMyEvaluResultList(evaluVO);
		model.addAttribute("evlist", oneEvalu);
		return "evalu/myEvaluateeResultList"; // 내가 피평가자로 소속된 평가에 대한 리스트
	}

	// 내가 평가 받은 평가 단건 조회 (일반 사용자)
	@GetMapping("emp/myResult/{formNo}")
	public String getMyEvaluScoreResultByIdPage(EvaluVO evaluVO, Model model, @PathVariable int formNo,
			@AuthenticationPrincipal LoginUserVO loginUser) {
		// 1. 로그인 유저 정보 세팅
		int userNo = loginUser.getUserVO().getUserNo();
		evaluVO.setUserNo(userNo);
		evaluVO.setEvaluFormNo(formNo);
		// 2. 점수 조회 (항목별 평균)
		List<EvaluVO> myEvaluResult = empService.findMyEvaluScoreResultById(evaluVO);
		// 3. view 에 보내기
		model.addAttribute("result", myEvaluResult);
		return "evalu/myEvaluateeResultById";
	}
	
	// 평가 : 관리자 

	// 지난 평가 리스트 전체 조회 (관리자)
		@GetMapping("emp/bfevalu") 
		public String selectBeforeEvaluListPage(EvaluVO evaluVO, Model model) {
			List<EvaluVO> findAllEvalu = empService.findBeforeEvaluList(evaluVO);
			model.addAttribute("list", findAllEvalu);
			return "evalu/adminEvaluBeforeList";
		}

		// 지난 평가 리스트 중 단건 조회 (관리자)
		@GetMapping("emp/bfoneevalu/{formNo}")
		public String selectBeforeEvaluResultById(@PathVariable int formNo, EvaluVO evaluVO, Model model,
		                                         @AuthenticationPrincipal LoginUserVO loginUser) {
		    // 관리자 권한 확인
		    String teamNo = loginUser.getUserVO().getTeamNo();
		    Integer userNo = loginUser.getUserVO().getUserNo();
		    boolean isAdmin = "ROLE_T001".equals(teamNo);
		    
		    System.out.println("팀번호 : [" + teamNo + "]");
		    // 관리자가 아니면 접근 제한
		    if (!isAdmin) {
		        return "redirect:/emp/bfevalu";  // 권한 없음 페이지로 리다이렉트
		    }
		    
		    evaluVO.setEvaluFormNo(formNo);
		    evaluVO.setUserNo(userNo);  // 현재 로그인한 사용자 번호 설정
		    
		    String status = Optional.ofNullable(empService.findEvaluStatus(formNo)).orElse("진행 중");
		    
		    if ("평가 완료".equals(status)) {
		        try {
		            Map<String, Object> result = empService.findAdminEvaluBeforeById(evaluVO);
		            model.addAttribute("people", result.get("people"));
		            model.addAttribute("items", result.get("items"));
		            model.addAttribute("scores", result.get("scores"));
		            return "evalu/adminBeforeEvaluById";
		        } catch (Exception e) {
		            // 예외 처리
		            model.addAttribute("errorMessage", "평가 결과를 불러오는 중 오류가 발생했습니다.");
		            return "error/evaluError";
		        }
		    } else {
		        try {
		            List<EvaluVO> fullList = empService.findInEvaluForm(evaluVO);
		            // 사용자 목록 중복 제거
		            Set<Integer> userSet = new HashSet<>();
		            List<EvaluVO> userList = new ArrayList<>();
		            for (EvaluVO vo : fullList) {
		                if (userSet.add(vo.getUserNo())) {
		                    userList.add(vo);
		                }
		            }

		            // 평가 항목 중복 제거
		            Set<String> keySet = new HashSet<>();
		            List<EvaluVO> evaluList = new ArrayList<>();
		            for (EvaluVO vo : fullList) {
		                String key = vo.getEvaluContent() + "|" + vo.getEvaluCompet();
		                if (keySet.add(key)) {
		                    evaluList.add(vo);
		                }
		            }

		            model.addAttribute("userList", userList);
		            model.addAttribute("evaluList", evaluList);
		            return "evalu/adminEvaluById";
		            
		        } catch (Exception e) {
		            // 예외 처리
		            model.addAttribute("errorMessage", "평가 정보를 불러오는 중 오류가 발생했습니다.");
		            return "error/evaluError";
		        }
		    }
		}
		// 사원 선택시 해당 사원의 결과만 조회하는 페이지 (관리자)
		@PostMapping("/emp/bfoneevalu/{formNo}/{userNo}")
		public String getEvaluResultByUser(@PathVariable int formNo, @PathVariable int userNo, Model model) {
			EvaluVO evaluVO = new EvaluVO();
			evaluVO.setEvaluFormNo(formNo);
			evaluVO.setUserNo(userNo);
			List<EvaluVO> resultList = empService.findAdminEvaluEmpOneById(evaluVO);

			if (!resultList.isEmpty()) {
				model.addAttribute("user", resultList.get(0)); // 유저 정보용
			}
			model.addAttribute("scores", resultList); // 항목별 점수 리스트

			return "evalu/ajaxResult :: resultFragment";
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
			return ResponseEntity.ok(response); // JSON 응답을 반환
		}

	
}
