package com.workmate.app.vendor.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.workmate.app.common.FileHandler;
import com.workmate.app.common.WhoAmI;
import com.workmate.app.security.service.LoginUserVO;
import com.workmate.app.vendor.service.VendorService;
import com.workmate.app.vendor.service.VendorVO;
import lombok.RequiredArgsConstructor;

/**
 * 거래처 페이지
 * 
 * @author 이종우
 * @since 2025-03-26
 * 
 *        <pre>
 * <pre>
 * 수정일자	수정자	수정내용
 * ----------------------
 * 03-26	이종우	거래처 페이지 생성
 * 03-27	이종우	거래처 상세, 등록, 수정 구현
 * 
 * </pre>
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/vendor")
public class VendorController {

	private final VendorService vendorService;
	private final WhoAmI whoAmI;
	private final FileHandler fileHandler;

	@Value("${file.upload-dir}")
	private String uploadDir;

	private String subDir = "vendor/";

	// 거래처 전체 조회 - 페이지
	@GetMapping("/list")
	public String vendorList(Model model) {
		List<VendorVO> list = vendorService.findVendorList(new VendorVO());
		model.addAttribute("vendorList", list);
		return "vendor/vendorList";
	}
	
	// 거래처 상세보기(단건 조회) - 페이지
	@GetMapping("/detail/{vendCode}")
	public String vendorDetail(@PathVariable String vendCode, Model model) {
	    VendorVO vo = new VendorVO();
	    vo.setVendCode(vendCode);

	    VendorVO vendor = vendorService.findVendorById(vo);
	    model.addAttribute("vendor", vendor);

	    return "vendor/vendorDetail"; // 상세 페이지 이름
	}
	
	// 파일 다운로드
	@GetMapping("/files/{vendCode}")
	public ResponseEntity<FileSystemResource> downloadFile(@PathVariable String vendCode) {
	    try {
	    	
	    	VendorVO vo = new VendorVO();
		    vo.setVendCode(vendCode);
	    	VendorVO vendor = vendorService.findVendorById(vo);
	    	
	        String fullPath = uploadDir + subDir; // 예: C:/uploads/vendor/
	        FileSystemResource resource = fileHandler.fileDownload(vendor.getCntrAttachment() , fullPath);

	        String encodedFileName = URLEncoder.encode(vendor.getCntrFile() , StandardCharsets.UTF_8);
	        		
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
	                .body(resource);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	

	// 거래처 등록 - 페이지
	@GetMapping("/insert")
	public String vendorInsert() {
		return "vendor/vendorInsert";
	}
	// 거래처 등록 - 처리
	@PostMapping("/insert")
	public String vendorInsert(
	        @AuthenticationPrincipal LoginUserVO loginUser,
	        @RequestParam("uploadFile") MultipartFile file,
	        VendorVO vendorVO,
	        Model model) {
	    try {
	        if (file != null && !file.isEmpty()) {
	            String fileName = file.getOriginalFilename();
	            String saveFileName = fileHandler.fileUpload(file, uploadDir + subDir, false);
	            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();

	            vendorVO.setCntrFile(fileName); // 원본 파일 이름
	            vendorVO.setCntrAttachment(saveFileName);
	        }

	        vendorService.inputVendor(vendorVO);

	        // ✅ 등록 성공 시 목록 페이지로 이동
	        return "redirect:/vendor/list";

	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("error", "등록 중 오류 발생");
	        return "vendor/vendorInsert"; // 실패 시 입력 페이지로 다시
	    }
	}


	// 거래처 수정 - 페이지
	@GetMapping("/edit/{vendCode}")
	public String vendorEditForm(VendorVO vendorVO, Model model) {
		VendorVO vendor = vendorService.findVendorById(vendorVO); 
	    model.addAttribute("vendor", vendor);
	    return "vendor/vendorEdit";
	}
	// 거래처 수정 - 처리
	@PostMapping("/update")
	public String updateVendor(
	        VendorVO vendorVO,
	        @RequestParam(value = "uploadFile", required = false) MultipartFile file
	) {
	    try {
	        if (file != null && !file.isEmpty()) {
	            // 새 파일 업로드 처리
	            String fileName = file.getOriginalFilename();
	            String saveFileName = fileHandler.fileUpload(file, uploadDir + subDir, false);
	            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();

	            // 파일 관련 정보 설정
	            vendorVO.setCntrFile(fileName);
	            vendorVO.setCntrAttachment(saveFileName);
	        }

	        vendorService.modifyVendor(vendorVO);
	        return "redirect:/vendor/detail/" + vendorVO.getVendCode();

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/vendor/edit/" + vendorVO.getVendCode() + "?error=true";
	    }
	}
	

	
	
}
