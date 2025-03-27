package com.workmate.app.mail.util;

import java.io.File;
import java.util.List;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class AttachmentEncryptor {

    /**
     * 단일 파일 암호화 압축
     */
    public static File encryptSingleFile(File file, String password) throws Exception {
        String zipPath = file.getParent() + File.separator + getZipName(file.getName());
        ZipFile zipFile = new ZipFile(zipPath, password.toCharArray());

        ZipParameters params = new ZipParameters();
        params.setEncryptFiles(true);
        params.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);

        zipFile.addFile(file, params);
        return new File(zipPath);
    }

    /**
     * 다중 파일 암호화 압축 (전체 경로 zipFullPath를 외부에서 지정)
     */
    public static File encryptMultipleFiles(List<File> files, String password, String zipFullPath) throws Exception {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("파일 목록이 비어있습니다.");
        }

        ZipFile zipFile = new ZipFile(zipFullPath, password.toCharArray());

        ZipParameters params = new ZipParameters();
        params.setEncryptFiles(true);
        params.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);

        for (File file : files) {
            zipFile.addFile(file, params);
        }

        return new File(zipFullPath);
    }

    /**
     * .zip 확장자 붙이기 유틸
     */
    private static String getZipName(String fileName) {
        if (fileName.lastIndexOf('.') != -1) {
            return fileName.substring(0, fileName.lastIndexOf('.')) + ".zip";
        }
        return fileName + ".zip";
    }
}
