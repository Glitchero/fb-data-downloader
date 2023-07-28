package model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MessageData {

	@SerializedName("created_time") String created_time;
	@SerializedName("from") From from;
	@SerializedName("message") String message;
	@SerializedName("id") String id;
	@SerializedName("messages") Messages messages;
	@SerializedName("tags") Tags tags;
	@SerializedName("is_subscribed") String isSubscribed;
	@SerializedName("snippet") String snippet;
	@SerializedName("updated_time") String updatedTime;
	@SerializedName("message_count") String messageCount;
	@SerializedName("can_reply") String canReply;
	@SerializedName("link") String link;
	@SerializedName("unreadCount") String unreadCount;
	@SerializedName("participants") Participants participants;
	
	
	public MessageData(String created_time, From from, String message, String id, Messages messages, Tags tags,
			String isSubscribed, String snippet, String updatedTime, String messageCount, String canReply, String link,
			String unreadCount, Participants participants) {
		this.created_time = created_time;
		this.from = from;
		this.message = message;
		this.id = id;
		this.messages = messages;
		this.tags = tags;
		this.isSubscribed = isSubscribed;
		this.snippet = snippet;
		this.updatedTime = updatedTime;
		this.messageCount = messageCount;
		this.canReply = canReply;
		this.link = link;
		this.unreadCount = unreadCount;
		this.participants = participants;
	}


	public String getCreated_time() {
		return created_time;
	}


	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}


	public From getFrom() {
		return from;
	}


	public void setFrom(From from) {
		this.from = from;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Messages getMessages() {
		return messages;
	}


	public void setMessages(Messages messages) {
		this.messages = messages;
	}


	public Tags getTags() {
		return tags;
	}


	public void setTags(Tags tags) {
		this.tags = tags;
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