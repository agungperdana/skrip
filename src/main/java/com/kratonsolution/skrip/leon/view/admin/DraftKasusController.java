package com.kratonsolution.skrip.leon.view.admin;

import java.util.Arrays;
import java.util.Optional;

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
			@RequestParam("no") int number,
			@RequestParam("pwp") String pwp,
			@RequestParam("symtoms") Optional<String[]> symtomsOpt,
			@RequestParam("disruptions") Optional<String[]> disruptions,
			@RequestParam("solusions") Optional<String[]> solusions){

		DraftKasus draft = db.findById(id, DraftKasus.class);
		if(draft != null) {

			draft.setNumber(number);
			draft.setReparationTime(pwp);
			
			draft.getSymtoms().forEach(symtom -> {
				symtom.setEnabled(symtomsOpt.isPresent() && Arrays.asList(symtomsOpt.get()).stream().anyMatch(p->p.equals(symtom.getGejalaID())));				

			});
			draft.getDisruptions().forEach(disrup->{
				disrup.setEnabled(disruptions.isPresent() && Arrays.asList(disruptions.get()).stream().anyMatch(p->p.equals(disrup.getGangguanID())));
			});
			
			draft.getSolusions().forEach(solusion -> {
				solusion.setEnabled(solusions.isPresent() && Arrays.asList(solusions.get()).stream().anyMatch(p->p.equals(solusion.getSolusiID())));
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
