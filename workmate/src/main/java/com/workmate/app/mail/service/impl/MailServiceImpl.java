package com.workmate.app.mail.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.workmate.app.mail.mapper.MailMapper;
import com.workmate.app.mail.service.MailService;
import com.workmate.app.mail.service.MailVO;


import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {
    private final MailMapper mailMapper;
    private final JavaMailSender mailSender;
   
   
    // 받은 메일 조회1
    @Override
    public List<MailVO> findReceivedMailsList(int userNo) {
        return mailMapper.findReceivedMailsList(userNo);
    }

    // 단일 메일 조회
    @Override
    public MailVO findMailById(int mailId) {
        return mailMapper.findMailById(mailId);
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
    private Integer getUserNoByEmail(String email) {
        return mailMapper.findUserNoByEmail(email);
    }
    // 보낸 메일 전체 조회
    @Override
    public List<MailVO> findSentMailsList(int userNo) {
        return mailMapper.findSentMailsList(userNo);
    }
 // 보낸 메일 단건 조회
	@Override
	public MailVO findSentMailById(int mailId) {
		
		return mailMapper.findSentMailById(mailId);
	}
    
	// **IMAP을 통한 외부 메일 수신**
    @Override
    public void fetchAndStoreEmails() {
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imap.host", "imap.gmail.com");
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.ssl.enable", "true");

            Session session = Session.getDefaultInstance(properties);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", "the7100000@gmail.com", "fqepbkpnpxfiasuk");

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                MailVO mail = new MailVO();
                mail.setUserNo(201);
                mail.setRecipients("the7100000@gmail.com");
                mail.setSubject(message.getSubject());
                mail.setContent("IMAP을 통해 수신한 메일입니다.");
                mail.setSentAt(new Date());
                mail.setStatus("수신됨");
                mail.setFolderId(1001);
                mail.setMailType("외부");
                mail.setIsSpam("N");
                mail.setEncrypted("N");
                mailMapper.insertMail(mail);
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    
}