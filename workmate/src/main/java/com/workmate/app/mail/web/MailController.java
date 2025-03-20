package com.workmate.app.mail.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.workmate.app.mail.service.MailService;
import com.workmate.app.mail.service.MailVO;
import com.workmate.app.security.service.LoginUserVO;

import jakarta.mail.MessagingException;

@Controller
public class MailController {
	private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    // 받은 메일 조회 (로그인한 사용자)
    @GetMapping("mail/mailmain")
    public String receivedMailList(Model model, @AuthenticationPrincipal LoginUserVO loginUser) {
    	// 로그인한 사용자의 이메일 정보 가져오기
        int userNo = loginUser.getUserVO().getUserNo(); // ✅ userMail 가져오기

        // 받은 메일 조회
        List<MailVO> receivedMails = mailService.findReceivedMailsList(userNo); // ✅ 이메일 기준 조회

        // 모델 데이터 추가
        model.addAttribute("receivedMails", receivedMails);

        return "mail/mailmain";
    }
    
 // 메일 단건 조회
    @GetMapping("mail/view")
    public String viewMail(@RequestParam("mailId") int mailId, Model model) {
        MailVO mail = mailService.findMailById(mailId);
        model.addAttribute("mail", mail);
        return "mail/view";
    }
    
    //메일 쓰기
    @GetMapping("mail/compose") 
	public String compose() {
		return "mail/compose";
	}
    //메일 보내는 기능
    @PostMapping("/mail/send")
    public String sendMail(
            @AuthenticationPrincipal LoginUserVO loginUser,
            @RequestParam String recipients,
            @RequestParam(required = false) String ccList,
            @RequestParam String subject,
            @RequestParam String content) {
        try {
            // ✅ 로그인한 사용자의 정보 가져오기
            String senderName = loginUser.getUserVO().getUserName();  // 사원 이름
            String senderEmail = loginUser.getUserVO().getUserMail(); // 사원 이메일

            // ✅ 메일 전송
            mailService.sendEmail(senderName, senderEmail, recipients, subject, content);

            return "redirect:/mail/mailmain"; // 성공 시 받은 메일함으로 이동
        } catch (MessagingException e) {
            return "전송 실패: " + e.getMessage();
        }
    }
    //보낸 메일함 전쳊
    @GetMapping("mail/sent")
    public String sentMailList(Model model, @AuthenticationPrincipal LoginUserVO loginUser) {
        int userNo = loginUser.getUserVO().getUserNo();

        List<MailVO> sentMails = mailService.findSentMailsList(userNo);
        model.addAttribute("sentMails", sentMails);

        return "mail/sent";
    }
 // 보낸 메일 상세 
    @GetMapping("mail/sentview")
    public String viewSentMail(@RequestParam("mailId") int mailId, Model model) {
        MailVO mail = mailService.findSentMailById(mailId);
        model.addAttribute("mail", mail);
        return "mail/sentview";
    }
    
 // **외부 메일 수신 (IMAP) 추가**
    @GetMapping("/mail/fetchAndStore")
    public String fetchAndStoreEmails() {
        mailService.fetchAndStoreEmails();
        return "redirect:/mail/mailmain"; // 받은 메일함으로 이동
    }
 
}