package com.tsunderebug.speedrun4j;

public class Speedrun4J {

	private Speedrun4J(){}

	public static int proxyNum = 0;
	public static final String USER_AGENT = "Speedrun4j/1.0";
	public static final String API_KEY = "";
	
	public static String getAPI_ROOT() {
		return "https://www.speedrun.com/api/v1/";
	}
}
