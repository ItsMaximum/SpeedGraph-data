package com.tsunderebug.speedrun4j.game;

import java.util.Map;

public class Subcategory {
	private String id;
	private String name;
	private Map<String,String> values;
	public Subcategory(String id, String name, Map<String,String> values) {
		this.id = id;
		this.name = name;
		this.values = values;
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public Map<String,String> getValues() {
		return values;
	}
	public String[] getValueIds() {
		return values.keySet().toArray(new String[0]);
	}
	public String[] getValueNames() {
		return values.values().toArray(new String[0]);
	}
				
}
