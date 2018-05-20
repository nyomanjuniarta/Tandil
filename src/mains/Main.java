package mains;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Main {

	public static void main(String[] args) {
		//ReadJson.readStories();
		//ReadJson.readPages();
		//ReadJson.readLikes();
		//ReadJson.pagesToSpmf(false);
		//Similarity.generate_dissimilarity(System.getProperty("user.dir") + "/src/254Visitors.txt", "254Visitors", 3);
		//Similarity.generate_similarity_lcs(System.getProperty("user.dir") + "/src/small.txt", "small", 0.2, true, 2);
		GenerateContext.generate_separate_context();
	}

}
