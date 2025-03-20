package com.workmate.app.mail.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.workmate.app.mail.mapper.MailMapper;
import com.workmate.app.mail.service.MailService;
import com.workmate.app.mail.service.MailVO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {
    private final MailMapper mailMapper;
    private final JavaMailSender mailSender;

    @Autowired
    public MailServiceImpl(MailMapper mailMapper, JavaMailSender mailSender) {
        this.mailMapper = mailMapper;
        this.mailSender = mailSender;
    }

    // 받은 메일 조회
    @Override
    public List<MailVO> getReceivedMails(int userNo) {
        return mailMapper.getReceivedMails(userNo);
    }

    // 단일 메일 조회
    @Override
    public MailVO getMailById(int mailId) {
        return mailMapper.getMailById(mailId);
    }

    // 이메일 전송
    @Override
    public void sendEmail(String senderName, String senderEmail, String toEmail, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String fromEmail = "the7100000@gmail.com"; // ✅ 대표 이메일

        // InternetAddress 생성 후 setPersonal 사용
        InternetAddress fromAddress = new InternetAddress(fromEmail);
        try {
            fromAddress.setPersonal(senderName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        helper.setFrom(fromAddress);
        helper.setTo(toEmail);

        // ✅ 내부/외부 메일 구분 (DB 조회)
        boolean isInternal = mailMapper.findUserByEmail(toEmail) != null;

        // ✅ 자동 태그 추가
        String finalSubject = isInternal ? "[내부] " + subject : "[외부] " + subject;
        helper.setSubject(finalSubject);

        helper.setText(content, true);
        helper.setReplyTo(senderEmail);

        // 메일 전송
        mailSender.send(message);

        // DB에 저장할 메일 객체 생성
        MailVO mail = new MailVO();
        mail.setUserNo(getUserNoByEmail(senderEmail)); // 보낸 사람의 사원번호
        mail.setRecipients(toEmail);
        mail.setFolderId(1002); // 보낸 메일함 폴더 ID
        mail.setSubject(finalSubject);
        mail.setContent(content);
        mail.setSentAt(new Date());
        mail.setStatus("발송됨");
        mail.setEncrypted("N");
        mail.setMailType(isInternal ? "내부" : "외부"); // 내부/외부 자동 구분
        mail.setIsSpam("N");

        // 메일을 DB에 저장
        mailMapper.insertMail(mail);
        
        mail.setUserNo(getUserNoByEmail(toEmail)); // 보낸 사람의 사원번호
        mail.setRecipients(senderEmail);
        mail.setFolderId(1001); // 보낸 메일함 폴더 ID
        mailMapper.insertMail(mail);
    }

    // 이메일을 기반으로 사원번호를 찾는 메서드
    private int getUserNoByEmail(String email) {
        return mailMapper.findUserNoByEmail(email);
    }
    // 보낸 메일 전체 조회
    @Override
    public List<MailVO> getSentMails(int userNo) {
        return mailMapper.getSentMails(userNo);
    }
    
    
}