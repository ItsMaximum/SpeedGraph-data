package com.tsunderebug.speedrun4j.game;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.tsunderebug.speedrun4j.Speedrun4J;

public class VariableList {
	private Variable[] data;

	public static VariableList fromCategory(Category ca) throws IOException {
		Gson gson = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "categories/" + ca.getId() + "/variables");
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.setRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		VariableList vl = gson.fromJson(r, VariableList.class);
		r.close();
		return vl;
	}

	public Variable[] getVariables() {
		return data;
	}
	
	public List<Subcategory> getSubcategories() {
		List<Subcategory> output = new ArrayList<Subcategory>();
		for(Variable v : this.getVariables()) {
			if(v.getIsSubcategory()) {
				Map<String,Object> values = v.getValues();
				Map<String,String> choices = (Map<String, String>) values.get(values.keySet().toArray()[1]);
				output.add(new Subcategory(v.getID(), v.getName(), choices));
			}
		}
		return output;
	}
}
