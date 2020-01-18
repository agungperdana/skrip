package com.kratonsolution.skrip.leon.view.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kratonsolution.skrip.leon.model.Kasus;

import io.jsondb.JsonDBTemplate;

@Controller
public class KasusController {

	private static final String HOME = "redirect:/admin/kasus-home";

	@Autowired
	private JsonDBTemplate db;
	
	@GetMapping("/admin/kasus-home")
	public String home() {
		
		return "kasus-home";
	}
	
	@GetMapping("/admin/kasus-preadd")
	public String preadd(@RequestParam("bit") String bit) {
		
		Kasus kasus = new Kasus();
		
		
		return "kasus-add";
	}
	
	@PostMapping("/admin/kasus-add")
	public String add() {
		
		return HOME;
	}
}
