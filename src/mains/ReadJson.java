package mains;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import geodata.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;

public class ReadJson {
	public static void readStories(){
		try{
			Gson gson = new GsonBuilder().create();
			PrintWriter writer = new PrintWriter("src/listOfStories.csv", "UTF-8");
			
			Reader reader = new InputStreamReader(ReadJson.class.getResourceAsStream("/stories.json"), "UTF-8");
			Story[] stories = gson.fromJson(reader, Story[].class);			
			for(int s = 0; s < stories.length; s++){
				//System.out.println(stories[s].toString());
				writer.println(stories[s].toString());
			}
			
			reader = new InputStreamReader(ReadJson.class.getResourceAsStream("/stories-user.json"), "UTF-8");
			stories = gson.fromJson(reader, Story[].class);			
			for(int s = 0; s < stories.length; s++){
				//System.out.println(stories[s].toString());
				writer.println(stories[s].toString());
			}
			
			reader.close();
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void readTrajectories(){
		try{
			Gson gson = new GsonBuilder().create();
			PrintWriter writer = new PrintWriter("src/trajectories.csv", "UTF-8");
						
			Reader reader = new InputStreamReader(ReadJson.class.getResourceAsStream("/trajectories.json"), "UTF-8");
			Trajectories[] trajectories = gson.fromJson(reader, Trajectories[].class);
			
			Feature[] features;
			for(int t = 0; t < trajectories.length; t++){
				features = trajectories[t].getDoc().getTrajectory().getFeatures();
				for(int f = 0; f < features.length; f++){
					writer.println(features[f].getProperties().getTimestamp() + "," + trajectories[t].getDoc().getUserId() + "," + features[f].getGeometry().getCoordinates()[0] + "," + features[f].getGeometry().getCoordinates()[1]);
				}
			}
			reader.close();
			writer.close();
			
			/*FilteredTrajectory[] filteredTrajectories = new FilteredTrajectory[trajectories.length];
			for(int t = 0; t < trajectories.length; t++){
				filteredTrajectories[t] = trajectories[t].getFilteredTrajectory();
			}
			Writer writer = new OutputStreamWriter(new FileOutputStream("src/filteredTrajectories.json"), "UTF-8");
			gson.toJson(filteredTrajectories, writer);*/
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, String> createMapOfStories(){
		HashMap<String, String> storiesMap = new HashMap<String, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/listOfStories.csv"));
			String sCurrentLine, splitted[];
			while ((sCurrentLine = br.readLine()) != null) {
				splitted = sCurrentLine.split(",");
				storiesMap.put(splitted[0], splitted[1]);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return storiesMap;
	}

	public static void readLikes(){
		String toWrite, itemId;
		try{
			Gson gson = new GsonBuilder().create();
			HashMap<String, String> storiesMap = createMapOfStories();
			Reader reader = new InputStreamReader(ReadJson.class.getResourceAsStream("/likes.json"), "UTF-8");
			Like[] likes = gson.fromJson(reader, Like[].class);
			Arrays.sort(likes);
			PrintWriter writer = new PrintWriter("src/likes.csv", "UTF-8");
			for(int l = 0; l < likes.length; l++){
				toWrite = likes[l].getPayload().toString();
				itemId = toWrite.split(",")[1];
				toWrite = toWrite.replaceAll(itemId, storiesMap.get(itemId));
				//System.out.println(toWrite);
				writer.println(toWrite);
			}
			reader.close();
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void readPages(){
		String toWrite, itemId;
		try{
			Gson gson = new GsonBuilder().create();
			HashMap<String, String> storiesMap = createMapOfStories();
			Reader reader = new InputStreamReader(ReadJson.class.getResourceAsStream("/pages.json"), "UTF-8");
			Page[] pages = gson.fromJson(reader, Page[].class);
			Arrays.sort(pages);
			PrintWriter writer = new PrintWriter("src/pages.csv", "UTF-8");
			for(int p = 0; p < pages.length; p++){
				toWrite = pages[p].getPayload().toString();
				//System.out.println(toWrite);
				itemId = toWrite.split(",")[1];
				toWrite = toWrite.replaceAll(itemId, storiesMap.get(itemId));
				//System.out.println(toWrite);
				writer.println(toWrite);
			}
			reader.close();
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static int duration(String start, String end){
		int t1 = Integer.parseInt(start.substring(4, start.length()));
		int t2 = Integer.parseInt(end.substring(4, end.length()));
		return t2 - t1;
	}
	
	public static void pagesToSpmf(boolean justPages){
		String sCurrentLine, splitted[], toWrite = "", userBefore = "", itemBefore = "", startBefore = "9999999999999", endBefore = "9999999999999", ratingBefore = "";
		String outputFile = justPages ? "src/pageSPMF.txt" : "src/page_duration_likeSPMF.txt";
		int duration;
		try {
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/pagesLikes.csv"));
			PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
			while ((sCurrentLine = br.readLine()) != null) {
				splitted = sCurrentLine.split(",");
				if(!splitted[1].equals(itemBefore)){
					duration = duration(startBefore, endBefore);
					if(justPages){
						toWrite += itemBefore.substring(0, Math.min(itemBefore.length(), 10)) + " -1 ";
					}
					else{
						toWrite += itemBefore.substring(0, Math.min(itemBefore.length(), 10)) + " " + String.valueOf(duration) + " " + ratingBefore + " -1 ";
					}
					startBefore = splitted[2];
					if(!splitted[0].equals(userBefore)){
						writer.println(toWrite + "-2");
						userBefore = splitted[0];
						toWrite = userBefore + ",";
					}
				}
				ratingBefore = splitted[4];
				itemBefore = splitted[1];
				endBefore = splitted[2];
			}
			br.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
