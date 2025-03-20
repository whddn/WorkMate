package com.workmate.app.mail.service;

import java.util.List;

import jakarta.mail.MessagingException;

public interface MailService {
    // 받은 메일 조회
    List<MailVO> getReceivedMails(int userNo);

    // 단일 메일 조회
    MailVO getMailById(int mailId);

    // 이메일 전송
    void sendEmail(String senderName, String senderEmail, String toEmail, String subject, String content) throws MessagingException;
    //보낸 메일함
    List<MailVO> getSentMails(int userNo);
}