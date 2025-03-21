package com.workmate.app.document.mapper;

import java.util.List;

import com.workmate.app.document.service.DocVO;

public interface DocMapper {
	
	
	// 자료실 조회
	public List<DocVO> selectFileList();
	
	// 자료실 업로드
	public int insertFile(DocVO docVO);
	
	// 자료실 삭제
	public int deleteFileInfo(int docVO);
}
