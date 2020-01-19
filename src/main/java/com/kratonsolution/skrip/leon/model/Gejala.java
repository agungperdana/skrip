package com.kratonsolution.skrip.leon.model;

import java.util.UUID;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(schemaVersion= "1.0", collection = "gejala")
public class Gejala {

	@Id
	private String id = UUID.randomUUID().toString();

	private int index;
	
	private String note;
	
	public Gejala() {
		// TODO Auto-generated constructor stub
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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
