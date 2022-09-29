import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.game.Category;
import com.tsunderebug.speedrun4j.game.Game;
import com.tsunderebug.speedrun4j.game.Level;
import com.tsunderebug.speedrun4j.game.Subcategory;
import com.tsunderebug.speedrun4j.game.VariableList;
import com.tsunderebug.speedrun4j.game.run.Player;
import com.tsunderebug.speedrun4j.game.run.Run;
import com.tsunderebug.speedrun4j.game.run.RunList;

public class topTensFlourish {
	
	public static final String GAME_ID = "fpa2";
	public static final String CATEGORY_NAME = "Any%";
	public static final int NTH_DAY = 7;
	public static final String[] SUBCATEGORIES = {"No Major Glitches"};
	public static final String level = "";
	
	public static void main(String[] args) throws IOException {
		boolean isLevel = level.length() > 0;
		System.out.println("Is a level category: " + isLevel);
		
		// Turn given game id and category name into a category id
		String FILE_NAME = (GAME_ID + "_" + CATEGORY_NAME + "_" + Arrays.toString(SUBCATEGORIES)).replaceAll("[\\\\/:*?\"<>|]", "");
		Game g = Game.fromID(GAME_ID);
		LinkedHashMap<String,Category> categories = new LinkedHashMap<String,Category>();
		for(Category c: g.getCategories().getCategories()) {
			if(isLevel == c.getType().equals("per-level"))
				categories.put(c.getName(), c);
		}
		if(categories.get(CATEGORY_NAME) == null) {
			System.out.println("Category name was not valid!");
			System.exit(0);
		} 
		
		// Get subcategories ids if subcategories are provided
		Category c = Category.fromID(categories.get(CATEGORY_NAME).getId());
		VariableList vl = VariableList.fromCategory(c);
		LinkedHashMap<String,String> chosenSubCatIds = new LinkedHashMap<String,String>();
		
		for(int i=0; i<SUBCATEGORIES.length; i++) {
			Subcategory sc = vl.getSubcategories().get(i);
			String[] names = sc.getValueNames();
			int index = Arrays.asList(names).indexOf(SUBCATEGORIES[i]);
			if(index == -1) {
				System.out.println("Invalid subcategory name!");
				System.exit(0);
			}
			chosenSubCatIds.put(sc.getId(), sc.getValueIds()[index]);
		}
		
		int numSubCats = chosenSubCatIds.keySet().size();
		
		ArrayList<Run> allRuns = new ArrayList<Run>();
		ArrayList<String> wrRunDates = new ArrayList<String>();
		LinkedHashMap<String,String> playerNames = new LinkedHashMap<String,String>();
		LinkedHashMap<String,LinkedHashMap<String,String>> timeData = new LinkedHashMap<String,LinkedHashMap<String,String>>();
		LinkedHashMap<String,String> playerImages = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> playerColors = new LinkedHashMap<String,String>();
		//sets the offset to the number of runs with an invalid date
		int offset = 0;
		int count = 0;
		
		//Add all of the runs to the allRuns arraylist using pagination
		while(true) {
			count = 0;
			for(Run r: RunList.forCatAndOffset(c,offset).getRuns()) {
				//Check if the subcategories have been provided
				if(numSubCats > 0) {
					Map<String,String> values = r.getValues();
					Iterator<String> keys = chosenSubCatIds.keySet().iterator();
					//Check if all subcategories match the ones provided
					while(keys.hasNext()) {
						String s = keys.next();
						if(!values.get(s).equals(chosenSubCatIds.get(s))) {
							break;
						}
						if(!keys.hasNext()) {
							if(!isLevel || Level.fromID(r.getLevel()).getName().equals(level)) {
								allRuns.add(r);
							}
						}
					}
					//Check if level matches the one provided
				} else {
					if(!isLevel || Level.fromID(r.getLevel()).getName().equals(level)) {
						allRuns.add(r);
					}
				}
				count++;
			}
			if(count<200)
				break;
			offset+=count;
		}
		
		//Remove runs with no date
		System.out.println(allRuns.size());
		Iterator<Run> runItr = allRuns.iterator();
	    while (runItr.hasNext()) {
	      Run r = runItr.next();
	      if (r.getDate() == null) {
	        runItr.remove();
	      }
	    }
	    System.out.println(allRuns.size());
		
		String startDate = allRuns.get(0).getDate();
		LocalDate currentDate = LocalDate.parse(startDate);
		
		while(true) {
			ArrayList<Run> runsByDate = new ArrayList<Run>();
			ArrayList<String> playerLinks = new ArrayList<String>();
			for(Run r : allRuns) {
				if(LocalDate.parse(r.getDate()).compareTo(currentDate) <= 0)
					runsByDate.add(r);
				else
					break;
			}
			
			Comparator<Run> compareById = (Run r1, Run r2) -> Double.valueOf(r1.getTimes().
					getPrimaryT()).compareTo(Double.valueOf(r2.getTimes().getPrimaryT()));
			 
			Collections.sort(runsByDate, compareById);
			
			//Remove obselete runs
			
			Iterator<Run> itr = runsByDate.iterator();
		    while (itr.hasNext()) {
		      Run r = itr.next();
		      Player p = r.getPlayers()[0];
				if(playerLinks.indexOf(p.getURI()) == -1) {
					playerLinks.add(p.getURI());
				} else {
					itr.remove();
				}
		    }
			
			//Remove runs out of top ten
			if(runsByDate.size() > 10)
				runsByDate.subList(10, runsByDate.size()).clear(); 
			
			//Get the date of the world record run
			wrRunDates.add(runsByDate.get(0).getDate());
			
			LinkedHashMap<String,String> playerTimes = new LinkedHashMap<String,String>();
			
			for(Run r : runsByDate) {
				Player p = r.getPlayers()[0];
				String pURI = p.getURI();
				String playerName;
				if(playerNames.containsKey(pURI)) {
					playerName = playerNames.get(pURI);
				} else {
					playerNames.put(pURI, p.getName());
					playerName = playerNames.get(pURI);
					Color color = p.getColor();
					String colorHex = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
					playerColors.put(playerName, colorHex);
					
				}
				
				playerTimes.put(playerName, Double.valueOf(r.getTimes().getPrimaryT()).toString());
			}
			
			timeData.put(currentDate.toString(), playerTimes);
			if(currentDate.compareTo(LocalDate.now()) >= 1) {
				break;
			}	
			currentDate = currentDate.plusDays(1);
		}
		//Start the csvWriter
		FileWriter csvWriter = new FileWriter("data/" + FILE_NAME + ".csv");
		csvWriter.append("Player,");
		csvWriter.append("Image,");
		
		int dateIndex = 0;
		for(String date : timeData.keySet()) {
			if(dateIndex % NTH_DAY == 0)
				csvWriter.append(date + ",");
			dateIndex++;
		}
		for(String pURI : playerNames.keySet()) {
			
			String playerName = playerNames.get(pURI);
			String pID = pURI.substring(pURI.lastIndexOf("/")+1);
			csvWriter.append("\n");
			csvWriter.append(playerName + ",");
			
			String imageURL = "https://www.speedrun.com/userasset/"+ pID + "/image";
			if(testImage(imageURL)) {
				playerImages.put(playerName,imageURL);
				csvWriter.append(imageURL + ",");
			} else {
				csvWriter.append(",");
			}
			
			int timeIndex = 0;
			for(LinkedHashMap<String,String> playerTimes : timeData.values()) {
				if(timeIndex % NTH_DAY == 0) {
					if (playerTimes.get(playerName) != null) {
						csvWriter.append(Double.valueOf(Double.valueOf(playerTimes.get(playerName))/60).toString());
					}
					csvWriter.append(",");
				} 
				timeIndex++;
			}
		}
		csvWriter.close();
		
		//Start the jsonWriter
		JsonObject json = new JsonObject();
		ArrayList<String> wrHolders = new ArrayList<String>();
		JsonArray players = new JsonArray();
		JsonArray records = new JsonArray();
		int timesCount = 0;
		
		for(LinkedHashMap<String,String> playerTimes : timeData.values()) {
			
			//add world record player to list if they aren't on there yet
			String wrPlayer = playerTimes.keySet().toArray()[0].toString();
			String wrTime = playerTimes.values().toArray()[0].toString();
			if(wrHolders.indexOf(wrPlayer) == -1) {
				wrHolders.add(wrPlayer);
				
				JsonObject player = new JsonObject();
				player.addProperty("name", wrPlayer);
				player.addProperty("image", playerImages.get(wrPlayer));
				player.addProperty("color", playerColors.get(wrPlayer));
				
				players.add(player);
			}	
			JsonObject record = new JsonObject();
			record.addProperty("player",wrPlayer);
			record.addProperty("player_color",playerColors.get(wrPlayer));
			record.addProperty("time",wrTime);
			record.addProperty("date",wrRunDates.get(timesCount));
			
			records.add(record);
			
			timesCount++;
		}
		
		json.add("players", players);
		json.add("records", records);
		
		FileWriter file = new FileWriter("data/" + FILE_NAME + ".txt");
		file.write(json.toString());
		
		file.flush();
		file.close();
		
		FileWriter colorFile = new FileWriter("data/" + FILE_NAME + "_colors.txt");
		String colorOutput = "custom_palette: \"";
		for(String player : playerColors.keySet()) {
			colorOutput += player + ": " + playerColors.get(player) + "\\n"; 
		}
		colorOutput += "\"";
		
		colorFile.write(colorOutput);
		
		colorFile.flush();
		colorFile.close();
	}
	public static Boolean testImage(String url) {  
			try {
				InputStream urlInput = new URL(url).openStream();
	            ImageInputStream iis = ImageIO.createImageInputStream(urlInput);
	            
	            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
	
	            while (imageReaders.hasNext()) {
	                ImageReader reader = (ImageReader) imageReaders.next();
	                if(reader.getFormatName().equals("gif")) {
	                	System.out.println(url + " is a gif. Discarding.");
	                	return false;
	                }
	            }
	            return true;
			} catch (Exception e) {
				return false;
			}
    }
}
