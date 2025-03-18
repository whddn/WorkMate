package com.workmate.app.mail.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.workmate.app.mail.mapper.MailMapper;
import com.workmate.app.mail.service.MailService;
import com.workmate.app.mail.service.MailVO;

import jakarta.mail.MessagingException;
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
    public void sendEmail(String fromEmail, String toEmail, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
}