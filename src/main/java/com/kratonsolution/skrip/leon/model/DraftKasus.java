package com.kratonsolution.skrip.leon.model;

import java.util.HashSet;
import java.util.Set;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(schemaVersion= "1.0", collection = "draft-kasus")
public class DraftKasus {

	@Id
	private String bit;
	
	private Set<GangguanKasus> gangguans = new HashSet<>();
	
	private Set<GejalaKasus> gejalas = new HashSet<>();
	
	private Set<SolusiKasus> solutions = new HashSet<>();
	
	public DraftKasus() {
		// TODO Auto-generated constructor stub
	}

	public Set<GangguanKasus> getGangguans() {
		return gangguans;
	}

	public void setGangguans(Set<GangguanKasus> gangguans) {
		this.gangguans = gangguans;
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
	
	public Kasus toKasus() {
		
		Kasus kasus = new Kasus();
		kasus.setId(this.bit);
//		kasus.setGangguans(this.gangguans);
		kasus.setSymtoms(this.gejalas);
		kasus.setSolusions(this.solutions);
		
		
		return kasus;
	}
}
