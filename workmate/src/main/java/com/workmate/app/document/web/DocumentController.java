package com.workmate.app.document.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.workmate.app.document.service.DocVO;
import com.workmate.app.document.service.DocumentService;

@Controller
public class DocumentController {
	
	private DocumentService documentService;
	
	@Autowired
	public DocumentController(DocumentService documentService) {
		this.documentService = documentService;
	}
	
	//자료실 전체 조회
	@GetMapping("document/list")
	public String documentList(Model model) {
		
		List<DocVO> list = documentService.findFileList();
		model.addAttribute("lists",list);
		
		return "document/list";
	}
}
