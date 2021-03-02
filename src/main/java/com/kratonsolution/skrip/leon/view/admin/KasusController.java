package com.kratonsolution.skrip.leon.view.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kratonsolution.skrip.leon.model.Gangguan;
import com.kratonsolution.skrip.leon.model.GangguanKasus;
import com.kratonsolution.skrip.leon.model.Gejala;
import com.kratonsolution.skrip.leon.model.GejalaKasus;
import com.kratonsolution.skrip.leon.model.Kasus;
import com.kratonsolution.skrip.leon.model.Solusi;
import com.kratonsolution.skrip.leon.model.SolusiKasus;

import io.jsondb.JsonDBTemplate;

@Controller
public class KasusController {

	private static final String HOME = "redirect:/admin/kasus-home";

	@Autowired
	private JsonDBTemplate db;

	@GetMapping("/admin/kasus-home")
	public String home(Model model) {

		model.addAttribute("kasuses", db.findAll(Kasus.class));
		return "kasus-home";
	}

	@GetMapping("/admin/kasus-preadd")
	public String preadd(Model model) {

		model.addAttribute("disruptions", db.findAll(Gangguan.class));
		model.addAttribute("symtoms", db.findAll(Gejala.class));
		model.addAttribute("solusions", db.findAll(Solusi.class));

		return "kasus-add";
	}

	@PostMapping("/admin/kasus-add")
	public String add(@RequestParam("no") Optional<Integer> no, @RequestParam("swp") Optional<String> swp,
			@RequestParam("disruptions") Optional<String[]> disruptionsOpt, 
			@RequestParam("symtoms") Optional<String[]> symtomsOpt,
			@RequestParam("solusions") Optional<String[]> solutionsOpt) {

		if(symtomsOpt.isPresent()) {

			Kasus kasus = new Kasus();
			kasus.setNumber(no.orElse(1000));
			kasus.setReparationTime(swp.orElse(null));
			
			List<String> requestSymtoms = new ArrayList<>();
			requestSymtoms.addAll(Arrays.asList(symtomsOpt.get()));

			//create tokens based on user selected symtoms
			StringBuilder tokens = new StringBuilder();
		
			db.findAll(Gejala.class).forEach(sym -> {

				GejalaKasus symtom = new GejalaKasus();
				
				if(requestSymtoms.stream().anyMatch(id -> id.equals(sym.getId()))) {

					tokens.append(sym.getOnScore());
					symtom.setEnabled(true);

				}
				else {
					tokens.append(sym.getOffScore());
				}
				
				symtom.setGejalaID(sym.getId());
				symtom.setGejalaNote(sym.getNote());
				
				kasus.getSymtoms().add(symtom);
			});
			
			kasus.setToken(tokens.toString());
			
			if(!db.findAll(Kasus.class).stream().anyMatch(ob -> ob.getToken().equals(tokens))) {
				
				List<String> requestDisruptions = new ArrayList<>();
				if(disruptionsOpt.isPresent()) {
					requestDisruptions.addAll(Arrays.asList(disruptionsOpt.get()));
				}
	
				List<String> requestSolutions = new ArrayList<>();
				if(solutionsOpt.isPresent()) {
					requestSolutions.addAll(Arrays.asList(solutionsOpt.get()));
				}
					
				db.findAll(Gangguan.class).forEach(disrupt -> {
					
					GangguanKasus disruption = new GangguanKasus();
					disruption.setGangguanID(disrupt.getId());
					disruption.setGangguanNote(disrupt.getNote());
					disruption.setEnabled(requestDisruptions.stream().anyMatch(d -> d.equals(disrupt.getId())));
					
					kasus.getDisruptions().add(disruption);
				});
				
				db.findAll(Solusi.class).forEach(solusi -> {
					
					SolusiKasus solusion = new SolusiKasus();
					solusion.setSolusiID(solusi.getId());
					solusion.setSolusiNote(solusi.getNote());
					solusion.setEnabled(requestSolutions.stream().anyMatch(s -> s.equals(solusi.getId())));
					
					kasus.getSolusions().add(solusion);
				});
				
				//if data not exist than save
				db.insert(kasus);
			}
		}

		return HOME;
	}
	
	@GetMapping("/admin/kasus-preedit")
	public String preedit(Model model, @RequestParam("id") String id) {

		model.addAttribute("kasus", db.findById(id, Kasus.class));
		return "kasus-edit";
	}

	@GetMapping("/admin/kasus-delete")
	public String delete(@RequestParam String id) {

		Kasus kasus = db.findById(id, Kasus.class);
		if(kasus != null) {
			db.remove(kasus, Kasus.class);
		}

		return HOME;
	}
}
