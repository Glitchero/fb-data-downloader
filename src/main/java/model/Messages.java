package model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Messages {

	@SerializedName("data") List<MessageData> messageList;
	@SerializedName("paging") Paging paging;
	@SerializedName("next") String next;
	@SerializedName("previous") String previous;

	public Messages(List<MessageData> messageList,Paging paging,String next,String previous) {
		this.messageList = messageList;
		this.paging = paging;
		this.next = next;
		this.previous = previous;
	}

	
	public List<MessageData> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<MessageData> messageList) {
		this.messageList = messageList;
	}


	public Paging getPaging() {
		return paging;
	}


	public void setPaging(Paging paging) {
		this.paging = paging;
	}


	public String getNext() {
		return next;
	}


	public void setNext(String next) {
		this.next = next;
	}


	public String getPrevious() {
		return previous;
	}


	public void setPrevious(String previous) {
		this.previous = previous;
	}

	
	
}
