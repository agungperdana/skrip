/**
 * 
 */
package com.kratonsolution.skrip.leon.view.admin;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kratonsolution.skrip.leon.model.Gejala;

import io.jsondb.JsonDBTemplate;

/**
 * @author Leoni
 *
 */
@Controller
public class GejalaController {

	private static final String HOME = "redirect:/admin/gejala-home";
	
	@Autowired
	private JsonDBTemplate db;
	
	@GetMapping("/admin/gejala-home")
	public String home(Model model) {
		
		List<Gejala> gejalas = db.findAll(Gejala.class);
		gejalas.sort(new Comparator<Gejala>() {

			@Override
			public int compare(Gejala o1, Gejala o2) {
				
				return o1.getIndex() - o2.getIndex();
			}
		});
		
		model.addAttribute("gejalas", gejalas);
		
		return "gejala-home";
	}
	
	@GetMapping("/admin/gejala-preadd")
	public String preadd() {
		return "gejala-add";
	}
	
	@PostMapping("/admin/gejala-add")
	public String add(@RequestParam("note") String note, @RequestParam("index") int index) {
		
		Gejala gejala = new Gejala();
		gejala.setIndex(index);
		gejala.setNote(note);
		
		db.insert(gejala);
		
		return HOME;
	}
	
	@GetMapping("/admin/gejala-preedit")
	public String preedit(Model model, @RequestParam String id) {
		
		model.addAttribute("gejala", db.findById(id, Gejala.class));
		return "gejala-edit";
	}
	
	@PostMapping("/admin/gejala-edit")
	public String edit(@RequestParam("id") String id, @RequestParam("note") String note, @RequestParam("index") int index) {
		
		Gejala gejala = db.findById(id, Gejala.class);
		if(gejala != null) {
			
			gejala.setIndex(index);
			gejala.setNote(note);
			db.save(gejala, Gejala.class);
		}
		
		return HOME;
	}
	
	@GetMapping("/admin/gejala-delete")
	public String delete(@RequestParam String id) {
		
		Gejala gejala = db.findById(id, Gejala.class);
		if(gejala != null) {
			db.remove(gejala, Gejala.class);
		}
		
		return HOME;
	}
}
