package com.workmate.app.mainscreen.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainscreenController {
	@GetMapping("/")
	public String base(Model model) {
		return "mainscreen/main";
	}
	
	@GetMapping("/search")
	public String search(Model model) {
		return "mainscreen/search";
	}
}
