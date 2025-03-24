package com.workmate.app.mail.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;

public interface MailService {
	// 받은 메일 조회
	List<MailVO> findReceivedMailsList(int userNo);

	// 단일 메일 조회
	MailVO findMailById(int mailId);

	// 이메일 전송
	void sendEmail(String senderName, String senderEmail, String toEmail, String subject, String content)
			throws MessagingException;

	// 보낸 메일함
	List<MailVO> findSentMailsList(int userNo);

	// 보낸 메일 상세ㅁ
	MailVO findSentMailById(int mailId);

	// **외부 메일 수신 메서드 추가**
	void fetchAndStoreEmails();

	// 삭제기능
	void moveMailsToTrash(List<Integer> mailIds);

	// 휴지통 조회
	List<MailVO> findTrashMails(int userNo);

	// 전체 폴더 조회 (내 폴더용)
	List<MailFolderVO> findMailFolderList(int userNo);

	// 폴더 추가
	void inputMailFolder(MailFolderVO folder);

	// 폴더 삭제
	void dropMailFolder(int folderId, int userNo);

	// 폴더 ID로 메일 목록을 가져오는 서비스
	List<MailVO> findMailsByFolderId(int userNo, int folderId);

	// 메일 폴더 이동 기능
	void modifyMailFolder(List<Integer> mailIds, int folderId);

	// 특정 폴더(FOLDER_ID) 안에 있는 메일들만 조회
	List<MailVO> findMailListByFolderId(int userNo, int folderId);

	// 특정폴더이름 넘기기(내가만든 폴더)
	MailFolderVO findMailFolderById(int folderId);

	// 메일 완전 삭제
	void dropMail(List<Integer> mailIds);

	// 공용 페이징 메일 조회
	List<MailVO> findMailListPaging(int userNo, int folderId, int offset, int limit);

	// 공용 메일 개수 조회
	int countMailsByFolder(int userNo, int folderId);

	// 메일 저장 공용 메서드
	void inputMail(MailVO mail);
	//스팸
	List<MailVO> findSpamMails(int userNo);
	// 첨부파일 저장
	void inputAttachments(List<AttachmentVO> files);

	// 메일 ID로 첨부파일 목록 조회
	List<AttachmentVO> findAttachmentsByMailId(int mailId);

	// 파일 ID로 단일 파일 정보 조회
	AttachmentVO findAttachmentById(Long fileId);
	
	MailVO sendEmailReturnSaved(String senderName, String senderEmail, String recipients, String ccList, String subject, String content) throws MessagingException;
	//첨부파일 리스트 DB 저장
	void inputAttachmentList(List<AttachmentVO> attachList);
	// 첨부파일 포함 이메일 전송
	void sendMailWithAttachment(String senderName, String senderEmail, String recipients, String ccList, String subject, String content, MultipartFile[] attachments) throws MessagingException;
}