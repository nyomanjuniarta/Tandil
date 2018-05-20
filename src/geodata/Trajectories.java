package geodata;

import filtereddata.FilteredTrajectory;
import filtereddata.TrajectoryLSTM;
import geodata.Doc;
import geodata.Feature;
import geodata.Geometry;

public class Trajectories {
	private class Value{
		private String rev;
	}
	
	private String id;
	private String key;
	private Value value;
	private Doc doc;
	public Doc getDoc() {
		return doc;
	}
	public void setDoc(Doc doc) {
		this.doc = doc;
	}
	
	public FilteredTrajectory getFilteredTrajectory(){
		FilteredTrajectory filteredTrajectory = new FilteredTrajectory();
		filteredTrajectory.setUserId(doc.getUserId());
		Feature[] features = doc.getTrajectory().getFeatures();
		Geometry[] geometries = new Geometry[features.length];
		for(int f = 0; f < features.length; f++){
			geometries[f] = new Geometry();
			geometries[f].setCoordinates(features[f].getGeometry().getCoordinates());
		}
		filteredTrajectory.setGeometry(geometries);
		return filteredTrajectory;
	}
}
