package controller;

import controller.services.BasicHttpComponent;
import controller.services.ConversationsHttpComponent;
import controller.services.ConversationsManager;

public class ConversationsClient extends ConversationsHttpComponent{

	
	  private String apiKey = null;
	  private String baseUri = ""; //"https://openapi.ariba.com/api";
	
	  private ConversationsManager conversations;
	
	
	   public ConversationsClient(String baseUri,String apiKey) {
		      super(new BasicHttpComponent());
		      this.apiKey =apiKey;
		      this.baseUri = baseUri;
		      this.conversations = new ConversationsManager(this);
		   }
	   
	 
	   public ConversationsManager conversations() {
		      return conversations;
		   }
	   
	   @Override
		public String apiKey() {
			return this.apiKey;
		}
		

		@Override
		public String baseUri() {
			 return this.baseUri;
		}
		
		
	
}
