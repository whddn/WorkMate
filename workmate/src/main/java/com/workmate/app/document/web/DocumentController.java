package com.workmate.app.document.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.workmate.app.document.service.DocVO;
import com.workmate.app.document.service.DocumentService;
import com.workmate.app.security.service.LoginUserVO;

@Controller
public class DocumentController {
	
	private DocumentService documentService;
	
	@Autowired
	public DocumentController(DocumentService documentService) {
		this.documentService = documentService;
	}
	
	//자료실 전체 조회
	@GetMapping("document/list")
	public String documentList(Model model, @AuthenticationPrincipal LoginUserVO loginUser) {
		  if (loginUser == null || loginUser.getUserVO() == null) {
		        return "redirect:/login";  // 로그인 페이지로 리다이렉트
		    }
		
		String userId = loginUser.getUserVO().getUserId(); 
		
		List<DocVO> list = documentService.findFileList();
		model.addAttribute("lists",list);
		model.addAttribute("userId",userId);
		
		return "document/list";
	}
	
	//자료 업로드
//	@GetMapping("document/upload")
//	public String uploadFile() {
//		return "document/list";
//	}
//	
//	@PostMapping("document/upload")
//	public String uploadFileProcess(DocVO docVO) {
//		
//		return null;
//	}
	
	//자료 단건 삭제
	@DeleteMapping("document/delete/{documentNo}")
	public ResponseEntity<String> documentDelete(@PathVariable Integer documentNo) {
	    try {
	        if (documentNo == null) {
	            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
	        }
	        documentService.dropFileInfo(documentNo);
	        return ResponseEntity.ok("삭제되었습니다.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류 발생");
	    }
	}
}
