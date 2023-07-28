package controller;

import java.util.Date;


public class Conversations {


     private String leadName = "Unknown";
	 
	 private String conversationGuid = ""; 
	 
	 private Integer messagesCount = 0;
	 
	 private Integer leadMessagesCount = 0;
	 
	 private String snippet = "";
	
	 private Integer unreadCount = 0;
	  
	 private Date updatedTime;
	 
	 private Date leadCreatedTime;
	 
	 private Date leadUpdatedTime;
	 
	 private String maskedFacebookId = "";
	 
	 private String leadMessages = "";
	 
	 private String stockMessages = "";
	 
	 private String leadPhoneNumber = "";
	 
	 private String leadEmail = "";
	 
	 private String lastUserWhoSentMessage = "";
	 
	 private boolean macropayDeliveryAppears = false;
	 
	 private boolean leadRedirectedToStore = false;
	 
	 private boolean leadAbandoned = false;

	 private long leadConversationDuration = -1;
	 
	 private int leadMessageInterestLevel = 0;
	 
	 private int leadConversationUniqueDays = 0;
	 
	 private int leadConversationLenght = 0;
	 
	 private int leadInteractions = 0;
	 	 
	 private long leadConversationDurationRecentMonth = -1;
	 
	 private int leadConversationUniqueDaysRecentMonth = 0;
	 
	 private Date leadCreatedTimeRecentMonth;
	 
	 private int leadConversationLenghtRecentMonth = 0;
	 
	 private Integer leadMessagesCountRecentMonth = 0;
	 
	 private String leadMessagesRecentMonth = "";
	 
	 private int leadMessageInterestLevelRecentMonth = 0;
	
	 private double leadScore;

	 public Conversations() {
		 
	 }
			 
	public Conversations(String leadName, String conversationGuid, Integer messagesCount, Integer leadMessagesCount,
			String snippet, Integer unreadCount, Date updatedTime, Date leadCreatedTime, Date leadUpdatedTime,
			String maskedFacebookId, String leadMessages, String stockMessages, String leadPhoneNumber,
			String leadEmail, String lastUserWhoSentMessage, boolean macropayDeliveryAppears,
			boolean leadRedirectedToStore, boolean leadAbandoned, long leadConversationDuration,
			int leadMessageInterestLevel, int leadConversationUniqueDays, int leadConversationLenght,
			int leadInteractions, long leadConversationDurationRecentMonth, int leadConversationUniqueDaysRecentMonth,
			Date leadCreatedTimeRecentMonth, int leadConversationLenghtRecentMonth,
			Integer leadMessagesCountRecentMonth, String leadMessagesRecentMonth,
			int leadMessageInterestLevelRecentMonth, double leadScore) {
		this.leadName = leadName;
		this.conversationGuid = conversationGuid;
		this.messagesCount = messagesCount;
		this.leadMessagesCount = leadMessagesCount;
		this.snippet = snippet;
		this.unreadCount = unreadCount;
		this.updatedTime = updatedTime;
		this.leadCreatedTime = leadCreatedTime;
		this.leadUpdatedTime = leadUpdatedTime;
		this.maskedFacebookId = maskedFacebookId;
		this.leadMessages = leadMessages;
		this.stockMessages = stockMessages;
		this.leadPhoneNumber = leadPhoneNumber;
		this.leadEmail = leadEmail;
		this.lastUserWhoSentMessage = lastUserWhoSentMessage;
		this.macropayDeliveryAppears = macropayDeliveryAppears;
		this.leadRedirectedToStore = leadRedirectedToStore;
		this.leadAbandoned = leadAbandoned;
		this.leadConversationDuration = leadConversationDuration;
		this.leadMessageInterestLevel = leadMessageInterestLevel;
		this.leadConversationUniqueDays = leadConversationUniqueDays;
		this.leadConversationLenght = leadConversationLenght;
		this.leadInteractions = leadInteractions;
		this.leadConversationDurationRecentMonth = leadConversationDurationRecentMonth;
		this.leadConversationUniqueDaysRecentMonth = leadConversationUniqueDaysRecentMonth;
		this.leadCreatedTimeRecentMonth = leadCreatedTimeRecentMonth;
		this.leadConversationLenghtRecentMonth = leadConversationLenghtRecentMonth;
		this.leadMessagesCountRecentMonth = leadMessagesCountRecentMonth;
		this.leadMessagesRecentMonth = leadMessagesRecentMonth;
		this.leadMessageInterestLevelRecentMonth = leadMessageInterestLevelRecentMonth;
		this.leadScore = leadScore;
	}

	public String getLeadName() {
		return leadName;
	}

	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}

	public String getConversationGuid() {
		return conversationGuid;
	}

	public void setConversationGuid(String conversationGuid) {
		this.conversationGuid = conversationGuid;
	}

	public Integer getMessagesCount() {
		return messagesCount;
	}

	public void setMessagesCount(Integer messagesCount) {
		this.messagesCount = messagesCount;
	}

	public Integer getLeadMessagesCount() {
		return leadMessagesCount;
	}

	public void setLeadMessagesCount(Integer leadMessagesCount) {
		this.leadMessagesCount = leadMessagesCount;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public Integer getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(Integer unreadCount) {
		this.unreadCount = unreadCount;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Date getLeadCreatedTime() {
		return leadCreatedTime;
	}

	public void setLeadCreatedTime(Date leadCreatedTime) {
		this.leadCreatedTime = leadCreatedTime;
	}

	public Date getLeadUpdatedTime() {
		return leadUpdatedTime;
	}

	public void setLeadUpdatedTime(Date leadUpdatedTime) {
		this.leadUpdatedTime = leadUpdatedTime;
	}

	public String getMaskedFacebookId() {
		return maskedFacebookId;
	}

	public void setMaskedFacebookId(String maskedFacebookId) {
		this.maskedFacebookId = maskedFacebookId;
	}

	public String getLeadMessages() {
		return leadMessages;
	}

	public void setLeadMessages(String leadMessages) {
		this.leadMessages = leadMessages;
	}

	public String getStockMessages() {
		return stockMessages;
	}

	public void setStockMessages(String stockMessages) {
		this.stockMessages = stockMessages;
	}

	public String getLeadPhoneNumber() {
		return leadPhoneNumber;
	}

	public void setLeadPhoneNumber(String leadPhoneNumber) {
		this.leadPhoneNumber = leadPhoneNumber;
	}

	public String getLeadEmail() {
		return leadEmail;
	}

	public void setLeadEmail(String leadEmail) {
		this.leadEmail = leadEmail;
	}

	public String getLastUserWhoSentMessage() {
		return lastUserWhoSentMessage;
	}

	public void setLastUserWhoSentMessage(String lastUserWhoSentMessage) {
		this.lastUserWhoSentMessage = lastUserWhoSentMessage;
	}

	public boolean isMacropayDeliveryAppears() {
		return macropayDeliveryAppears;
	}

	public void setMacropayDeliveryAppears(boolean macropayDeliveryAppears) {
		this.macropayDeliveryAppears = macropayDeliveryAppears;
	}

	public boolean isLeadRedirectedToStore() {
		return leadRedirectedToStore;
	}

	public void setLeadRedirectedToStore(boolean leadRedirectedToStore) {
		this.leadRedirectedToStore = leadRedirectedToStore;
	}

	public boolean isLeadAbandoned() {
		return leadAbandoned;
	}

	public void setLeadAbandoned(boolean leadAbandoned) {
		this.leadAbandoned = leadAbandoned;
	}

	public long getLeadConversationDuration() {
		return leadConversationDuration;
	}

	public void setLeadConversationDuration(long leadConversationDuration) {
		this.leadConversationDuration = leadConversationDuration;
	}

	public int getLeadMessageInterestLevel() {
		return leadMessageInterestLevel;
	}

	public void setLeadMessageInterestLevel(int leadMessageInterestLevel) {
		this.leadMessageInterestLevel = leadMessageInterestLevel;
	}

	public int getLeadConversationUniqueDays() {
		return leadConversationUniqueDays;
	}

	public void setLeadConversationUniqueDays(int leadConversationUniqueDays) {
		this.leadConversationUniqueDays = leadConversationUniqueDays;
	}

	public int getLeadConversationLenght() {
		return leadConversationLenght;
	}

	public void setLeadConversationLenght(int leadConversationLenght) {
		this.leadConversationLenght = leadConversationLenght;
	}

	public int getLeadInteractions() {
		return leadInteractions;
	}

	public void setLeadInteractions(int leadInteractions) {
		this.leadInteractions = leadInteractions;
	}

	public long getLeadConversationDurationRecentMonth() {
		return leadConversationDurationRecentMonth;
	}

	public void setLeadConversationDurationRecentMonth(long leadConversationDurationRecentMonth) {
		this.leadConversationDurationRecentMonth = leadConversationDurationRecentMonth;
	}

	public int getLeadConversationUniqueDaysRecentMonth() {
		return leadConversationUniqueDaysRecentMonth;
	}

	public void setLeadConversationUniqueDaysRecentMonth(int leadConversationUniqueDaysRecentMonth) {
		this.leadConversationUniqueDaysRecentMonth = leadConversationUniqueDaysRecentMonth;
	}

	public Date getLeadCreatedTimeRecentMonth() {
		return leadCreatedTimeRecentMonth;
	}

	public void setLeadCreatedTimeRecentMonth(Date leadCreatedTimeRecentMonth) {
		this.leadCreatedTimeRecentMonth = leadCreatedTimeRecentMonth;
	}

	public int getLeadConversationLenghtRecentMonth() {
		return leadConversationLenghtRecentMonth;
	}

	public void setLeadConversationLenghtRecentMonth(int leadConversationLenghtRecentMonth) {
		this.leadConversationLenghtRecentMonth = leadConversationLenghtRecentMonth;
	}

	public Integer getLeadMessagesCountRecentMonth() {
		return leadMessagesCountRecentMonth;
	}

	public void setLeadMessagesCountRecentMonth(Integer leadMessagesCountRecentMonth) {
		this.leadMessagesCountRecentMonth = leadMessagesCountRecentMonth;
	}

	public String getLeadMessagesRecentMonth() {
		return leadMessagesRecentMonth;
	}

	public void setLeadMessagesRecentMonth(String leadMessagesRecentMonth) {
		this.leadMessagesRecentMonth = leadMessagesRecentMonth;
	}

	public int getLeadMessageInterestLevelRecentMonth() {
		return leadMessageInterestLevelRecentMonth;
	}

	public void setLeadMessageInterestLevelRecentMonth(int leadMessageInterestLevelRecentMonth) {
		this.leadMessageInterestLevelRecentMonth = leadMessageInterestLevelRecentMonth;
	}

	public double getLeadScore() {
		return leadScore;
	}

	public void setLeadScore(double leadScore) {
		this.leadScore = leadScore;
	}
	 
	
	
	@Override
	public String toString() {
	    return "Conversations{" +
	            "leadName='" + leadName + '\'' +
	            ", conversationGuid='" + conversationGuid + '\'' +
	            ", messagesCount=" + messagesCount +
	            '}';
	}
	 

}
