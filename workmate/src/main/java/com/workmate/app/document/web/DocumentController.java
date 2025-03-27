package com.workmate.app.document.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import com.workmate.app.document.service.DocVO;
import com.workmate.app.document.service.DocumentService;
import com.workmate.app.security.service.LoginUserVO;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/document")
@Controller
public class DocumentController {
	
	private DocumentService documentService;
	private FileHandler fileHandler;
	
	public DocumentController(DocumentService documentService, FileHandler fileHandler) {
		this.documentService = documentService;
		this.fileHandler = fileHandler;
	}
	
	@Value("${file.upload-dir}")
	private String uploadDir;

	private String subDir ="document/";
	
	//자료실 전체 조회
	@GetMapping("/list")
	public String documentList(Model model, @AuthenticationPrincipal LoginUserVO loginUser) {
		
		String userId = loginUser.getUserVO().getUserId();
		String teamNo = loginUser.getUserVO().getTeamNo();
		
		List<DocVO> list = documentService.findFileList();
		
		// 선언 후 초기화
		List<Map<String, Object>> fileListWithSize = new ArrayList<>();
		
		for (DocVO doc : list) {
			Long fileSize = doc.getFileSize() != null ? doc.getFileSize() : 0L;
	        doc.setFormattedSize(formatSize(fileSize)); // 포맷된 문자 셋팅
        }

		
		model.addAttribute("lists",list);
		model.addAttribute("userId",userId);
		model.addAttribute("teamNo",teamNo);
		
		return "document/list";
	}
	
	//자료실 이력 전체조회
	@GetMapping("/downloadHistory")
	public String downhistoryList(Model model, DocVO docVO) {
				
		List<DocVO> list = documentService.findDownhistory(docVO);
		
		model.addAttribute("lists", list);
		
		return "document/downloadHistory";
		
	}
	
	//파일사이즈
	private String formatSize(long size) {
		  if (size >= 1024 * 1024) {
	            return String.format("%.2f MB", size / (1024.0 * 1024.0));
	        } else if (size >= 1024) {
	            return String.format("%.1f KB", size / 1024.0);
	        } else {
	            return size + " B";
	        }
	}
	
	//자료 업로드	
	@PostMapping("/upload")
	public  ResponseEntity<String> uploadFileProcess(@RequestParam("uploadFile") MultipartFile file,
                                    DocVO docVO,
                                    Model model,
                                    @AuthenticationPrincipal LoginUserVO loginUser) {
		
		String userId = loginUser.getUserVO().getUserId();
		String teamNo = loginUser.getUserVO().getTeamNo();
		
		try {
		
			if(file.isEmpty()) {
				model.addAttribute("message", "파일 업로드 실패");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
			}
			String fileName = file.getOriginalFilename();
			String saveFileName = fileHandler.fileUpload(file, uploadDir + subDir, false);
			String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1).toUpperCase();
			
			docVO.setFileName(fileName);
			docVO.setAttachment(saveFileName); //다운받을 파일이름
			docVO.setFileSize(file.getSize());
			docVO.setExtenstion(fileExtension);
			
			documentService.inputFile(docVO);
			return ResponseEntity.ok("파일이 업로드되었습니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드");
		}		
		
		
	}
	
	//자료 첨부파일 다운로드
	@GetMapping("/download/{documentNo}")
	public ResponseEntity<Resource> downloadDocument(@PathVariable int documentNo,
											@AuthenticationPrincipal LoginUserVO loginUser,
											HttpServletRequest request) 
													throws IOException {
		
		String userId = loginUser.getUserVO().getUserId();
		int userNo = loginUser.getUserVO().getUserNo();
		String teamNo = loginUser.getUserVO().getTeamNo();
		
		System.out.println("요청 들어옴: " + documentNo);
		
		DocVO doc = documentService.findFileById(documentNo); //파일 단건 조회
		
		//저장된 파일 경로			
		String fullPath = Paths.get(uploadDir, subDir, doc.getAttachment()).toString();
	    Path path = Paths.get(fullPath); //파일시스템에서 쓸수있게 변환
	    	
		if (!Files.exists(path)) {
	        return ResponseEntity.notFound().build(); // 파일 없으면 404
	    }
		// 다운로드 이력 저장
		 DocVO log = new DocVO();
		 log.setDocumentNo(documentNo);
		 log.setFileName(doc.getFileName());

		 Integer downUserNo = -1;
		 if (loginUser != null && loginUser.getUserVO() != null && loginUser.getUserVO().getUserNo() != null) {
		     downUserNo = loginUser.getUserVO().getUserNo();
		 }
		 log.setDownUser(downUserNo);

		 log.setDownloadDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
		 log.setDownloadUserIp(request.getRemoteAddr());
		
		 documentService.inputDownhistory(log);
		    
		// 다운로드
		Resource resource = new UrlResource(path.toUri());
		
		String encodedFileName = URLEncoder.encode(doc.getFileName(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
		
		return ResponseEntity.ok()
		        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
		        .body(resource);
		
		
	}
	
	//자료 단건 삭제
	@DeleteMapping("/delete/{documentNo}")
	public ResponseEntity<String> documentDelete(@PathVariable Integer documentNo, DocVO docVO ) {
	    try {
	        if (documentNo == null) {
	            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
	        }	        
	        // 1) 삭제할 파일 정보 단건조회
	        DocVO doc = documentService.findFileById(documentNo);
	        if(doc == null) {
	        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("파일 정보를 찾을 수 없습니다.");
	        }
	        //2) 실제 파일 삭제 시도
	        String fullPath = Paths.get(uploadDir, subDir, doc.getAttachment()).toString();
		    Path path = Paths.get(fullPath);
	        if (Files.exists(path)) {
	            Files.delete(path);  // 파일 삭제
	        }
	        // 3) DB 삭제
	        documentService.dropFileInfo(documentNo);
	        
	        return ResponseEntity.ok("삭제되었습니다.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류 발생");
	    }
	}
	
	//다운로드 이력 전체조회
	
}
