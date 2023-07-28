package controller.services;

import controller.ConversationsClient;
import model.Conversations;
import model.Messages;
import model.Page;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class ConversationsManager extends ConversationsHttpComponent{

	private static final String fields1 ="conversations%7Bis_subscribed%2Csnippet%2Csubject%2Cparticipants%2Cupdated_time%2Cmessages%7Bcreated_time%2Cfrom%2Cid%2Cmessage%2Ctags%7D%2Cmessage_count%2Ccan_reply%2Clink%2Cname%2Cscoped_thread_key%2Cunread_count%7D";

	private ConversationsClient client;
	
	
	public ConversationsManager(ConversationsClient client) {
		super(client.getHttpComponent());
		this.client = client;
	}

	public Page getConversations(String apiVersion, String pageId, String longTermAccessToken) {
		 	  String json = getSecured(baseUri() + "/" + apiVersion + "/" + pageId + "?fields=" + fields1 + "&access_token=" + longTermAccessToken);   

		      if(!validate(json)){
		         return null;
		      }
		    
		      Type listType = new TypeToken<Page>(){}.getType();

		      Gson gson = new Gson();

		      return gson.fromJson(json, listType); 
		 }
	
	
	
	
	public Messages getMessages(String next) {
		try {
	 		String json = getSecured(next);   
      
	      if(!validate(json)){
	         return null;
	      }
	    
	      Type listType = new TypeToken<Messages>(){}.getType();

	      Gson gson = new Gson();
	      
	      return gson.fromJson(json, listType);    
		} catch (Exception e) {
			System.out.println(next);
			System.out.println("No next page found.");
			return null;
		}
	}


	
	
	@Override
	public String apiKey() {
		return client.apiKey();
	}

	@Override
	public String baseUri() {
		// TODO Auto-generated method stub
		return client.baseUri();
	}

	
	
	public Page getConversations(String apiVersion, String longTermAccessToken) {
		  String json = getSecured(baseUri() + "/" + apiVersion + "/" + parseIdFromApiKey(getPageData(apiVersion,longTermAccessToken)) + "?fields=" + fields1 + "&access_token=" + longTermAccessToken);   
	      if(!validate(json)){
	         return null;
	      }
	    
	      Type listType = new TypeToken<Page>(){}.getType();

	      Gson gson = new Gson();

	      return gson.fromJson(json, listType); 
	}

	private String getPageData(String apiVersion, String longTermAccessToken) {
		  String json = getSecured(baseUri() + "/" + "me" + "?access_token=" + longTermAccessToken);   
	      
	      if(!validate(json)){
	         return null;
	      }
	      	      
		  return json;
	
	}
	
	
	public static String parseIdFromApiKey(String apiKeyString) {
        Gson gson = new Gson();
        JsonObject apiKeyJson = gson.fromJson(apiKeyString, JsonObject.class);
        return apiKeyJson.get("id").getAsString();
    }
	
	
	public String parseNameFromApiKey(String apiKeyString) {
        Gson gson = new Gson();
        JsonObject apiKeyJson = gson.fromJson(apiKeyString, JsonObject.class);
        return apiKeyJson.get("name").getAsString();
    }

	public String getAdmin(String apiVersion, String longTermAccessToken) {
		String json = getPageData(apiVersion,longTermAccessToken);
        return parseNameFromApiKey(json);
	}

	
	public boolean isValid(String apiVersion, String longTermAccessToken) {
	    String json = getSecured(baseUri() + "/" + "me" + "?access_token=" + longTermAccessToken);

	    if (!validateNew(json)) {
	        System.out.println("Error message: " + json);
	        return false;
	    }

	    System.out.println("Valid access token: " + json);

	    return true;
	}
	
	private boolean validateNew(String json) {
	    Gson gson = new Gson();
	    JsonObject response = gson.fromJson(json, JsonObject.class);
	    
	    if (response.has("error")) {
	        JsonObject error = response.getAsJsonObject("error");
	        String errorMessage = error.get("message").getAsString();
	        System.out.println("Error message: " + errorMessage);
	        return false;
	    }
	    
	    return true;
	}
	

}
