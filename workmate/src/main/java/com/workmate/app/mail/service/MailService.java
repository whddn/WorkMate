package com.workmate.app.mail.service;

import java.util.List;

import jakarta.mail.MessagingException;

public interface MailService {
    // 받은 메일 조회
    List<MailVO> findReceivedMailsList(int userNo);

    // 단일 메일 조회
    MailVO findMailById(int mailId);

    // 이메일 전송
    void sendEmail(String senderName, String senderEmail, String toEmail, String subject, String content) throws MessagingException;
    //보낸 메일함
    List<MailVO> findSentMailsList(int userNo);
    //보낸 메일 상세ㅁ
	MailVO findSentMailById(int mailId);
	
	// **외부 메일 수신 메서드 추가**
    void fetchAndStoreEmails();
	 
}