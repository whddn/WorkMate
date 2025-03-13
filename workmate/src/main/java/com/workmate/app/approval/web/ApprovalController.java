package com.workmate.app.approval.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class ApprovalController {
	@GetMapping("/")
	public String base(Model model) {
		return "base";
	}
	
	@GetMapping("approval/waiting")
	public String waiting(Model model) {
		return "approval/waiting";
	}
	
	@GetMapping("approval/complete")
	public String complete(Model model) {
		return "approval/complete";
	}
	
	@GetMapping("approval/formList")
	public String formList(Model model) {
		return "approval/formList";
	}
	
	@GetMapping("approval/formWrite")
	public String formWriteGet(Model model) {
		return "approval/formWrite";
	}
	
	@PostMapping("approval/formWrite")
	public String formWritePost(Model model) {
		return "approval/formList";
	}
	
	@GetMapping("approval/formRead")
	public String formReadGet(Model model) {
		return "approval/formRead";
	}
	
	@PutMapping("approval/formRead")
	public String formReadPut(Model model) {
		return "approval/formList";
	}
	
	@DeleteMapping("approval/formRead")
	public String formReadDelete(Model model) {
		return "approval/formList";
	}
	
	@GetMapping("approval/manage")
	public String manage(Model model) {
		return "approval/manage";
	}
}
