package com.kratonsolution.skrip.leon.model;

import java.util.UUID;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(schemaVersion= "1.0", collection = "solusi")
public class Solusi {

	@Id
	private String id = UUID.randomUUID().toString();

	private String note;
	
	public Solusi() {
		// TODO Auto-generated constructor stub
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
