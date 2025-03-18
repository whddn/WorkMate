package com.workmate.app.mail.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        List<MailVO> receivedMails = mailService.getReceivedMails(userNo); // ✅ 이메일 기준 조회

        // 모델 데이터 추가
        model.addAttribute("receivedMails", receivedMails);

        return "mail/mailmain";
    }
    
 // 메일 단건 조회
    @GetMapping("mail/view")
    public String viewMail(@RequestParam("mailId") int mailId, Model model) {
        MailVO mail = mailService.getMailById(mailId);
        model.addAttribute("mail", mail);
        return "mail/view";
    }
    
    
    @GetMapping("mail/compose") 
	public String compose() {
		return "mail/compose";
	}
 // ✅ 메일 테스트 API (브라우저에서 호출 가능)
    @GetMapping("/mail/sendTest")
    public String sendTestEmail(@RequestParam String to) {
        String fromEmail = "th9080@naver.com"; // 
        String subject = "테스트 메일";
        String content = "<h2>이메일 테스트</h2><p>이메일이 정상적으로 전송됩니다!</p>";

        try {
            mailService.sendEmail(fromEmail, to, subject, content);
            return "메일 전송 성공!";
        } catch (MessagingException e) {
            return "메일 전송 실패: " + e.getMessage();
        }
    }
}