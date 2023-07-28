package model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Conversation {

	@SerializedName("messages") Messages messages;
	@SerializedName("id") String id;
	@SerializedName("is_subscribed") String isSubscribed;
	@SerializedName("snippet") String snippet;
	@SerializedName("updated_time") String updatedTime;
	@SerializedName("message_count") String messageCount;
	@SerializedName("can_reply") String canReply;
	@SerializedName("link") String link;
	@SerializedName("unread_count") String unreadCount;
	@SerializedName("participants") Participants participants;
	
	
	public Conversation(Messages messages, String id, String isSubscribed, String snippet, String updatedTime,
			String messageCount, String canReply, String link, String unreadCount, Participants participants) {
		this.messages = messages;
		this.id = id;
		this.isSubscribed = isSubscribed;
		this.snippet = snippet;
		this.updatedTime = updatedTime;
		this.messageCount = messageCount;
		this.canReply = canReply;
		this.link = link;
		this.unreadCount = unreadCount;
		this.participants = participants;
	}


	public Messages getMessages() {
		return messages;
	}


	public void setMessages(Messages messages) {
		this.messages = messages;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getIsSubscribed() {
		return isSubscribed;
	}


	public void setIsSubscribed(String isSubscribed) {
		this.isSubscribed = isSubscribed;
	}


	public String getSnippet() {
		return snippet;
	}


	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}


	public String getUpdatedTime() {
		return updatedTime;
	}


	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}


	public String getMessageCount() {
		return messageCount;
	}


	public void setMessageCount(String messageCount) {
		this.messageCount = messageCount;
	}


	public String getCanReply() {
		return canReply;
	}


	public void setCanReply(String canReply) {
		this.canReply = canReply;
	}


	public String getLink() {
		return link;
	}


	public void setLink(String link) {
		this.link = link;
	}


	public String getUnreadCount() {
		return unreadCount;
	}


	public void setUnreadCount(String unreadCount) {
		this.unreadCount = unreadCount;
	}


	public Participants getParticipants() {
		return participants;
	}


	public void setParticipants(Participants participants) {
		this.participants = participants;
	}
	
	
	
	
}
