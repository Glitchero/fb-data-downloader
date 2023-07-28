package model;

public class Output {

	String customerId  = "";
	String customerName = "";
	String customerMessage = "";
	String messageForPhone = "";
	String stockMessage = "";
	String fullMessage = "";
	
	
	
	public Output(String customerId, String customerName, String customerMessage, String messageForPhone,
			String stockMessage, String fullMessage) {
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerMessage = customerMessage;
		this.messageForPhone = messageForPhone;
		this.stockMessage = stockMessage;
		this.fullMessage = fullMessage;
	}
	
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerMessage() {
		return customerMessage;
	}
	public void setCustomerMessage(String customerMessage) {
		this.customerMessage = customerMessage;
	}
	public String getMessageForPhone() {
		return messageForPhone;
	}
	public void setMessageForPhone(String messageForPhone) {
		this.messageForPhone = messageForPhone;
	}
	public String getStockMessage() {
		return stockMessage;
	}
	public void setStockMessage(String stockMessage) {
		this.stockMessage = stockMessage;
	}


	public String getFullMessage() {
		return fullMessage;
	}


	public void setFullMessage(String fullMessage) {
		this.fullMessage = fullMessage;
	}
	
	
	
	 
}
