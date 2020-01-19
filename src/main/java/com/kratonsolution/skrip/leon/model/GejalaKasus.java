/**
 * 
 */
package com.kratonsolution.skrip.leon.model;

/**
 * @author Leoni
 *
 */
public class GejalaKasus {

	private boolean enabled;
	
	private String gejalaID;
	
	private String gejalaNote;
	
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

	public String getGejalaNote() {
		return gejalaNote;
	}

	public void setGejalaNote(String gejalaNote) {
		this.gejalaNote = gejalaNote;
	}
}
