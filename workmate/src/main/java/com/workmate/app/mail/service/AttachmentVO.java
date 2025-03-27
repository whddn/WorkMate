package com.workmate.app.mail.service;

import lombok.Data;

@Data
public class AttachmentVO {
	private Long fileId;       // 파일 고유 ID
    private Integer mailId;       // 연관된 메일 ID
    private String fileName;   // 원본 파일 이름
    private String fileType;   // 파일 MIME 타입
    private Long fileSize;     // 파일 크기 (바이트)
    private String filePath;   // 서버 내 저장 경로
    
}
