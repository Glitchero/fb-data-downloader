package model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Participants {

	@SerializedName("data") List<Participant> parsipantList;

	public Participants(List<Participant> parsipantList) {
		this.parsipantList = parsipantList;
	}

	public List<Participant> getParsipantList() {
		return parsipantList;
	}

	public void setParsipantList(List<Participant> parsipantList) {
		this.parsipantList = parsipantList;
	}
	
	
	
}