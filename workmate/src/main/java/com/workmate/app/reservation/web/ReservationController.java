package com.workmate.app.reservation.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationController {
	
	@GetMapping("reservation")
	public String reservation(Model model) {
		return "reservation/reservation";
	}
}
