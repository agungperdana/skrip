package com.kratonsolution.skrip.leon.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
 * @author Leoni
 *
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
	
	@PostMapping("/do-diagnosa")
	public String diagnose(Model holder,
			@RequestParam("q1")String g1, 
			@RequestParam("q2")String g2,
			@RequestParam("q3")String g3,
			@RequestParam("q4")String g4,
			@RequestParam("q5")String g5,
			@RequestParam("q6")String g6,
			@RequestParam("q7")String g7,
			@RequestParam("q8")String g8,
			@RequestParam("q9")String g9,
			@RequestParam("q10")String g10,
			@RequestParam("q11")String g11,
			@RequestParam("q12")String g12,
			@RequestParam("q13")String g13,
			@RequestParam("q14")String g14,
			@RequestParam("q15")String g15,
			@RequestParam("q16")String g16,
			@RequestParam("q17")String g17,
			@RequestParam("q18")String g18,
			@RequestParam("q19")String g19,
			@RequestParam("q20")String g20,
			@RequestParam("q21")String g21,
			@RequestParam("q22")String g22,
			@RequestParam("q23")String g23,
			@RequestParam("q24")String g24,
			@RequestParam("q25")String g25,
			@RequestParam("q26")String g26,
			@RequestParam("q27")String g27,
			@RequestParam("q28")String g28,
			@RequestParam("q29")String g29,
			@RequestParam("q30")String g30,
			@RequestParam("q31")String g31) {

		StringBuilder builder = new StringBuilder();
		builder.append(g1).append(g2).append(g3).append(g4).append(g5)
				.append(g6).append(g7).append(g8).append(g9).append(g10)
				.append(g11)
				.append(g12)
				.append(g13)
				.append(g14)
				.append(g15)
				.append(g16)
				.append(g17)
				.append(g18)
				.append(g19)
				.append(g20)
				.append(g21)
				.append(g22)
				.append(g23)
				.append(g24)
				.append(g25)
				.append(g26)
				.append(g27)
				.append(g28)
				.append(g29)
				.append(g30)
				.append(g31);

		log.info("Start parsing user input -> ");
		Map<CharSequence, Integer> leftVector = createVector(builder.toString());
		
		holder.addAttribute("bit", builder.toString());
		
		Kasus kasus = db.findById(builder.toString(), Kasus.class);
		if(kasus != null) {
		
			holder.addAttribute("match","100%");
			holder.addAttribute("gangguans", kasus.getGangguans());
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
				
                Map<CharSequence, Integer> rightVector = createVector(db.getBit());
                
                double similarity = cosine.cosineSimilarity(leftVector, rightVector);

                log.info("left {}, right {}, sim {}", leftVector, rightVector, similarity);
                
                if(similarity > buffer) {
                	
                	buffer = similarity;
                	obj = db;
                }
			}
			
			if(obj != null && buffer > 0.5d) {
				
				log.info("Kemiripan tertinggi dengan {} jumblah bit {}", obj.getBit(), obj.getBit().length());
				
				holder.addAttribute("match",buffer*100+"%");
				holder.addAttribute("gangguans", obj.getGangguans());
				holder.addAttribute("solusions", obj.getSolutions());
				holder.addAttribute("mflag",false);
				
				draft.setBit(builder.toString());
				draft.setGangguans(obj.getGangguans());
				draft.setSolutions(obj.getSolutions());
				
				List<Gejala> gejala = getGejalas();
				
				for(int idx=0;idx<builder.toString().length();idx++) {
					
					GejalaKasus gk = new GejalaKasus();
					gk.setEnabled(builder.toString().charAt(idx) == '1');
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
				
				draft.setBit(builder.toString());
				draft.getGangguans().addAll(gangguans);
				draft.getSolutions().addAll(solusions);
				
				List<Gejala> gejala = getGejalas();
				
				for(int idx=0;idx<builder.toString().length();idx++) {
					
					GejalaKasus gk = new GejalaKasus();
					gk.setEnabled(builder.toString().charAt(idx) == '1');
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
				return o2.getIndex() - o1.getIndex();
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
