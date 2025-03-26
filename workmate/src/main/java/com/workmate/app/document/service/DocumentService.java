package com.workmate.app.document.service;

import java.util.List;
import java.util.Map;


public interface DocumentService {
	
	// 자료 전체 조회  
	public List<DocVO> findFileList();
	
	// 자료 단건 조회
	public DocVO findFileById(int documentNo);
	
	// 자료 업로드
	public int inputFile(DocVO docVO);
	
	// 자료 삭제
	public Map<String, Object> dropFileInfo(int documentNo);

}
