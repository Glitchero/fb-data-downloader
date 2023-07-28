package model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Tags {

	@SerializedName("data") List<TagData> tagList;

	public Tags(List<TagData> tagList) {
		this.tagList = tagList;
	}

	public List<TagData> getTagList() {
		return tagList;
	}

	public void setTagList(List<TagData> tagList) {
		this.tagList = tagList;
	}
	
	
}
