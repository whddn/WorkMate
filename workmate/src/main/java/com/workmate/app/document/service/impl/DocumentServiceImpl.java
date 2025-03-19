package com.workmate.app.document.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.workmate.app.document.mapper.DocMapper;
import com.workmate.app.document.service.DocVO;
import com.workmate.app.document.service.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {
	
	private DocMapper docMapper;

	DocumentServiceImpl(DocMapper docMapper) {
		this.docMapper = docMapper;
	}

	@Override
	public List<DocVO> findFileList() {
		return docMapper.selectFileList();
	}
}
