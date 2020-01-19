package com.kratonsolution.skrip.leon.view.admin;

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
	public String home() {

		return "kasus-home";
	}

	@GetMapping("/admin/kasus-preadd")
	public String preadd(Model model) {

		model.addAttribute("gangguans", db.findAll(Gangguan.class));
		model.addAttribute("gejalas", db.findAll(Gejala.class));
		model.addAttribute("solusios", db.findAll(Solusi.class));

		return "kasus-add";
	}

	@PostMapping("/admin/kasus-add")
	public String add(@RequestParam("bit") String bit, 
			@RequestParam("gang-en")boolean[] gangEN,
			@RequestParam("gang-id")String[] gangID,
			@RequestParam("gang-note")String[] gangNOTE,
			@RequestParam("gej-en")boolean[] gejEN,
			@RequestParam("gej-id")String[] gejID,
			@RequestParam("gej-note")String[] gejNOTE,
			@RequestParam("sol-en")boolean[] solEN,
			@RequestParam("sol-id")String[] solID,
			@RequestParam("sol-note")String[] solNOTE) {

		Kasus kasus = new Kasus();
		kasus.setBit(bit);

		for(int idx=0;idx<gangEN.length;idx++) {

			GangguanKasus gk = new GangguanKasus();
			gk.setEnabled(gangEN[idx]);
			gk.setGangguanID(gangID[idx]);
			gk.setGangguanNote(gangNOTE[idx]);

			kasus.getGangguans().add(gk);
		}

		for(int idx=0;idx<gejEN.length;idx++) {

			GejalaKasus gk = new GejalaKasus();
			gk.setEnabled(gejEN[idx]);
			gk.setGejalaID(gejID[idx]);
			gk.setGejalaNote(gejNOTE[idx]);

			kasus.getGejalas().add(gk);
		}

		for(int idx=0;idx<solEN.length;idx++) {

			SolusiKasus gk = new SolusiKasus();
			gk.setEnabled(solEN[idx]);
			gk.setSolusiID(solID[idx]);
			gk.setSolusiNote(solNOTE[idx]);

			kasus.getSolutions().add(gk);
		}

		db.save(kasus, Kasus.class);

		return HOME;
	}

	@GetMapping("/admin/kasus-preedit")
	public String preedit(Model model, @RequestParam String id) {

		model.addAttribute("kasus", db.findById(id, Kasus.class));
		return "kasus-edit";
	}

	@PostMapping("/admin/kasus-edit")
	public String edit(@RequestParam("id") String id, 
			@RequestParam("gang-en")boolean[] gangEN,
			@RequestParam("gang-id")String[] gangID,
			@RequestParam("gej-en")boolean[] gejEN,
			@RequestParam("gej-id")String[] gejID,
			@RequestParam("sol-en")boolean[] solEN,
			@RequestParam("sol-id")String[] solID) {

		Kasus kasus = db.findById(id, Kasus.class);
		if(kasus != null) {

			kasus.getGangguans().forEach(obj -> {

				for(int idx=0;idx<gangID.length;idx++) {

					if(gangID[idx].equals(obj.getGangguanID())) {
						obj.setEnabled(gangEN[idx]);
						break;
					}
				}
			});

			kasus.getGejalas().forEach(obj -> {

				for(int idx=0;idx<gejID.length;idx++) {

					if(gejID[idx].equals(obj.getGejalaID())) {
						obj.setEnabled(gejEN[idx]);
						break;
					}
				}
			});

			kasus.getSolutions().forEach(obj -> {

				for(int idx=0;idx<solID.length;idx++) {

					if(solID[idx].equals(obj.getSolusiID())) {
						obj.setEnabled(solEN[idx]);
						break;
					}
				}
			});

			db.save(kasus, Kasus.class);
		}

		return HOME;
	}

	public String delete(@RequestParam String id) {

		Kasus kasus = db.findById(id, Kasus.class);
		if(kasus != null) {
			db.remove(kasus, Kasus.class);
		}

		return HOME;
	}
}
