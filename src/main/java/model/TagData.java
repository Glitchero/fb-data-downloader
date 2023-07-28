package model;

import com.google.gson.annotations.SerializedName;

public class TagData {

	@SerializedName("name") String name;

	
	public TagData(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
