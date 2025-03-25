package com.workmate.app.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileHandler {
	public String fileUpload(MultipartFile file, String fileDir, boolean overwrite) {
		String fileName = file.getOriginalFilename();
		
		// 덮어쓰기를 피한다
		if(!overwrite) {
			String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			fileName = UUID.randomUUID().toString() + fileExtension;
		}
		
		try {
			// 파일 저장 폴더가 없으면 생성
			File dir = new File(fileDir);
			if (!dir.exists())
				dir.mkdirs();

			// 파일 경로 설정
	        Path filePath = Paths.get(fileDir, fileName);
	        System.out.println(filePath.toString());
	        
	        // 파일로 저장
	        Files.write(filePath, file.getBytes());
	        return fileName;
		}
		catch(IOException e) {
			e.printStackTrace();
	        return "";
		}
	}
	
	public FileSystemResource fileDownload(String fileName, String filePath) throws IOException {
        Path path = Paths.get(filePath, fileName);
        return new FileSystemResource(path.toFile());
    }
	
	public String htmlStrUpload(String title, String content, String fileDir, boolean overwrite) {
		byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
		InputStream inputStream = new ByteArrayInputStream(contentBytes);
		title += ".html";
		
		try {
	        MultipartFile multipartFile = new MockMultipartFile(
	            title,		// 파일명
	            title,		// 원본 파일명
	            "text/html",// Content Type
	            inputStream	// 파일 데이터
	        );
	
	        return fileUpload(multipartFile, fileDir, overwrite);
		}
		catch(IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
