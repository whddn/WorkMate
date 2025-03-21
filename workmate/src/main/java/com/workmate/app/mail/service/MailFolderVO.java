package com.workmate.app.mail.service;

import lombok.Data;

@Data
public class MailFolderVO {
	private int folderId;
    private int userNo;
    private String folderName;
    private String folderType;
    private String editable;
}