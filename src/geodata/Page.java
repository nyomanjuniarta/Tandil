package geodata;

public class Page implements Comparable<Page>{
	public class Payload{
		private String itemId;
		private String userId;
		private String type;
		private long timestamp;
		public String getItemId() {
			return itemId;
		}
		public void setItemId(String itemId) {
			this.itemId = itemId;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
		@Override
		public String toString(){
			return userId + "," + itemId + "," + timestamp + "," + type;
		}
	}
	
	private String source;
	private Payload payload;
	private String type;
	private String id;
	private long timestamp;
	public Payload getPayload() {
		return payload;
	}
	public void setPayload(Payload payload) {
		this.payload = payload;
	}
	
	@Override
	public int compareTo(Page p){
		if(getPayload().getUserId().compareTo(p.getPayload().getUserId()) == 0){
			return (int) (getPayload().getTimestamp() - p.getPayload().getTimestamp());
		}
		return getPayload().getUserId().compareTo(p.getPayload().getUserId());
	}
}
