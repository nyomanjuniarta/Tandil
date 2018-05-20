package filtereddata;

import geodata.Geometry;

public class FilteredTrajectory {	
	private String userId;
	private long timestamp;
	private Geometry[] geometry;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Geometry[] getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometry[] geometry) {
		this.geometry = geometry;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
