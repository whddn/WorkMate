package com.workmate.app.common;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.workmate.app.security.service.LoginUserVO;

@ControllerAdvice
public class ModelAttributeAdvice {
	@ModelAttribute
    public void addCommonAttributes(Model model, @AuthenticationPrincipal LoginUserVO loginUser) {
        if (loginUser != null && loginUser.getUserVO() != null) {
            model.addAttribute("userId", loginUser.getUserVO().getUserId());
            model.addAttribute("userName", loginUser.getUserVO().getUserName());
            model.addAttribute("teamNo", loginUser.getUserVO().getTeamNo());
        }
    }
}
