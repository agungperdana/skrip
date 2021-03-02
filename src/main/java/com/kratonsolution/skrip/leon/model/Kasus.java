package com.kratonsolution.skrip.leon.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(schemaVersion= "1.0", collection = "kasus")
public class Kasus {

	@Id
	private String id = UUID.randomUUID().toString();
	
	private String token;
	
	private Set<GangguanKasus> disruption = new HashSet<>();
	
	private Set<GejalaKasus> symtoms = new HashSet<>();
	
	private Set<SolusiKasus> solutions = new HashSet<>();
	
	public Kasus() {
	}

	public String getId() {
		return id;
	}

	public void setId(String bit) {
		this.id = bit;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Set<GangguanKasus> getDisruption() {
		return disruption;
	}

	public void setDisruption(Set<GangguanKasus> disruption) {
		this.disruption = disruption;
	}

	public Set<GejalaKasus> getSymtoms() {
		return symtoms;
	}

	public void setSymtoms(Set<GejalaKasus> gejalas) {
		this.symtoms = gejalas;
	}

	public Set<SolusiKasus> getSolutions() {
		return solutions;
	}

	public void setSolutions(Set<SolusiKasus> solutions) {
		this.solutions = solutions;
	}
}
