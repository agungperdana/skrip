package com.kratonsolution.skrip.leon.view;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kratonsolution.skrip.leon.model.Gejala;

import io.jsondb.JsonDBTemplate;

/**
 * @author Agung Dodi Perdana
 * @email agung.dodi.perdana@gmail.com
 * @version 2.0.0
 */
@Controller
public class LandingPage {

	@Autowired
	private JsonDBTemplate temp;
	
	@GetMapping("/")
	public String landing(Model model) {
		
		List<Gejala> quetions = temp.findAll(Gejala.class);
		Collections.sort(quetions,(a, b)-> a.getOnScore() - b.getOnScore());
		
		model.addAttribute("quetions", quetions);
		return "index";
	}
}
