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
	
	private int number;
	
	private String reparationTime;
	
	private String token;
	
	private Set<GangguanKasus> disruptions = new HashSet<>();
	
	private Set<GejalaKasus> symtoms = new HashSet<>();
	
	private Set<SolusiKasus> solusions = new HashSet<>();
	
	public Kasus() {
	}

	public String getId() {
		return id;
	}

	public void setId(String bit) {
		this.id = bit;
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

	public void setDisruptions(Set<GangguanKasus> disruption) {
		this.disruptions = disruption;
	}

	public Set<GejalaKasus> getSymtoms() {
		return symtoms;
	}

	public void setSymtoms(Set<GejalaKasus> gejalas) {
		this.symtoms = gejalas;
	}

	public Set<SolusiKasus> getSolusions() {
		return solusions;
	}

	public void setSolusions(Set<SolusiKasus> solutions) {
		this.solusions = solutions;
	}
}
