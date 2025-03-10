package com.workmate.app.approval.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApprovalController {
	@GetMapping("approval/formChoose")
	public String formChoose(Model model) {
		return "approval/formChoose";
	}
}
