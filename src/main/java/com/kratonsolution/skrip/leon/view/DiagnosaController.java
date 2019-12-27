/**
 * 
 */
package com.kratonsolution.skrip.leon.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.jsondb.JsonDBTemplate;

/**
 * @author agung
 *
 */
@Controller
public class DiagnosaController {
	
	private JsonDBTemplate db = new JsonDBTemplate("diagnosadb", "com.kratonsolution.skrip.leon.view");
	
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
		
		Map<CharSequence, Integer> leftVector = Arrays.stream(builder.toString().split(""))
                .collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
		
		holder.addAttribute("bit", builder.toString());
		
		Diagnosa diagnosa = db.findById(builder.toString(), Diagnosa.class);
		if(diagnosa != null) {
			
			holder.addAttribute("match","100%");
			holder.addAttribute("problem", diagnosa.getProblem());
			holder.addAttribute("solusions", diagnosa.getSolusi());
			holder.addAttribute("mflag", true);
		}
		else {
			
			double buffer = 0d;
			Diagnosa obj = null;
			
			List<Diagnosa> list = db.findAll(Diagnosa.class);
			for(Diagnosa diag:list) {
				
                Map<CharSequence, Integer> rightVector = Arrays.stream(diag.getBit().split(""))
                        .collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
                
                double similarity = cosine.cosineSimilarity(leftVector, rightVector);
                if(similarity > buffer) {
                	
                	buffer = similarity;
                	obj = diag;
                }
			}
			
			if(obj != null && buffer > 50d) {
				
				holder.addAttribute("match", Double.valueOf(buffer).intValue()+"%");
				holder.addAttribute("problem", obj.getProblem());
				holder.addAttribute("solusions", obj.getSolusi());
				holder.addAttribute("mflag",false);
				
				Diagnosa diagObj = new Diagnosa();
				diagObj.setBit(builder.toString());
				diagObj.setProblem(obj.getProblem());
				diagObj.setSolusi(obj.getSolusi());
				
				db.insert(diagObj);
			}
			else {
				
				holder.addAttribute("match", "kurang dari 50%");
				holder.addAttribute("problem", "Belum diketahui");
				holder.addAttribute("mflag",false);
				
				Diagnosa diagObj = new Diagnosa();
				diagObj.setBit(builder.toString());
				diagObj.setProblem("");

				for(String s:DBStatic.SOLUSI) {
					diagObj.getSolusi().add(s);
				}

				holder.addAttribute("solusions", diagObj.getSolusi());

				
				db.insert(diagObj);
			}
		}
		
		return "hasil";
	}
}
