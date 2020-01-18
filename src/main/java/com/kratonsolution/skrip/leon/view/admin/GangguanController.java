package com.kratonsolution.skrip.leon.view.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kratonsolution.skrip.leon.model.Gangguan;

import io.jsondb.JsonDBTemplate;

@Controller
public class GangguanController {

	private static final String HOME = "redirect:/admin/gangguan-home";

	@Autowired
	private JsonDBTemplate db;
	
	@GetMapping("/admin/gangguan-home")
	public String home(Model model) {
		
		model.addAttribute("gangguans", db.findAll(Gangguan.class));
		
		return "gangguan-home";
	}
	
	@GetMapping("/admin/gangguan-preadd")
	public String preadd() {
		
		return "gangguan-add";
	}
	
	@PostMapping("/admin/gangguan-add")
	public String add(@RequestParam String note) {
		
		Gangguan gangguan = new Gangguan();
		gangguan.setNote(note);
		
		db.insert(gangguan);
		
		return HOME;
	}
	
	@GetMapping("/admin/gangguan-preedit")
	public String preedit(Model model, @RequestParam String id) {
		
		model.addAttribute("gangguan", db.findById(id, Gangguan.class));
		
		return "gangguan-edit";
	}
	
	@PostMapping("/admin/gangguan-edit")
	public String edit(@RequestParam("id") String id, @RequestParam("note")String note) {
		
		Gangguan out = db.findById(id, Gangguan.class);
		if(out != null && !out.getNote().equals(note)) {
			
			out.setNote(note);
			db.save(out, Gangguan.class);
		}
		
		return HOME;
	}
	
	@GetMapping("/admin/gangguan-delete")
	public String delete(@RequestParam String id) {
		
		Gangguan out = db.findById(id, Gangguan.class);
		if(out != null) {
			db.remove(out, Gangguan.class);
		}
		
		return HOME;
	}
}
 