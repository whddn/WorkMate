package com.workmate.app.document.web;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.workmate.app.common.FileHandler;
import com.workmate.app.document.service.DocVO;
import com.workmate.app.document.service.DocumentService;
import com.workmate.app.security.service.LoginUserVO;

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
	@GetMapping("document/list")
	public String documentList(Model model, @AuthenticationPrincipal LoginUserVO loginUser) {
		
		String userId = loginUser.getUserVO().getUserId();
		String teamNo = loginUser.getUserVO().getTeamNo();
		
		List<DocVO> list = documentService.findFileList();
		model.addAttribute("lists",list);
		model.addAttribute("userId",userId);
		model.addAttribute("teamNo",teamNo);
		
		return "document/list";
	}
	
	//자료 업로드	
	@PostMapping("document/upload")
	public  ResponseEntity<String> uploadFileProcess(@RequestParam("uploadFile") MultipartFile file,
                                    DocVO docVO,
                                    Model model) {
		try {
		
			if(file.isEmpty()) {
				model.addAttribute("message", "파일 업로드 실패");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
			}
			String fileName = file.getOriginalFilename();
			String saveFileName = fileHandler.fileUpload(file, uploadDir + subDir );
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
	
	//자료 단건 삭제
	@DeleteMapping("document/delete/{documentNo}")
	public ResponseEntity<String> documentDelete(@PathVariable Integer documentNo) {
	    try {
	        if (documentNo == null) {
	            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
	        }
	        //DB 삭제
	        documentService.dropFileInfo(documentNo);
	        
	        return ResponseEntity.ok("삭제되었습니다.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류 발생");
	    }
	}
}
