package geodata;

import geodata.Trajectory;

public class Doc {
	private String _id;
	private String _rev;
	private String userId;
	private Trajectory trajectory;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Trajectory getTrajectory() {
		return trajectory;
	}
	public void setTrajectory(Trajectory trajectory) {
		this.trajectory = trajectory;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String get_rev() {
		return _rev;
	}
	public void set_rev(String _rev) {
		this._rev = _rev;
	}
}
