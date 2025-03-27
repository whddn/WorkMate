package com.workmate.app.document.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.workmate.app.document.service.DocVO;

public interface DocMapper {
	
	
	// 자료실 조회
	public List<DocVO> selectFileList();	
	
	// 자료실 단건 조회
	public DocVO selectFileById(@Param("documentNo") int documentNoO);
	
	// 자료실 업로드
	public int insertFile(DocVO docVO);	
	
	// 자료실 삭제
	public int deleteFileInfo(int docVO);
	
	// 다운로드시 자료실 이력추가
	public int insertDownhistory(DocVO docVO);
	
	// 자료실 이력 조회
	public List<DocVO> selectDownhistory(DocVO docVO);
}
