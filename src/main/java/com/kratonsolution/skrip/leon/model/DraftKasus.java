package com.kratonsolution.skrip.leon.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(schemaVersion= "1.0", collection = "draft-kasus")
public class DraftKasus {

	@Id
	private String id = UUID.randomUUID().toString();
	
	private int number;
	
	private double similarity;
	
	private String token;
	
	private String reparationTime;
	
	private Set<GangguanKasus> disruptions = new HashSet<>();
	
	private Set<GejalaKasus> symtoms = new HashSet<>();
	
	private Set<SolusiKasus> solusions = new HashSet<>();
	
	private boolean convertable = true;
	
	public DraftKasus() {
		// TODO Auto-generated constructor stub
	}
	
	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}


	public String getReparationTime() {
		return reparationTime;
	}

	public void setReparationTime(String reparationTime) {
		this.reparationTime = reparationTime;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public Set<GangguanKasus> getDisruptions() {
		return disruptions;
	}


	public void setDisruptions(Set<GangguanKasus> disruptions) {
		this.disruptions = disruptions;
	}


	public Set<GejalaKasus> getSymtoms() {
		return symtoms;
	}


	public void setSymtoms(Set<GejalaKasus> symtoms) {
		this.symtoms = symtoms;
	}


	public Set<SolusiKasus> getSolusions() {
		return solusions;
	}


	public void setSolusions(Set<SolusiKasus> solusions) {
		this.solusions = solusions;
	}

	public boolean isConvertable() {
		return convertable;
	}

	public Kasus toKasus() {
		
		Kasus kasus = new Kasus();
		kasus.setNumber(number);
		kasus.setToken(token);
		kasus.setReparationTime(reparationTime);
		kasus.setDisruptions(this.disruptions);
		kasus.setSymtoms(this.symtoms);
		kasus.setSolusions(this.solusions);

		this.convertable = false;
		
		return kasus;
	}
}
