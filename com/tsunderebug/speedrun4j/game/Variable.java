package com.tsunderebug.speedrun4j.game;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class Variable {
	private String id;
	private String name;
	@SerializedName("is-subcategory") private boolean issubcategory;
	private Map<String, Object> values;
	private Map<String,String> possValues;
	
	public String getID() {
		return id;
	}
	public String getName() {
		return name;
	}
	
	public boolean getIsSubcategory() {
		return issubcategory;
	}
	
	public Map<String, Object> getValues() {
		return values;
	}
	
	public Map<String, String> getReadableValues() {
		Map<String,Object> values = this.getValues();
		return (Map<String, String>) values.get(values.keySet().toArray()[1]);
	}
}
