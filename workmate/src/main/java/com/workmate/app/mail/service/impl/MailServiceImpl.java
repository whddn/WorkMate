package com.workmate.app.mail.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.workmate.app.mail.mapper.AttachmentMapper;
import com.workmate.app.mail.mapper.MailMapper;
import com.workmate.app.mail.service.AttachmentVO;
import com.workmate.app.mail.service.MailFolderVO;
import com.workmate.app.mail.service.MailService;
import com.workmate.app.mail.service.MailVO;

import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
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
    private final AttachmentMapper attachmentMapper;
   
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
    public void sendEmail(String senderName, String senderEmail,  String toEmail, String subject, String content) throws MessagingException {
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
	//스프링 스케쥴러
		//@Scheduled(fixedDelay = 300000) // 5분마다 실행 (ms 단위: 300,000ms = 5분)
		public void scheduledFetchEmails() {
		    System.out.println("⏰ [스케쥴러] 외부 메일 자동 수신 실행 중...");
		    fetchAndStoreEmails();
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

	        Session session = Session.getInstance(properties);
	        Store store = session.getStore("imaps");
	        store.connect("imap.gmail.com", "the7100000@gmail.com", "fqepbkpnpxfiasuk"); // 앱 비밀번호 사용

	        Folder inbox = store.getFolder("INBOX");
	        inbox.open(Folder.READ_ONLY);

	        Message[] messages = inbox.getMessages();

	        for (Message message : messages) {
	        	// Message-ID 가져오기
	        	String messageId = ((MimeMessage) message).getMessageID();
	        	if (mailMapper.countMessageId(messageId) > 0) {
	        	    System.out.println("⚠️ 이미 저장된 메일입니다: " + messageId);
	        	    continue;
	        	}//
	        	
	        	
	        	
	            Address[] toAddresses = message.getRecipients(Message.RecipientType.TO);
	            if (toAddresses == null || toAddresses.length == 0) continue;

	            // 🔹 수신자 이메일 추출 및 파싱
	            String fullAddress = toAddresses[0].toString();
	            String recipientEmail = extractEmail(fullAddress); // "홍길동 <aaa@bbb.com>" → "aaa@bbb.com"

	            // 🔍 사원번호 찾기
	            Integer userNo = mailMapper.findUserNoByEmail(recipientEmail);

	            // ❌ 해당 메일이 사내 사원 대상이 아니라면 skip
	            if (userNo == null) {
	                System.out.println("⚠️ 사원 찾을 수 없음: " + recipientEmail);
	                continue;
	            }
	            // 🔥 스팸 필터링 로직 추가
	            String content = getContent(message);
	            String subject = message.getSubject();
	            boolean isSpam = isSpamMail(subject, content);
	            String senderEmail = extractEmail(((InternetAddress) message.getFrom()[0]).toString());
	            // ✅ 해당 사원 메일함에 저장
	            MailVO mail = new MailVO();
	            mail.setUserNo(userNo); // ✔️ 해당 사원 번호로 저장
	            mail.setRecipients(recipientEmail);
	            mail.setSenderEmail(senderEmail);
	            mail.setSubject(message.getSubject());
	            mail.setContent(getContent(message));
	            mail.setSentAt(message.getSentDate());
	            mail.setStatus("수신됨");
	            mail.setFolderId(isSpam ? 1004 : 1001); // 스팸이면 1004로 분류
	            mail.setMailType("외부");
	            mail.setIsSpam(isSpam ? "Y" : "N");
	            mail.setEncrypted("N");

	            mailMapper.insertMail(mail);
	        }

	        inbox.close(false);
	        store.close();
	    } catch (Exception e) {
	        System.out.println("❌ 외부메일 수신 실패: " + e.getMessage());
	    }
	}
	// 스팸 판단 메서드 추가
	private boolean isSpamMail(String subject, String content) {
	    List<String> spamKeywords = List.of("viagra", "무료", "도박", "카지노", "click here", "성인", "축하합니다");
	    String combined = (subject + " " + content).toLowerCase();
	    return spamKeywords.stream().anyMatch(combined::contains);
	}
	
	/**
	 * 🔹 메일 본문 가져오는 메서드 (텍스트/HTML 처리 가능)
	 */
	private String getContent(Message message) {
	    try {
	    	System.out.println(message);
	        if (message.isMimeType("text/plain")) {
	            return (String) message.getContent();
	        } else if (message.isMimeType("multipart/*")) {
	            Multipart multipart = (Multipart) message.getContent();
	            for (int i = 0; i < multipart.getCount(); i++) {
	                BodyPart part = multipart.getBodyPart(i);
	                if (part.isMimeType("text/plain")) {
	                    return (String) part.getContent(); // 텍스트 본문 반환
	                } else if (part.isMimeType("text/html")) {
	                    return (String) part.getContent(); // HTML 본문 반환
	                }
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("❌ 본문 파싱 중 오류 발생: " + e.getMessage());
	    }
	    return "[본문 없음]"; // 본문이 없는 경우
	}
	private String extractEmail(String address) {
        if (address.contains("<") && address.contains(">")) {
            return address.substring(address.indexOf("<") + 1, address.indexOf(">"));
        }
        return address;
    }
	
	
	//삭제기능
	@Override
	public void moveMailsToTrash(List<Integer> mailIds) {
	    for (Integer mailId : mailIds) {
	        mailMapper.updateMailToTrash(mailId); // 매퍼 호출
	    }
	}
	//휴지통
	@Override
	public List<MailVO> findTrashMails(int userNo) {
	    return mailMapper.findTrashMails(userNo);
	}
	//폴더 조회
	@Override
	public List<MailFolderVO> findMailFolderList(int userNo) {
	    return mailMapper.selectMailFolderList(userNo);
	}
	//폴더 생성
	@Override
	public void inputMailFolder(MailFolderVO folder) {
	    mailMapper.insertMailFolder(folder);
	}
	//폴더삭제
	@Override
	public void dropMailFolder(int folderId, int userNo) {
	    mailMapper.deleteMailFolder(folderId, userNo);
	}
	
	//특정 폴더 ID의 메일 리스트를 조회
	@Override
	public List<MailVO> findMailsByFolderId(int userNo, int folderId) {
	    return mailMapper.selectMailsByFolderId(userNo, folderId);
	}
	//메일폴더 이동
	@Override
	public void modifyMailFolder(List<Integer> mailIds, int folderId) {
	    for (Integer mailId : mailIds) {
	        mailMapper.updateMailFolder(mailId, folderId);
	    }
	}
	//특정 폴더(FOLDER_ID) 안에 있는 메일들만 조회
	@Override
	public List<MailVO> findMailListByFolderId(int userNo, int folderId) {
	    return mailMapper.selectMailListByFolderId(userNo, folderId);
	}
	//특정폴더이름 넘기기(내가만든 폴더)
	@Override
	public MailFolderVO findMailFolderById(int folderId) {
	    return mailMapper.selectMailFolderById(folderId);
	}
	//메일 완전삭제
	@Override
	public void dropMail(List<Integer> mailIds) {
	    for (Integer mailId : mailIds) {
	        mailMapper.deleteMail(mailId); // 진짜 DB 삭제
	    }
	}
	// 페이징용 메일 리스트
	@Override
	public List<MailVO> findMailListPaging(int userNo, int folderId, int offset, int limit) {
	    return mailMapper.selectMailListPaging(userNo, folderId, offset, limit);
	}
	//총메일개수
	@Override
	public int countMailsByFolder(int userNo, int folderId) {
	    return mailMapper.countMailsByFolder(userNo, folderId);
	}
	// 공용 메일 저장
	@Override
	public void inputMail(MailVO mail) {
	    mailMapper.insertMail(mail);
	}
//스팸
	@Override
	public List<MailVO> findSpamMails(int userNo) {
	    return mailMapper.findSpamMails(userNo);
	}
	

	// 첨부파일 저장 구현
	@Override
	public void inputAttachments(List<AttachmentVO> files) {
	    for (AttachmentVO file : files) {
	        attachmentMapper.insertAttachment(file);
	    }
	}

	// 메일 ID로 첨부파일 목록 조회 구현
	@Override
	public List<AttachmentVO> findAttachmentsByMailId(int mailId) {
	    return attachmentMapper.selectAttachmentsByMailId(mailId);
	}

	// 파일 ID로 단일 파일 정보 조회 구현
	@Override
	public AttachmentVO findAttachmentById(Long fileId) {
	    return attachmentMapper.selectAttachmentById(fileId);
	}
	// ✅ 메일 저장 후 반환하는 메서드
	@Override
	public MailVO sendEmailReturnSaved(String senderName, String senderEmail, String recipients, String ccList, String subject, String content) throws MessagingException {
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	    String fromEmail = "the7100000@gmail.com"; // 대표 계정
	    InternetAddress fromAddress = new InternetAddress(fromEmail);
	    try {
	        fromAddress.setPersonal(senderName, "UTF-8");
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }

	    helper.setFrom(fromAddress);
	    helper.setTo(recipients);

	    // 내부/외부 구분 및 제목 자동 태그
	    boolean isInternal = mailMapper.findUserByEmail(recipients) != null;
	    String finalSubject = (isInternal ? "[내부] " : "[외부] ") + subject;
	    helper.setSubject(finalSubject);
	    helper.setText(content, true);
	    helper.setReplyTo(senderEmail);

	    // 📌 메일 DB 저장
	    MailVO mail = new MailVO();
	    mail.setUserNo(getUserNoByEmail(senderEmail));
	    mail.setRecipients(recipients);
	    mail.setCcList(ccList);
	    mail.setSubject(finalSubject);
	    mail.setContent(content);
	    mail.setSentAt(new Date());
	    mail.setStatus("발송됨");
	    mail.setEncrypted("N");
	    mail.setMailType(isInternal ? "내부" : "외부");
	    mail.setIsSpam("N");
	    mail.setFolderId(1002); // 보낸메일함
	    mail.setSenderEmail(senderEmail);
	    mail.setMessageId(((MimeMessage) message).getMessageID());

	    mailMapper.insertMail(mail); // mailId 채워짐

	    // ✅ 첨부파일 조회 후 메일에 추가
	    List<AttachmentVO> attachments = attachmentMapper.selectAttachmentsByMailId(mail.getMailId());
	    if (attachments != null && !attachments.isEmpty()) {
	        for (AttachmentVO file : attachments) {
	            File fileToAttach = new File(file.getFilePath());
	            if (fileToAttach.exists()) {
	                helper.addAttachment(file.getFileName(), fileToAttach);
	            }
	        }
	    }

	    // ✅ 실제 SMTP 전송
	    mailSender.send(message);

	    return mail;
	}


	// ✅ 첨부파일 리스트 저장 메서드
	@Override
	public void inputAttachmentList(List<AttachmentVO> attachList) {
//	    for (AttachmentVO file : attachList) {
//	        mailMapper.insertAttachment(file);
//	    }
	}
	@Value("${file.upload-dir}")
    private String uploadDir;
	
	
	@Override
	public void sendMailWithAttachment(String senderName, String senderEmail, String recipients, String ccList, String subject, String content, MultipartFile[] attachments) throws MessagingException {
	    // 1. 메일 객체 생성 및 저장
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	    String fromEmail = "the7100000@gmail.com";
	    InternetAddress fromAddress = new InternetAddress(fromEmail);
	    try {
	        fromAddress.setPersonal(senderName, "UTF-8");
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }

	    helper.setFrom(fromAddress);
	    helper.setTo(recipients);
	    if (ccList != null && !ccList.isBlank()) {
	        helper.setCc(ccList.split(",")); // 참조자 분리
	    }

	    // 내부/외부 여부
	    boolean isInternal = mailMapper.findUserByEmail(recipients) != null;
	    String finalSubject = (isInternal ? "[내부] " : "[외부] ") + subject;
	    helper.setSubject(finalSubject);
	    helper.setText(content, true);
	    helper.setReplyTo(senderEmail);

	    // 📌 메일 DB 저장용 객체 생성
	    MailVO mail = new MailVO();
	    Integer senderNo = getUserNoByEmail(senderEmail);

	    if (senderNo == null) {
	        throw new IllegalArgumentException("보낸 사람 사원번호를 찾을 수 없습니다.");
	    }

	    mail.setUserNo(senderNo);
	    mail.setRecipients(recipients);
	    mail.setCcList(ccList);
	    mail.setSubject(finalSubject);
	    mail.setContent(content);
	    mail.setSentAt(new Date());
	    mail.setStatus("발송됨");
	    mail.setEncrypted("N");
	    mail.setMailType(isInternal ? "내부" : "외부");
	    mail.setIsSpam("N");
	    mail.setFolderId(1002); // 보낸메일함
	    mail.setSenderEmail(senderEmail);
	    mail.setMessageId(((MimeMessage) message).getMessageID());

	    // 📥 먼저 메일 저장
	    mailMapper.insertMail(mail); // mailId 자동 세팅됨

	    // 📎 첨부파일 DB 저장 및 메일에 추가
	    if (attachments != null && attachments.length > 0) {
	        for (MultipartFile file : attachments) {
	        	 String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	             String fullPath = uploadDir + File.separator + fileName;

	        	AttachmentVO att = new AttachmentVO();
	        	att.setMailId(mail.getMailId());
	            att.setFileName(fileName);
	            att.setFileType(file.getContentType());
	            att.setFileSize(file.getSize());
	            att.setFilePath(fileName);
	        	
	            att.setMailId(mail.getMailId());

	            File localFile = new File(fullPath);
	            try {
					file.transferTo(localFile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	            
	            if (localFile.exists()) {
	                helper.addAttachment(att.getFileName(), localFile);
	            }
	            
	            // DB 저장
	            attachmentMapper.insertAttachment(att);
	        }
	    }

	    // ✉️ SMTP로 메일 전송
	    mailSender.send(message);

	    // 🔁 수신자 메일함에도 저장 (단, 내부 사용자일 경우만)
	    Integer recipientNo = getUserNoByEmail(recipients);
	    if (recipientNo != null) {
	        MailVO receiverMail = new MailVO();
	        receiverMail.setUserNo(recipientNo);
	        receiverMail.setRecipients(senderEmail);
	        receiverMail.setCcList(ccList);
	        receiverMail.setSubject(finalSubject);
	        receiverMail.setContent(content);
	        receiverMail.setSentAt(new Date());
	        receiverMail.setStatus("수신됨");
	        receiverMail.setEncrypted("N");
	        receiverMail.setMailType(isInternal ? "내부" : "외부");
	        receiverMail.setIsSpam("N");
	        receiverMail.setFolderId(1001); // 받은메일함
	        receiverMail.setSenderEmail(senderEmail);
	        receiverMail.setMessageId(mail.getMessageId());

	        mailMapper.insertMail(receiverMail);
	    }
	}

}
