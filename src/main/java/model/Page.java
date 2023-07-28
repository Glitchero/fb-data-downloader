package model;


import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Page {

	@SerializedName("id") String id;
	@SerializedName("conversations") Conversations conversations;

	public Page(String id, Conversations conversations) {
		this.id = id;
		this.conversations = conversations;
	}

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public Conversations getConversations() {
		return conversations;
	}



	public void setConversations(Conversations conversations) {
		this.conversations = conversations;
	}

	
	
	
}