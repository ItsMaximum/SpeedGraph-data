package com.tsunderebug.speedrun4j.user;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.tsunderebug.speedrun4j.Speedrun4J;

public class UserList {
	private User[] data;
	public static UserList forNameAndOffset(String name, int offset) throws IOException {
		Gson g = new Gson();
		URL u = new URL(Speedrun4J.API_ROOT + "users?name=" + name + "&_bulk=yes&max=200&offset=" + offset);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		UserList ul = g.fromJson(r, UserList.class);
		r.close();
		return ul;
	}
	
	public static UserList forNameAndOffsetRandom(String name, int offset) throws IOException {
		Gson g = new Gson();
		List<String> orderList = Arrays.asList("","&orderby=signup");
		List<String> sortList = Arrays.asList("&direction=desc","");
	    Random rand = new Random();
	    String order = orderList.get(rand.nextInt(orderList.size()));
	    String sort = sortList.get(rand.nextInt(sortList.size()));
		URL u = new URL(Speedrun4J.API_ROOT + "users?name=" + name + "&_bulk=yes&max=200&offset=" + offset + order + sort);
		System.out.println(u.toString());
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("User-Agent", Speedrun4J.USER_AGENT);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		UserList ul = g.fromJson(r, UserList.class);
		r.close();
		return ul;
	}

	public User[] getUsers() {
		return data;
	}
}
