package geodata;

import geodata.Geometry;

public class Story {
	private class Stories{
		private String storyId;
		private String title;
	}
	
	private class Properties{
		//private String storyId;
		private Stories[] stories;
	}
	
	private Properties properties;
	private Geometry geometry;
	private String poiId;
	private String type;
	
	@Override
	public String toString(){
		//return poiId + " " + properties.storyId + " " + geometry.getCoordinates()[0] + "," + geometry.getCoordinates()[1];
		return properties.stories[0].storyId + "," + properties.stories[0].title.replace(" ", "_").replace(",", "").replace("\"", "");
	}
}
