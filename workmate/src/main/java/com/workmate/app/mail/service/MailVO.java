package com.workmate.app.mail.service;

import java.util.Date;
import java.util.List;

import lombok.Data;
@Data
public class MailVO {
	private int attachmentCount; //첨부파일이 존재하는지 확인
	private int mailId; // 메일 id
	private Integer userNo; //사원번호
	private String recipients; // 수신자목록
	private String ccList; //참조자목록
	private String subject; //제목
	private String content; //본문
	private Date sentAt; //발송일시
	private String status; //메일상태
	private String encrypted; //암호화 여부
	private String mailType; //메일종류
	private String encryptedPwd; //암호화비밀번호
	private Date backupDate; //백업일시
	private String backupStatus; //백업상태
	private String isSpam; //스팸여부
	private Date reserSendtime; //에약된 발송시간
	private String reserStatus; //예약상태
	private int folderId; //폴더ID
	private String senderEmail;  // 외부 보낸 사람 이메일
	private String messageId; //메세지 중복방지
	private String isRead; //받은메일 읽음 표시
	private List<AttachmentVO> attachmentList;
	private String mailRole; // 'SENDER' 또는 'RECEIVER'
	
}
