package com.workmate.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
	@GetMapping("test")
	public String testPage(Model model) {
		return "test/test";
	}
}
