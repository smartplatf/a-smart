package org.anon.smart.generator.flowgen;

import java.util.List;

public class FlowDescription {

	private String name;
	private List<String> defaultEnable;
	private List<String> primeData;
	private List<String> data;
	private List<KeysAttributes> keys;
	private List<FeatureAttributes> features;

	public FlowDescription(String name, List<String> defaultEnable,
			List<String> primeData, List<String> data, List<KeysAttributes> keys) {
		super();
		this.name = name;
		this.defaultEnable = defaultEnable;
		this.primeData = primeData;
		this.data = data;
		this.keys = keys;
	}

	
	public FlowDescription() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDefaultEnable() {
		return defaultEnable;
	}

	public void setDefaultEnable(List<String> defaultEnable) {
		this.defaultEnable = defaultEnable;
	}

	public List<String> getPrimeData() {
		return primeData;
	}

	public void setPrimeData(List<String> primeData) {
		this.primeData = primeData;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

	public List<KeysAttributes> getKeys() {
		return keys;
	}

	public void setKeys(List<KeysAttributes> keys) {
		this.keys = keys;
	}

	public List<FeatureAttributes> getFeatures() {
		return features;
	}

	public void setFeatures(List<FeatureAttributes> features) {
		this.features = features;
	}

}
