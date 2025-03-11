package com.workmate.app.approval.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class ApprovalController {
	@GetMapping("approval/formChoose")
	public String formChoose(Model model) {
		return "approval/formChoose";
	}
	
	@GetMapping("approval/formWrite")
	public String formWriteGet(Model model) {
		return "approval/annualLeave";
	}
	
	@PostMapping("approval/formWrite")
	public String formWritePost(Model model) {
		return "approval/formChoose";
	}
	
	@GetMapping("approval/formRead")
	public String formReadGet(Model model) {
		return "approval/annualLeave";
	}
	
	@PutMapping("approval/formRead")
	public String formReadPut(Model model) {
		return "approval/formChoose";
	}
	
	@DeleteMapping("approval/formRead")
	public String formReadDelete(Model model) {
		return "approval/formChoose";
	}
}
