package com.tsunderebug.speedrun4j.game;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.tsunderebug.speedrun4j.Speedrun4J;

public class Series {
	public static Series fromID(String id) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "series/" + id);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.setRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		SeriesData series = g.fromJson(r, SeriesData.class);
		r.close();
		return series.data;
	}
	private static class SeriesData {
		Series data;
	}
	

}
