package com.tsunderebug.speedrun4j.game;

import com.google.gson.Gson;
import com.tsunderebug.speedrun4j.Speedrun4J;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LevelList {

	private Level[] data;

	public static LevelList forGame(Game g) throws IOException {
		Gson gson = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "games/" + g.getId() + "/levels");
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.setRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		LevelList ll = gson.fromJson(r, LevelList.class);
		r.close();
		return ll;
	}
	

	public Level[] getLevels() {
		return data;
	}

}
