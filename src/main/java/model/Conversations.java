package model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Conversations {

	@SerializedName("data") List<Conversation> conversationList;
	@SerializedName("paging") Paging paging;
	//Igual aqui tiene que ir paging. Revisar
	
	public Conversations(List<Conversation> conversationList, Paging paging) {
		this.conversationList = conversationList;
		this.paging = paging;
	}

	public List<Conversation> getConversationList() {
		return conversationList;
	}

	public void setConversationList(List<Conversation> conversationList) {
		this.conversationList = conversationList;
	}

	public Paging getPaging() {
		return paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}
	
	
	
}
