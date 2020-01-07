/**
 * 
 */
package com.kratonsolution.skrip.leon.view.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kratonsolution.skrip.leon.model.Gejala;

import io.jsondb.JsonDBTemplate;

/**
 * @author Leoni
 *
 */
@Controller
public class GejalaController {

	@Autowired
	private JsonDBTemplate db;
	
	@GetMapping("/admin/gejala-home")
	public String home(Model model) {
		
		model.addAttribute("gejalas", db.findAll(Gejala.class));
		
		return "gejala-home";
	}
}
