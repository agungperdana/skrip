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

	private String bit;
	
	private String gangguanID;
	
	private String gangguanNote;
	
	private Set<GejalaKasus> gejalas = new HashSet<>();
	
	private Set<SolusiKasus> solutions = new HashSet<>();
	
	public Kasus() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBit() {
		return bit;
	}

	public void setBit(String bit) {
		this.bit = bit;
	}

	public Set<GejalaKasus> getGejalas() {
		return gejalas;
	}

	public void setGejalas(Set<GejalaKasus> gejalas) {
		this.gejalas = gejalas;
	}

	public Set<SolusiKasus> getSolutions() {
		return solutions;
	}

	public void setSolutions(Set<SolusiKasus> solutions) {
		this.solutions = solutions;
	}
}
