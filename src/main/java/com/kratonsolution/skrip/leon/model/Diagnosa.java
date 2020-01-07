/**
 * 
 */
package com.kratonsolution.skrip.leon.model;

import java.util.ArrayList;
import java.util.List;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

/**
 * @author Leoni
 *
 */
@Document(schemaVersion= "1.0", collection = "diagnosa")
public class Diagnosa {
	
	@Id
	private String bit;
	
	private String problem;
	
	private List<String> solusi = new ArrayList<>();
	
	public Diagnosa() {
		// TODO Auto-generated constructor stub
	}

	public String getBit() {
		return bit;
	}

	public void setBit(String bit) {
		this.bit = bit;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public List<String> getSolusi() {
		return solusi;
	}

	public void setSolusi(List<String> solusi) {
		this.solusi = solusi;
	}
}
