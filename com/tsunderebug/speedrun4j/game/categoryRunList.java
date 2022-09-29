package com.tsunderebug.speedrun4j.game;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.tsunderebug.speedrun4j.game.run.PlacedRun;
import com.tsunderebug.speedrun4j.game.run.Run;
import com.tsunderebug.speedrun4j.game.run.Timeset;

public class categoryRunList {
	
	private Game game;
	private Category category;
	private List<Subcategory> subcatlist;
	private LinkedHashMap<String, String> subCatValues;
	private String name;
	private Leaderboard leaderboard;
	private int numWrs;
	private int numRuns;
	private int numRunners;
	
	public categoryRunList(Game game, Category category, String name, Leaderboard leaderboard, ArrayList<Run> runs) {
		this.name = name.replaceAll(",", ".");
		this.leaderboard = leaderboard;
		this.numWrs = getNumWrs(runs);
		this.numRuns = runs.size();
		this.numRunners = this.leaderboard.getRuns().length;
		this.updateRunValues();
	}
	
	public categoryRunList(Game game, Category category, List<Subcategory> subcatlist, LinkedHashMap<String, String> subCatValues, String name, Leaderboard leaderboard, ArrayList<Run> runs) {
		this.name = name.replaceAll(",", ".");
		this.leaderboard = leaderboard;
		this.numWrs = getNumWrs(runs);
		this.numRuns = runs.size();
		this.numRunners = this.leaderboard.getRuns().length;
		this.updateRunValues();
	}
	
	public double getRunValue(int place) {
		if(place == 0 || numRuns == 0 || place == numRunners)
			return 0.0;
		double placeWeight = Math.log(this.numRuns) / Math.log(place + 0.5);
		double numWrsWeight = -1 * (1.0 * numWrs/numRunners) * (place - 1) + numWrs;
		return placeWeight * numWrsWeight;
	}
	
	public void updateRunValues() {
		for(PlacedRun pr : this.leaderboard.getRuns()) {
			pr.setRunValue(this.getRunValue(pr.getPlace()));
		}
	}
	
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Subcategory> getSubcatlist() {
		return subcatlist;
	}

	public void setSubcatlist(List<Subcategory> subcatlist) {
		this.subcatlist = subcatlist;
	}

	public LinkedHashMap<String, String> getSubCatValues() {
		return subCatValues;
	}

	public void setSubCatValues(LinkedHashMap<String, String> subCatValues) {
		this.subCatValues = subCatValues;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLeaderboard(Leaderboard leaderboard) {
		this.leaderboard = leaderboard;
	}


	public String getName() {
		return name;
	}
	public Leaderboard getLeaderboard() {
		return leaderboard;
	}
	public int getNumWrs() {
		return numWrs;
	}

	public void setNumWrs(int numWrs) {
		this.numWrs = numWrs;
	}

	public int getNumRuns() {
		return numRuns;
	}

	public void setNumRuns(int numRuns) {
		this.numRuns = numRuns;
	}

	
	public static int getNumWrs(ArrayList<Run> r) {
		ArrayList<Run> wrs = new ArrayList<Run>();
		ArrayList<Run> runs = new ArrayList<Run>(r);
		double currentWR = 100000000;
		for(int i=0; i<runs.size(); i++) {
			double time;
			Timeset ts = runs.get(i).getTimes();
			if (ts.getRealtimeNoloadsT() > 0){
				time = ts.getRealtimeNoloadsT();
			} else if(ts.getIngameT() > 0) {
				time = ts.getIngameT();
			} else {
				time = ts.getRealtimeT();
			}
			if(runs.get(i).getDate() != null && time < currentWR) {
				currentWR = time;
				wrs.add(runs.get(i));
			}
		}
		return wrs.size();
	}
}
