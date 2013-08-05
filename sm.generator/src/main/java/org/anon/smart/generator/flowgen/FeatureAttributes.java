package org.anon.smart.generator.flowgen;

import java.util.List;

public class FeatureAttributes {
	
	private String name;
	private List<String> artefacts;
	
	public FeatureAttributes(String name, List<String> artefacts) {
		super();
		this.name = name;
		this.artefacts = artefacts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getArtefacts() {
		return artefacts;
	}

	public void setArtefacts(List<String> artefacts) {
		this.artefacts = artefacts;
	}

	public FeatureAttributes() {
		// TODO Auto-generated constructor stub
	}

}
