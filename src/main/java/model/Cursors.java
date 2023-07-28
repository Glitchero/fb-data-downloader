package model;

import com.google.gson.annotations.SerializedName;

public class Cursors {
	
	@SerializedName("after") String after;

	public Cursors(String after) {
		this.after = after;
	}

	public String getAfter() {
		return after;
	}

	public void setAfter(String after) {
		this.after = after;
	}
		
	
}