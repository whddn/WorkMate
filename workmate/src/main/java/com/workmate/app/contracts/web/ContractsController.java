package com.workmate.app.contracts.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.workmate.app.approval.service.ApprFormService;
import com.workmate.app.common.FileHandler;
import com.workmate.app.common.WhoAmI;
import com.workmate.app.contracts.service.ContractsService;
import com.workmate.app.contracts.service.ContractsVO;
import com.workmate.app.employee.service.EmpService;
import com.workmate.app.vendor.service.VendorVO;

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
	private final FileHandler fileHandler;
	
	@Value("${file.upload-dir}")
	private String uploadDir;

	private String subDir = "contr/";

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
	public String submitContract(@RequestParam("uploadFile") MultipartFile file,
	                             @ModelAttribute ContractsVO contract,
	                             Model model) {
	    try {
	        // 파일 업로드 처리
	        if (file != null && !file.isEmpty()) {
	            String fileName = file.getOriginalFilename();
	            String saveFileName = fileHandler.fileUpload(file, uploadDir + subDir, false);
	            contract.setCntrFile(fileName);           // 원본 파일 이름
	            contract.setCntrAttachment(saveFileName); // 저장된 파일 경로
	        }

	        // Base64 서명 이미지 디코딩
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

	        // 이미지 미리보기를 위한 base64 세팅 (뷰용)
	        if (contract.getSignImage() != null) {
	            String base64Image = Base64.getEncoder().encodeToString(contract.getSignImage());
	            contract.setSignImageBase64(base64Image);
	        }

	        // 계약 등록
	        int result = contractsService.inputContracts(contract);

	        if (result > 0) {
	            return "redirect:/contracts/main"; // 성공 시 목록으로 이동
	        } else {
	            model.addAttribute("error", "계약서 등록에 실패했습니다.");
	            return "error/500";
	        }

	    } catch (Exception e) {
	        model.addAttribute("error", "계약서 등록 중 오류가 발생했습니다.");
	        return "error/500";
	    }
	}

	
	// 파일 다운로드
		@GetMapping("/files/{contrNo}")
		public ResponseEntity<FileSystemResource> downloadFile(@PathVariable String contrNo) {
		    try {
		    	
		    	ContractsVO vo = new ContractsVO();
			    vo.setContrNo(contrNo);
			    ContractsVO contr = contractsService.findContractsById(vo);
		    	
		        String fullPath = uploadDir + subDir;
		        FileSystemResource resource = fileHandler.fileDownload(contr.getCntrAttachment() , fullPath);

		        String encodedFileName = URLEncoder.encode(contr.getCntrFile() , StandardCharsets.UTF_8);
		        		
		        return ResponseEntity.ok()
		                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
		                .body(resource);

		    } catch (Exception e) {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
        if (contract.getSignImage() != null) {
            String base64 = Base64.getEncoder().encodeToString(contract.getSignImage());
            contract.setSignImageBase64(base64); // 화면 전달용
        }
        model.addAttribute("contr", contract);
        return "contracts/Standard_Detail_Form";
    }

    // 거래처계약서 상세
    @GetMapping("/Trade_Detail_Form/{contrNo}")
    public String tradeDetail(ContractsVO contractsVO, Model model) {
        ContractsVO contract = contractsService.findContractsById(contractsVO);
        if (contract.getSignImage() != null) {
            String base64 = Base64.getEncoder().encodeToString(contract.getSignImage());
            contract.setSignImageBase64(base64); // 화면 전달용
        }
        model.addAttribute("contr", contract);
        return "contracts/Trade_Detail_Form";
    }
	
}
