package com.tsunderebug.speedrun4j.platform;

import com.google.gson.Gson;
import com.tsunderebug.speedrun4j.Speedrun4J;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Platform {

	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}


	public static Platform fromID(String id) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "platforms/" + id);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.setRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		PlatformData cd = g.fromJson(r, PlatformData.class);
		r.close();
		return cd.data;
	}
	

	private static class PlatformData {
		Platform data;
	}

}
