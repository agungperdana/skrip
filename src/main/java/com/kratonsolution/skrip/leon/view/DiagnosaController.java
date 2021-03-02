package com.kratonsolution.skrip.leon.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kratonsolution.skrip.leon.model.DraftKasus;
import com.kratonsolution.skrip.leon.model.Gangguan;
import com.kratonsolution.skrip.leon.model.GangguanKasus;
import com.kratonsolution.skrip.leon.model.Gejala;
import com.kratonsolution.skrip.leon.model.GejalaKasus;
import com.kratonsolution.skrip.leon.model.Kasus;
import com.kratonsolution.skrip.leon.model.Solusi;
import com.kratonsolution.skrip.leon.model.SolusiKasus;

import io.jsondb.JsonDBTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Agung Dodi Perdana
 * @email agung.dodi.perdana@gmail.com
 * @version 2.0.0
 */
@Slf4j
@Controller
public class DiagnosaController {
	
	@Autowired
	private JsonDBTemplate db;
	
	private Cosine cosine = new Cosine();
	
	@GetMapping("/prepare-diagnosa")
	public String prepare() {
		return "diagnosa";
	}
	
	@PostMapping("/diagnosing")
	public String diagnose(Model holder, @RequestParam("answers") Optional<String[]> answersOpt) {

		StringBuilder tokens = new StringBuilder();
		
		List<String> answers = new ArrayList<>();
		if(answersOpt.isPresent()) {
			answers.addAll(Arrays.asList(answersOpt.get()));
		}
		
		List<Gejala> simtoms = db.findAll(Gejala.class);
		Collections.sort(simtoms, (a,b)-> a.getOnScore() - b.getOnScore());
		
		simtoms.forEach(sim -> {
			
			if(answers.stream().anyMatch(answer -> answer.equals(sim.getId()))) {
				tokens.append(sim.getOnScore());
			}
			else {
				tokens.append(sim.getOffScore());
			}
		});
	
		log.info("Start parsing user input tokens -> {}", tokens);
		Map<CharSequence, Integer> leftVector = createVector(tokens.toString());
		
		holder.addAttribute("bit", tokens.toString());
		
		Kasus kasus = db.findById(tokens.toString(), Kasus.class);
		if(kasus != null) {
		
			holder.addAttribute("match","100%");
//			holder.addAttribute("gangguans", kasus.getGangguans());
			holder.addAttribute("solusions", kasus.getSolutions());
			holder.addAttribute("mflag", true);
		}
		else {
			
			log.info("Tidak ditemukan kecocokan data");
			//Tidak ada yg cocok 100%, persiapkan data draft kasus
			DraftKasus	 draft = new DraftKasus();
			
			double buffer = 0d;
			Kasus obj = null;
			
			List<Kasus> list = db.findAll(Kasus.class);
			for(Kasus db:list) {
				
                Map<CharSequence, Integer> rightVector = createVector(db.getId());
                
                double similarity = cosine.cosineSimilarity(leftVector, rightVector);

                log.info("left {}, right {}, sim {}", leftVector, rightVector, similarity);
                
                if(similarity > buffer) {
                	
                	buffer = similarity;
                	obj = db;
                }
			}
			
			if(obj != null && buffer > 0.5d) {
				
				log.info("Kemiripan tertinggi dengan {} jumblah bit {}", obj.getId(), obj.getId().length());
				
				holder.addAttribute("match",buffer*100+"%");
//				holder.addAttribute("gangguans", obj.getGangguans());
				holder.addAttribute("solusions", obj.getSolutions());
				holder.addAttribute("mflag",false);
				
				draft.setBit(tokens.toString());
//				draft.setGangguans(obj.getGangguans());
				draft.setSolutions(obj.getSolutions());
				
				List<Gejala> gejala = getGejalas();
				
				for(int idx=0;idx<tokens.toString().length();idx++) {
					
					GejalaKasus gk = new GejalaKasus();
					gk.setEnabled(tokens.toString().charAt(idx) == '1');
					gk.setGejalaID(gejala.get(idx).getId());
					gk.setGejalaNote(gejala.get(idx).getNote());
					
					draft.getGejalas().add(gk);
				}
			}
			else {
				
				List<GangguanKasus> gangguans = new ArrayList<>();
				List<SolusiKasus> solusions = new ArrayList<>();
				
				db.findAll(Gangguan.class).forEach(gang -> {
					
					GangguanKasus gk = new GangguanKasus();
					gk.setGangguanID(gang.getId());
					gk.setGangguanNote(gang.getNote());
					
					gangguans.add(gk);
				});
				
				db.findAll(Solusi.class).forEach(sol -> {
					
					SolusiKasus ks = new SolusiKasus();
					ks.setSolusiID(sol.getId());
					ks.setSolusiNote(sol.getNote());
					
					solusions.add(ks);
				});
				
				holder.addAttribute("match", "kurang dari 50%");
				holder.addAttribute("gangguans", gangguans);
				holder.addAttribute("solusions", solusions);
				holder.addAttribute("mflag",false);
				
				draft.setBit(tokens.toString());
				draft.getGangguans().addAll(gangguans);
				draft.getSolutions().addAll(solusions);
				
				List<Gejala> gejala = getGejalas();
				
				for(int idx=0;idx<tokens.toString().length();idx++) {
					
					GejalaKasus gk = new GejalaKasus();
					gk.setEnabled(tokens.toString().charAt(idx) == '1');
					gk.setGejalaID(gejala.get(idx).getId());
					gk.setGejalaNote(gejala.get(idx).getNote());
					
					draft.getGejalas().add(gk);
				}
			}
			
			try {
				
				db.insert(draft);
			} catch (Exception e) {}
		}
		
		return "hasil";
	}
	
	private List<Gejala> getGejalas() {
		
		List<Gejala> gejalas = db.findAll(Gejala.class);
		gejalas.sort(new Comparator<Gejala>() {

			@Override
			public int compare(Gejala o1, Gejala o2) {
				// TODO Auto-generated method stub
				return o2.getOnScore() - o1.getOnScore();
			}
		});
		
		return gejalas;
	}
	
	public Map<CharSequence, Integer> createVector(String input) {
		
		log.info("input {} ", input);
		
		StringBuilder tmp = new StringBuilder();
		for(int idx=0;idx<input.length();idx++) {
			tmp.append(mask(Integer.parseInt(input.charAt(idx)+"")+(idx+1)));
		}
		
		return Arrays.stream(tmp.toString().split("")).collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
	}
	
	private String mask(int idx) {
		
		switch (idx) {
			case 1: return "A";
			case 2: return "C";
			case 3: return "D";
			case 4: return "E";
			case 5: return "F";
			case 6: return "G";
			case 7: return "H";
			case 8: return "I";
			case 9: return "J";
			case 10: return "K";
			case 11: return "L";
			case 12: return "M";
			case 13: return "N";
			case 14: return "O";
			case 15: return "P";
			case 16: return "Q";
			case 17: return "R";
			case 18: return "S";
			case 19: return "T";
			case 20: return "U";
			case 21: return "V";
			case 22: return "W";
			case 23: return "X";
			case 24: return "Y";
			case 25: return "Z";
			case 26: return "!";
			case 27: return "@";
			case 28: return "#";
			case 29: return "$";
			case 30: return "%";
			case 31: return "&";
			default: return "00";
		}
	}
}
