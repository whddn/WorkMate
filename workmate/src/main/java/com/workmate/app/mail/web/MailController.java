package com.workmate.app.mail.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MailController {
	@GetMapping("mail/mailmain")
	public String mailmain() {
		return "mail/mailmain";
	}
}