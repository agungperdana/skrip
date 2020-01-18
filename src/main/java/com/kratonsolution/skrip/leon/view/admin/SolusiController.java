/**
 * 
 */
package com.kratonsolution.skrip.leon.view.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kratonsolution.skrip.leon.model.Solusi;

import io.jsondb.JsonDBTemplate;

/**
 * @author Leoni
 *
 */
@Controller
public class SolusiController {

	private static final String HOME = "redirect:/admin/solusi-home";
	
	@Autowired
	private JsonDBTemplate db;
	
	@GetMapping("/admin/solusi-home")
	public String home(Model model) {
		
		model.addAttribute("solusis", db.findAll(Solusi.class));
		
		return "solusi-home";
	}
	
	@GetMapping("/admin/solusi-preadd")
	public String preadd() {
		return "solusi-add";
	}
	
	@PostMapping("/admin/solusi-add")
	public String add(@RequestParam String note) {
		
		Solusi solusi = new Solusi();
		solusi.setNote(note);
		
		db.insert(solusi);
		
		return HOME;
	}
	
	@GetMapping("/admin/solusi-preedit")
	public String preedit(Model model, @RequestParam String id) {
		
		model.addAttribute("solusi", db.findById(id, Solusi.class));
		return "solusi-edit";
	}
	
	@PostMapping("/admin/solusi-edit")
	public String edit(@RequestParam("id") String id, @RequestParam("note") String note) {
		
		Solusi solusi = db.findById(id, Solusi.class);
		if(solusi != null) {
			
			solusi.setNote(note);
			db.save(solusi, Solusi.class);
		}
		
		return HOME;
	}
	
	@GetMapping("/admin/solusi-delete")
	public String delete(@RequestParam String id) {
		
		Solusi solusi = db.findById(id, Solusi.class);
		if(solusi != null) {
			db.remove(solusi, Solusi.class);
		}
		
		return HOME;
	}
}
