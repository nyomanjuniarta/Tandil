package mains;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import similarity.ACS;
import similarity.ADS;

public class Similarity {
	public static List<Vector> pars_sequence_integer(String seq){
		  List<Vector> MS=new ArrayList(); 
		  if (seq.trim().equals("-2")){
		      return MS;
		  }
	      seq=seq.replace("-2", "").trim();
	      try {
	          
	          String[] seqs=seq.trim().split("-1");
	          for(int i=0;i<seqs.length;i++){
	              String set = seqs[i];
	              String[] items=set.trim().split(" ");
	              
	              Vector itemset =new Vector() ;
	              for(int j=0;j<items.length;j++){
	                  itemset.addElement(items[j].trim());
	              }
	              
	              MS.add(itemset);
	          }
	      } catch (Exception e) {
	          System.out.println("Error in pars " +
	                             e.toString());
	      }
	      return MS;    
	  }
	
	public static double sim_longest_common_substring(List<Vector> s1, List<Vector> s2){
		// only for sequences whose itemset_size = 1
		int max_length = Math.max(s1.size(), s2.size());
		double longest = 0;
		double[][] L = new double[s1.size()+1][s2.size()+1];
		for(int i = 1; i <= s1.size(); i++){
			for(int j = 1; j <= s2.size(); j++){
				if(s1.get(i-1).get(0).equals(s2.get(j-1).get(0))){
					L[i][j] = L[i-1][j-1] + 1;
					if(L[i][j] > longest){
						longest = L[i][j];
					}
				}
				//System.out.print(L[i][j] + " ");
			}
			//System.out.println();
		}
		return (double) longest/max_length;
	}
	
	public static double sim_longest_common_subsequence(List<Vector> s1, List<Vector> s2){
		// only for sequences whose itemset_size = 1
		int max_length = Math.max(s1.size(), s2.size());
		double[][] L = new double[s1.size()+1][s2.size()+1];
		for(int i = 1; i <= s1.size(); i++){
			for(int j = 1; j <= s2.size(); j++){
				if(s1.get(i-1).get(0).equals(s2.get(j-1).get(0))){
					L[i][j] = L[i-1][j-1] + 1;
				}
				else{
					L[i][j] = Math.max(L[i-1][j], L[i][j-1]);
				}
				//System.out.print(L[i][j] + " ");
			}
			//System.out.println();
		}
		return (double) L[s1.size()][s2.size()]/max_length;
	}
	
	/**
	 * generate n x n distance matrix
	 * @param input_file
	 * @param output_file
	 * @param type (1 = longest common substring, 2 = longest common subsequence, 3 = acs)
	 */
	public static void generate_dissimilarity(String input_file, String output_file, int type){
		try {
			BufferedReader br = new BufferedReader(new FileReader(input_file));
			String output_extra = "";
			if(type == 1)
				output_extra = "_lcStr";
			else if(type == 2)
				output_extra = "_lcSeq";
			else if(type == 3)
				output_extra = "_acs";
			PrintWriter writer = null;
			double dist = 0;
			int i, j;
			
			String sCurrentLine, splitted[];
			List<List<Vector>> sequences = new ArrayList(); 
			List<String> person_ids = new ArrayList();
			List<BigDecimal> allDist = new ArrayList<BigDecimal>();
			while ((sCurrentLine = br.readLine()) != null) {
				splitted = sCurrentLine.split(",");
				person_ids.add(splitted[0].substring(0, 3));
				sequences.add(pars_sequence_integer(splitted[1]));
				if(type == 3)
					allDist.add(ADS.Number_Subsequence(sequences.get(sequences.size()-1)));
			}
			
			BigDecimal intersection, max, distanceBig;
			writer = new PrintWriter(output_file + output_extra + ".txt", "UTF-8");
			for(i = 1; i < sequences.size(); i++){
				for(j = 0; j < i; j++){
					if(type == 1){
						dist = 1 - sim_longest_common_substring(sequences.get(i), sequences.get(j));
						writer.print(dist + " ");
					}
					else if(type == 2){
						dist = 1 - sim_longest_common_subsequence(sequences.get(i), sequences.get(j));
						writer.print(dist + " ");
					}
					else if(type == 3){
						intersection = ACS.ACS_method(sequences.get(i), sequences.get(j));
						max = allDist.get(i).max(allDist.get(j));
						distanceBig = BigDecimal.ONE.subtract(intersection.divide(max, MathContext.DECIMAL128));
						writer.print(distanceBig + " ");
					}
				}
				writer.println();
			}
			writer.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * generate n x n similarity matrix
	 * @param input_file
	 * @param output_file
	 * @param threshold (output a binary similarity  => for latviz)
	 * @param half (output a 0-1 similarity, triangular matrix => for non-fca clustering)
	 * @param type (1 = longest common substring, 2 = longest common subsequence)
	 */
	public static void generate_similarity_lcs(String input_file, String output_file, double threshold, boolean half, int type){
		try {
			BufferedReader br = new BufferedReader(new FileReader(input_file));
			String output_extra = "";
			if(type == 1)
				output_extra = "_lcStr";
			else if(type == 2)
				output_extra = "_lcSeq";
			else if(type == 3)
				output_extra = "_acs";
			PrintWriter writer = null;
			double dist = 0;
			int i, j;
			
			String sCurrentLine, splitted[];
			List<List<Vector>> sequences = new ArrayList(); 
			List<String> person_ids = new ArrayList();
			List<BigDecimal> allDist = new ArrayList<BigDecimal>();
			while ((sCurrentLine = br.readLine()) != null) {
				splitted = sCurrentLine.split(",");
				person_ids.add(splitted[0].substring(0, 3));
				sequences.add(pars_sequence_integer(splitted[1]));
				if(type == 3)
					allDist.add(ADS.Number_Subsequence(sequences.get(sequences.size()-1)));
			}
			
			BigDecimal intersection, max, distanceBig;
			if(half){
				writer = new PrintWriter(output_file + output_extra + ".txt", "UTF-8");
				for(i = 1; i < sequences.size(); i++){
					for(j = 0; j < i; j++){
						if(type == 1){
							dist = sim_longest_common_substring(sequences.get(i), sequences.get(j));
							writer.print(dist + " ");
						}
						else if(type == 2){
							dist = sim_longest_common_subsequence(sequences.get(i), sequences.get(j));
							writer.print(dist + " ");
						}
					}
					writer.println();
				}
			}
			else{
				writer = new PrintWriter(output_file + output_extra + ".csv", "UTF-8");
				for(i = 0; i < sequences.size(); i++){
					writer.print("," + person_ids.get(i));
				}
				writer.println();
				
				for(i = 0; i < sequences.size(); i++){
					writer.print(person_ids.get(i));
					for(j = 0; j < sequences.size(); j++){
						if(type == 1)
							dist = sim_longest_common_substring(sequences.get(i), sequences.get(j));
						else if (type == 2)
							dist = sim_longest_common_subsequence(sequences.get(i), sequences.get(j));
						if(dist >= threshold)
							writer.print(",1");
						else
							writer.print(",0");
					}
					writer.println();
				}
			}
			writer.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
