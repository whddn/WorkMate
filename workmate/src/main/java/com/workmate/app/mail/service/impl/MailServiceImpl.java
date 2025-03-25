package com.workmate.app.mail.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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

import com.workmate.app.employee.service.DepartmentVO;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.employee.service.TeamVO;
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
import jakarta.mail.Part;
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
  
  //전체부서목록 조회
    @Override
    public List<DepartmentVO> findDepartmentList() {
        return mailMapper.selectDepartmentList();
    }
  //특정부서 팀목록
    @Override
    public List<TeamVO> findTeamListByDepartment(String departmentId) {
        return mailMapper.selectTeamListByDepartment(departmentId);
    }
  //특정 팀에 속한 사원들의 이메일 주소
    @Override
    public List<String> findEmailsByTeam(String teamNo) {
        return mailMapper.selectEmailsByTeam(teamNo);
    }
    
    
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
    public void sendEmail(String senderName, String senderEmail, String recipients, String subject, String content) throws MessagingException {
        String[] recipientList = recipients.split(",");

        for (String recipient : recipientList) {
            recipient = recipient.trim();
            if (recipient.isBlank()) continue;

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
            helper.setTo(recipient);

            boolean isInternal = mailMapper.findUserByEmail(recipient) != null;
            String finalSubject = (isInternal ? "[내부] " : "[외부] ") + subject;
            helper.setSubject(finalSubject);
            helper.setText(content, true);
            helper.setReplyTo(senderEmail);

            mailSender.send(message);

            MailVO mail = new MailVO();
            mail.setUserNo(getUserNoByEmail(senderEmail));
            mail.setRecipients(recipient);
            mail.setSubject(finalSubject);
            mail.setContent(content);
            mail.setSentAt(new Date());
            mail.setStatus("발송됨");
            mail.setEncrypted("N");
            mail.setMailType(isInternal ? "내부" : "외부");
            mail.setIsSpam("N");
            mail.setFolderId(1002);
            mail.setSenderEmail(senderEmail);
            mailMapper.insertMail(mail);

            Integer recipientNo = getUserNoByEmail(recipient);
            if (recipientNo != null) {
                MailVO receiverMail = new MailVO();
                receiverMail.setUserNo(recipientNo);
                receiverMail.setRecipients(senderEmail);
                receiverMail.setSubject(finalSubject);
                receiverMail.setContent(content);
                receiverMail.setSentAt(new Date());
                receiverMail.setStatus("수신됨");
                receiverMail.setEncrypted("N");
                receiverMail.setMailType(isInternal ? "내부" : "외부");
                receiverMail.setIsSpam("N");
                receiverMail.setFolderId(1001);
                receiverMail.setSenderEmail(senderEmail);
                mailMapper.insertMail(receiverMail);
            }
        }
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
		            String messageId = ((MimeMessage) message).getMessageID();
		            if (mailMapper.countMessageId(messageId) > 0) continue;

		            Address[] toAddresses = message.getRecipients(Message.RecipientType.TO);
		            if (toAddresses == null || toAddresses.length == 0) continue;

		            String recipientEmail = extractEmail(toAddresses[0].toString());
		            Integer userNo = mailMapper.findUserNoByEmail(recipientEmail);
		            if (userNo == null) continue;

		            String subject = message.getSubject();
		            String content = getContent(message);
		            boolean isSpam = isSpamMail(subject, content);
		            String senderEmail = extractEmail(((InternetAddress) message.getFrom()[0]).toString());

		            MailVO mail = new MailVO();
		            mail.setUserNo(userNo);
		            mail.setRecipients(recipientEmail);
		            mail.setSenderEmail(senderEmail);
		            mail.setSubject(subject);
		            mail.setContent(content);
		            mail.setSentAt(message.getSentDate());
		            mail.setStatus("수신됨");
		            mail.setFolderId(isSpam ? 1004 : 1001);
		            mail.setMailType("외부");
		            mail.setIsSpam(isSpam ? "Y" : "N");
		            mail.setEncrypted("N");
		            mail.setMessageId(messageId);

		            mailMapper.insertMail(mail);
		            System.out.println("📨 메일 저장 완료: " + subject);

		            if (message.isMimeType("multipart/*")) {
		                Multipart multipart = (Multipart) message.getContent();
		                for (int i = 0; i < multipart.getCount(); i++) {
		                    BodyPart part = multipart.getBodyPart(i);
		                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
		                        String originalFileName = part.getFileName();
		                        String uuidFileName = UUID.randomUUID() + "_" + originalFileName;
		                        String fullPath = uploadDir + File.separator + uuidFileName;

		                        File file = new File(fullPath);
		                        try (InputStream is = part.getInputStream()) {
		                            Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		                        }

		                        AttachmentVO att = new AttachmentVO();
		                        att.setMailId(mail.getMailId());
		                        att.setFileName(originalFileName);
		                        att.setFileType(part.getContentType());
		                        att.setFileSize((long) part.getSize());
		                        att.setFilePath(fullPath);

		                        attachmentMapper.insertAttachment(att);
		                        System.out.println("✅ 첨부파일 저장됨: " + originalFileName);
		                    }
		                }
		            }
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
	    String[] recipientList = recipients.split(",");
	    String fromEmail = "the7100000@gmail.com";

	    // ✅ 첨부파일 먼저 저장 (한 번만)
	    List<AttachmentVO> savedAttachments = new ArrayList<>();

	    if (attachments != null && attachments.length > 0) {
	        for (MultipartFile file : attachments) {
	            if (file.isEmpty()) continue;

	            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	            String fullPath = uploadDir + File.separator + fileName;
	            File localFile = new File(fullPath);
	            try {
	                file.transferTo(localFile);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }

	            AttachmentVO att = new AttachmentVO();
	            att.setFileName(file.getOriginalFilename());
	            att.setFileType(file.getContentType());
	            att.setFileSize(file.getSize());
	            att.setFilePath(fullPath);

	            savedAttachments.add(att); // 저장된 첨부파일 목록
	        }
	    }

	    for (String recipient : recipientList) {
	        recipient = recipient.trim();
	        if (recipient.isBlank()) continue;

	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	        try {
	            InternetAddress fromAddress = new InternetAddress(fromEmail, senderName, "UTF-8");
	            helper.setFrom(fromAddress);
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }

	        helper.setTo(recipient);
	        if (ccList != null && !ccList.isBlank()) {
	            helper.setCc(ccList.split(","));
	        }

	        boolean isInternal = mailMapper.findUserByEmail(recipient) != null;
	        String finalSubject = (isInternal ? "[내부] " : "[외부] ") + subject;
	        helper.setSubject(finalSubject);
	        helper.setText(content, true);
	        helper.setReplyTo(senderEmail);

	        // ✅ 파일 다시 첨부 (경로 기준으로 File 객체 만들어서)
	        for (AttachmentVO att : savedAttachments) {
	            File fileToAttach = new File(att.getFilePath());
	            if (fileToAttach.exists()) {
	                helper.addAttachment(att.getFileName(), fileToAttach);
	            }
	        }

	        // ✉️ 전송
	        mailSender.send(message);

	        // ✅ 보낸 메일함 저장
	        MailVO mail = new MailVO();
	        Integer senderNo = getUserNoByEmail(senderEmail);
	        mail.setUserNo(senderNo);
	        mail.setRecipients(recipient);
	        mail.setCcList(ccList);
	        mail.setSubject(finalSubject);
	        mail.setContent(content);
	        mail.setSentAt(new Date());
	        mail.setStatus("발송됨");
	        mail.setEncrypted("N");
	        mail.setMailType(isInternal ? "내부" : "외부");
	        mail.setIsSpam("N");
	        mail.setFolderId(1002);
	        mail.setSenderEmail(senderEmail);
	        mail.setMessageId(((MimeMessage) message).getMessageID());

	        mailMapper.insertMail(mail);

	        // ✅ 첨부파일 DB에 저장 (mailId 포함)
	        for (AttachmentVO att : savedAttachments) {
	            AttachmentVO copy = new AttachmentVO();
	            copy.setMailId(mail.getMailId());
	            copy.setFileName(att.getFileName());
	            copy.setFileType(att.getFileType());
	            copy.setFileSize(att.getFileSize());
	            copy.setFilePath(att.getFilePath());
	            attachmentMapper.insertAttachment(copy);
	        }

	        // ✅ 수신자 메일함 (내부 사용자일 때만)
	        Integer recipientNo = getUserNoByEmail(recipient);
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
	            receiverMail.setFolderId(1001);
	            receiverMail.setSenderEmail(senderEmail);
	            receiverMail.setMessageId(mail.getMessageId());

	            mailMapper.insertMail(receiverMail);
	        }
	    }
	}


	//임시저장 첨부파일
	@Override
	public void saveDraftMail(MailVO mail, MultipartFile[] attachments) {
	    // 1. 임시 메일 저장
	    mailMapper.insertMail(mail); // mailId 자동 생성됨

	    // 2. 첨부파일 처리
	    if (attachments != null && attachments.length > 0) {
	        for (MultipartFile file : attachments) {
	            if (file.isEmpty()) continue;

	            try {
	                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	                String fullPath = uploadDir + File.separator + fileName;

	                File dest = new File(fullPath);
	                file.transferTo(dest);

	                AttachmentVO att = new AttachmentVO();
	                att.setMailId(mail.getMailId());
	                att.setFileName(file.getOriginalFilename());
	                att.setFileType(file.getContentType());
	                att.setFileSize(file.getSize());
	                att.setFilePath(fullPath);

	                attachmentMapper.insertAttachment(att);
	            } catch (IOException e) {
	                e.printStackTrace(); // 필요시 로그 처리
	            }
	        }
	    }
	}
	//특정 팀에 속한 사원들의 정보 전체
	@Override
	public List<EmpVO> findEmployeesByTeam(String teamNo) {
	    return mailMapper.selectEmployeesByTeam(teamNo);
	}
}
