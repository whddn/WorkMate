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
	public String fileUpload(MultipartFile file, String fileDir) {
		String fileName = file.getOriginalFilename();
		try {
	        Path filePath = Paths.get(fileDir + fileName);
	        Files.write(filePath, file.getBytes());
	        return filePath.toString();
		}
		catch(IOException e) {
			e.printStackTrace();
	        return "";
		}
	}
	
	public FileSystemResource fileDownload(String fileName, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return new FileSystemResource(path.toFile());
    }
}
