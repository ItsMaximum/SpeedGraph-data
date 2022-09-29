package com.tsunderebug.speedrun4j.game.run;

import com.google.gson.Gson;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j.game.Category;
import com.tsunderebug.speedrun4j.game.Level;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;

public class RunList {

	private Run[] data;
	public static RunList forCat(Category cat) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "runs?category=" + cat.getId() + "&orderby=date" + "&status=verified" + "&_bulk=yes&max=500");
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		RunList l = g.fromJson(r, RunList.class);
		r.close();
		return l;
	}
	public static RunList forCatAndLevel(Category cat, Level lev) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "runs?category=" + cat.getId() + "&orderby=date" + "&level=" + lev.getId() + "&status=verified" + "&_bulk=yes&max=200");
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		RunList l = g.fromJson(r, RunList.class);
		r.close();
		return l;
	}
	public static RunList forCatAndPlayer(Category cat, String pid) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "runs?category=" + cat.getId() + "&orderby=date" + "&user=" + pid + "&status=verified" + "&_bulk=yes&max=200");
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		RunList l = g.fromJson(r, RunList.class);
		r.close();
		return l;
	}
	public static RunList forCatAndDate(Category cat, String date) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "runs?category=" + cat.getId() + "&orderby=date" + "&date=" + date + "&status=verified" + "&_bulk=yes&max=200");
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		RunList l = g.fromJson(r, RunList.class);
		r.close();
		return l;
	}
	public static RunList forCatAndGuest(Category cat, String guestName) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "runs?category=" + cat.getId() + "&orderby=date" + "&guest=" + guestName + "&status=verified" + "&_bulk=yes&max=200");
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		RunList l = g.fromJson(r, RunList.class);
		r.close();
		return l;
	}
	public static RunList forCatAndOffset(Category cat, int offset) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "runs?category=" + cat.getId() + "&orderby=date" + "&status=verified" + "&offset=" + offset + "&max=200");
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		RunList l = g.fromJson(r, RunList.class);
		r.close();
		return l;
	}
	public static RunList forCatAndOffsetAll(Category cat, int offset) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "runs?category=" + cat.getId() + "&orderby=date" + "&offset=" + offset + "&max=200");
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		RunList l = g.fromJson(r, RunList.class);
		r.close();
		return l;
	}
	public static RunList forCatAndOffsetAndDate(Category cat, int offset, String date) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "runs?category=" + cat.getId() + "&orderby=date" + "&status=verified" + "&offset=" + offset + "&max=200" + "&date=" + date);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		RunList l = g.fromJson(r, RunList.class);
		r.close();
		return l;
	}
	public static RunList forStatus(Category cat, String status) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "runs?category=" + cat.getId() + "&status=" + status);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		RunList l = g.fromJson(r, RunList.class);
		r.close();
		return l;
	}
	public static RunList forSubCats(Category cat, String subcats) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "runs?category=" + cat.getId() + subcats + "&_bulk=yes&max=1000");
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		RunList l = g.fromJson(r, RunList.class);
		r.close();
		return l;
	}
	public static RunList forCatAndOffsetAndSubCat(Category cat, int offset, LinkedHashMap<String,String> subcats) throws IOException {
		Gson g = new Gson();
		String sc = "";
		if(subcats.keySet().size() > 0) {
			sc = "&var-" + subcats.toString();
			sc = sc.replace("{", "");
			sc = sc.replace("}", "");
			sc = sc.replace(", ", "&var-");
		}
		System.out.println(Speedrun4J.API_ROOT + "runs?category=" + cat.getId() + "&orderby=date" + "&status=verified" + "&offset=" + offset + "&max=200" + sc);
		URL u = new URL(Speedrun4J.API_ROOT + "runs?category=" + cat.getId() + "&orderby=date" + "&status=verified" + "&offset=" + offset + "&max=200" + sc);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		RunList l = g.fromJson(r, RunList.class);
		r.close();
		return l;
	}

	public Run[] getRuns() {
		return data;
	}

}
