package com.kratonsolution.skrip.leon.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

		String inputTokens = createTokens(answersOpt);

		Map<CharSequence, Integer> leftVector = createVector(inputTokens);

		holder.addAttribute("tokens", inputTokens);

		Kasus kasus = db.findOne(String.format("/.[token='%s']", inputTokens), Kasus.class);
		if(kasus != null) {

			holder.addAttribute("match","100%");
			holder.addAttribute("disruptions", kasus.getDisruptions());
			holder.addAttribute("solusions", kasus.getSolusions());
			holder.addAttribute("reparationTime", kasus.getReparationTime());
		}
		else if(db.findOne(String.format("/.[token='%s']", inputTokens), DraftKasus.class) != null) {
			
			DraftKasus draft = db.findOne(String.format("/.[token='%s']", inputTokens), DraftKasus.class);
			
			holder.addAttribute("match", draft.getSimilarity()*100+"%");
			holder.addAttribute("disruptions", draft.getDisruptions());
			holder.addAttribute("solusions", draft.getSolusions());
			holder.addAttribute("reparationTime", draft.getReparationTime());
		}
		else {

			log.info("Tidak ditemukan kecocokan data");
			DraftKasus draft = new DraftKasus();

			double buffer = 0d;
			Kasus obj = null;

			List<Kasus> list = db.findAll(Kasus.class);
			for(Kasus db:list) {

				Map<CharSequence, Integer> rightVector = createVector(db.getToken());

				double similarity = cosine.cosineSimilarity(leftVector, rightVector);

				log.info("left {}, right {}, sim {}", leftVector, rightVector, similarity);

				if(similarity > buffer) {

					buffer = similarity;
					obj = db;
				}
			}

			if(obj != null && buffer > 0.5d) {

				holder.addAttribute("match", buffer*100+"%");
				holder.addAttribute("gangguans", obj.getDisruptions());
				holder.addAttribute("solusions", obj.getSolusions());
				holder.addAttribute("reparationTime", obj.getReparationTime());

				draft.setNumber(obj.getNumber());
				draft.setSimilarity(buffer);
				draft.setDisruptions(obj.getDisruptions());
				draft.setSolusions(obj.getSolusions());
				draft.setToken(inputTokens);
				draft.setReparationTime(obj.getReparationTime());

				String tokens[] = asArray(inputTokens);

				getGejalas().forEach(symtom -> {

					GejalaKasus gk = new GejalaKasus();
					gk.setGejalaID(symtom.getId());
					gk.setGejalaNote(symtom.getNote());
					gk.setEnabled(Arrays.asList(tokens).stream().anyMatch(t->t.equals(symtom.getOnScore()+"")));

					draft.getSymtoms().add(gk);
				});
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
				holder.addAttribute("disruptions", gangguans);
				holder.addAttribute("solusions", solusions);
				holder.addAttribute("reparationTime", "Belum diketahui");

				draft.setNumber(0);
				draft.setSimilarity(buffer);
				draft.setToken(inputTokens);
				draft.getDisruptions().addAll(gangguans);
				draft.getSolusions().addAll(solusions);

				String tokens[] = asArray(inputTokens);
				
				getGejalas().forEach(symtom -> {

					GejalaKasus gk = new GejalaKasus();
					gk.setGejalaID(symtom.getId());
					gk.setGejalaNote(symtom.getNote());
					gk.setEnabled(Arrays.asList(tokens).stream().anyMatch(t->t.equals(symtom.getOnScore()+"")));

					draft.getSymtoms().add(gk);
				});
			}

			try {

				db.insert(draft);
			} catch (Exception e) {}
		}

		return "hasil";
	}

	private String createTokens(Optional<String[]> answersOpt) {

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

		return tokens.toString();
	}

	private List<Gejala> getGejalas() {

		List<Gejala> gejalas = db.findAll(Gejala.class);
		Collections.sort(gejalas, (a,b) -> a.getOnScore()-b.getOnScore());

		return gejalas;
	}

	public Map<CharSequence, Integer> createVector(String input) {

		String tokens[] = asArray(input);

		return Arrays.stream(tokens).collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
	}

	private String[] asArray(String input) {
		return input.split("(?<=\\G..)");
	}
}
