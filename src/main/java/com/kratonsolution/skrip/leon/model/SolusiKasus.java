package com.kratonsolution.skrip.leon.model;

public class SolusiKasus {

	private boolean enabled;
	
	private String solusiID;
	
	private String solusiNote;
	
	public SolusiKasus() {
		// TODO Auto-generated constructor stub
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getSolusiID() {
		return solusiID;
	}

	public void setSolusiID(String solusiID) {
		this.solusiID = solusiID;
	}

	public String getSolusiNote() {
		return solusiNote;
	}

	public void setSolusiNote(String note) {
		this.solusiNote = note;
	}
}
