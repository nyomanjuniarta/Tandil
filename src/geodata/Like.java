package geodata;

public class Like implements Comparable<Like>{
	public class Payload{
		private String userId;
		private String itemId;
		private double value;
		private long timestamp;
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getItemId() {
			return itemId;
		}
		public void setItemId(String itemId) {
			this.itemId = itemId;
		}
		public double getValue() {
			return value;
		}
		public void setValue(double value) {
			this.value = value;
		}
		public long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
		@Override
		public String toString(){
			return userId + "," + itemId + "," + timestamp + "," + value;
		}
	}
	
	private String source;
	private String type;
	private String id;
	private long timestamp;
	private Payload payload;
	public Payload getPayload() {
		return payload;
	}
	public void setPayload(Payload payload) {
		this.payload = payload;
	}
	
	@Override
	public int compareTo(Like l){
		if(getPayload().getUserId().compareTo(l.getPayload().getUserId()) == 0){
			return (int) (getPayload().getTimestamp() - l.getPayload().getTimestamp());
		}
		return getPayload().getUserId().compareTo(l.getPayload().getUserId());
	}
}
