/**
 * 
 */
package com.kratonsolution.skrip.leon.model;

/**
 * @author Agung
 *
 */
public class GejalaKasus {

	private boolean enabled;
	
	private String gejalaID;
	
	private String note;
	
	public GejalaKasus() {
		// TODO Auto-generated constructor stub
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getGejalaID() {
		return gejalaID;
	}

	public void setGejalaID(String gejalaID) {
		this.gejalaID = gejalaID;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
