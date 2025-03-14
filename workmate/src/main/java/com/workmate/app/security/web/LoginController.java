package com.workmate.app.security.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class LoginController {
	@GetMapping("login") 
	public String login() {
		return "login/login";
	}
	@GetMapping("/logout-success")
    public String logoutSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "로그아웃되었습니다.");
        return "redirect:/workmate";
    }
	
}


