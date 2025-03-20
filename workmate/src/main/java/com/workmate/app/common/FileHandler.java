package com.workmate.app.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class FileHandler {
	public String fileUpload(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		try {
	        Path filePath = Paths.get("C://workmate/" + fileName);
	        Files.write(filePath, file.getBytes());
	        
	        String result = "파일 저장 완료: " + fileName;
	        System.out.println(result);
	        return result;
		}
		catch(IOException e) {
			String result = "파일 저장 실패: " + fileName;
			e.printStackTrace();
	        System.out.println(result);
	        return result;
		}
	}
	
	public ResponseEntity<FileSystemResource> fileDownload(String fileName, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        FileSystemResource resource = new FileSystemResource(path.toFile());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
