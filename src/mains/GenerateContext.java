package mains;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class GenerateContext {
	public static void generate_separate_context(){
		int numberOfItems = 20, c;
		int[] durationArray = new int[numberOfItems];
		String[] ratingArray = new String[numberOfItems];
		String path = System.getProperty("user.dir") + "\\src\\";
		BufferedReader br = null;
		HashMap<String, Integer> intMap = new HashMap<String, Integer>();
		String sCurrentLine, item_name, lines[], sequence, items[], elements[];
		try {
			br = new BufferedReader(new FileReader(path + "listOfStories_visited.csv"));
			PrintWriter writerDurCsv = new PrintWriter("tandil_duration.csv", "UTF-8");
			PrintWriter writerRtgCsv = new PrintWriter("tandil_rating.csv", "UTF-8");
			c = 0;
			while((sCurrentLine = br.readLine()) != null){
				lines = sCurrentLine.split(",");
				item_name = lines[1].substring(0, Math.min(lines[1].length(), 10));
				intMap.put(item_name, c);
				writerDurCsv.print("," + item_name);
				writerRtgCsv.print("," + item_name);
				c++;
			}
			writerDurCsv.println();
			writerRtgCsv.println();
			br.close();
			br = new BufferedReader(new FileReader(path + "page_duration_likeSPMF.txt"));
			//PrintWriter writerDurTxt = new PrintWriter("tandil_duration.txt", "UTF-8");
			while((sCurrentLine = br.readLine()) != null){
				for(int k1 = 0; k1 < numberOfItems; k1++){
					durationArray[k1] = 0;
					ratingArray[k1] = "?";
				}
				writerDurCsv.print(sCurrentLine.split(",")[0]);
				writerRtgCsv.print(sCurrentLine.split(",")[0]);
				sequence = sCurrentLine.split(",")[1];
				items = sequence.split(" -1 ");
				for(int i = 0; i < items.length - 1; i++){ // ignoring -2 in the end of sequence
					elements = items[i].split(" ");
					durationArray[intMap.get(elements[0])] += Integer.parseInt(elements[1]);
					ratingArray[intMap.get(elements[0])] = elements[2];
				}
				for(int k2 = 0; k2 < numberOfItems; k2++){
					if(durationArray[k2] == 0){
						writerDurCsv.print(",?");
					}
					else if(durationArray[k2] > 1000000){
						writerDurCsv.print(",10");
					}
					else{
						writerDurCsv.print("," + durationArray[k2]/100000);
					}
					writerRtgCsv.print("," + ratingArray[k2]);
				}
				writerDurCsv.println();
				writerRtgCsv.println();
			}
			writerDurCsv.close();
			writerRtgCsv.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
