package com.workmate.app.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

public class FileHandler {
	public String fileUpload(MultipartFile file, String fileDir) {
		String fileName = file.getOriginalFilename();
		String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
		try {
			// ✅ 1. 파일 저장 폴더가 없으면 생성
			File dir = new File(fileDir);
			if (!dir.exists())
				dir.mkdirs();

	        Path filePath = Paths.get(fileDir + uniqueFileName);
	        Files.write(filePath, file.getBytes());
	        return uniqueFileName;
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
