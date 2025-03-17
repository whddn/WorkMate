package com.workmate.app.mail.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.workmate.app.mail.service.MailService;
import com.workmate.app.mail.service.MailVO;
import com.workmate.app.security.service.LoginUserVO;

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
}