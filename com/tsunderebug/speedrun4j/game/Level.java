package com.tsunderebug.speedrun4j.game;

import com.google.gson.Gson;
import com.tsunderebug.speedrun4j.Speedrun4J;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Level {

	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}


	public static Level fromID(String id) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "levels/" + id);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.setRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		LevelData cd = g.fromJson(r, LevelData.class);
		r.close();
		return cd.data;
	}
	

	private static class LevelData {
		Level data;
	}

}
