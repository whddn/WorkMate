package com.workmate.app.document.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.workmate.app.attendance.service.WorkVO;
import com.workmate.app.document.mapper.DocMapper;
import com.workmate.app.document.service.DocVO;
import com.workmate.app.document.service.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {
	
	private DocMapper docMapper;

	DocumentServiceImpl(DocMapper docMapper) {
		this.docMapper = docMapper;
	}
	
	// 파일 조회
	@Override
	public List<DocVO> findFileList() {
		return docMapper.selectFileList();
	}
	
	// 파일 업로드
	@Override
	public int inputFile(DocVO docVO) {
		int result = docMapper.insertFile(docVO);
		return result == 1 ? docVO.getDocumentNo() : -1;
	}
	
	// 파일 단건 삭제
	@Override
	public Map<String, Object> dropFileInfo(int documentNo) {
		Map<String, Object> map = new HashMap<>();
		
		int result = docMapper.deleteFileInfo(documentNo);
		if(result == 1) {
			map.put("documentNo", documentNo);
		}
		return map;
	}


}
