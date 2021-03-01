package com.kratonsolution.skrip.leon.model;

import java.util.UUID;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(schemaVersion= "1.0", collection = "gejala")
public class Gejala {

	@Id
	private String id = UUID.randomUUID().toString();

	private int onScore;
	
	private int offScore;
	
	private String note;
	
	public Gejala() {
		// TODO Auto-generated constructor stub
	}

	public int getOffScore() {
		return offScore;
	}

	public void setOffScore(int offScore) {
		this.offScore = offScore;
	}

	public int getOnScore() {
		return onScore;
	}

	public void setOnScore(int index) {
		this.onScore = index;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
