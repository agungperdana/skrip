package com.kratonsolution.skrip.leon.view.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kratonsolution.skrip.leon.model.DraftKasus;
import com.kratonsolution.skrip.leon.model.Kasus;

import io.jsondb.JsonDBTemplate;

@Controller
public class DraftKasusController {

	private static final String HOME = "redirect:/admin/draft-kasus-home";

	@Autowired
	private JsonDBTemplate db;

	@GetMapping("/admin/draft-kasus-home")
	public String home(Model model) {

		model.addAttribute("kasuses", db.findAll(DraftKasus.class));
		return "draft-kasus-home";
	}

	@GetMapping("/admin/draft-kasus-preedit")
	public String preedit(Model model, @RequestParam String id) {

		model.addAttribute("kasus", db.findById(id, DraftKasus.class));
		return "draft-kasus-edit";
	}

	@PostMapping("/admin/draft-kasus-edit")
	public String edit(@RequestParam("id") String id, 
			@RequestParam("gang-en")boolean[] gangEN,
			@RequestParam("gang-id")String[] gangID,
			@RequestParam("gej-en")boolean[] gejEN,
			@RequestParam("gej-id")String[] gejID,
			@RequestParam("sol-en")boolean[] solEN,
			@RequestParam("sol-id")String[] solID) {

		DraftKasus draft = db.findById(id, DraftKasus.class);
		if(draft != null) {

			draft.getGangguans().forEach(obj -> {

				for(int idx=0;idx<gangID.length;idx++) {

					if(gangID[idx].equals(obj.getGangguanID())) {
						obj.setEnabled(gangEN[idx]);
						break;
					}
				}
			});

			draft.getGejalas().forEach(obj -> {

				for(int idx=0;idx<gejID.length;idx++) {

					if(gejID[idx].equals(obj.getGejalaID())) {
						obj.setEnabled(gejEN[idx]);
						break;
					}
				}
			});

			draft.getSolutions().forEach(obj -> {

				for(int idx=0;idx<solID.length;idx++) {

					if(solID[idx].equals(obj.getSolusiID())) {
						obj.setEnabled(solEN[idx]);
						break;
					}
				}
			});

			db.save(draft, DraftKasus.class);
		}

		return HOME;
	}
	
	@GetMapping("/admin/draft-kasus-convert")
	public String convert(@RequestParam String id) {
		
		DraftKasus draft = db.findById(id, DraftKasus.class);
		Kasus kasus = db.findById(id, Kasus.class);
		if(draft != null && kasus == null) {
			
			db.insert(draft.toKasus());
		}
		
		db.remove(draft, DraftKasus.class);
		
		return HOME;
	}

	@GetMapping("/admin/draft-kasus-delete")
	public String delete(@RequestParam String id) {

		DraftKasus draft = db.findById(id, DraftKasus.class);
		if(draft != null) {
			db.remove(draft, DraftKasus.class);
		}

		return HOME;
	}
}
