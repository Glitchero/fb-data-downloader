package controller.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import controller.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;



public abstract class ConversationsHttpComponent {

	/**
	    * Abstract Methods to get the Keys and base URL's
	    * @return
	    */
	   public abstract String apiKey();
	   public abstract String baseUri();
	   
	   protected HttpComponent httpComponent;
	
	
	   public ConversationsHttpComponent(HttpComponent httpComponent){
		      this.httpComponent = httpComponent;
	   }

	   
	   public String getSecured(String uri) {
		   
		      Map<String, String> headers = new HashMap<String, String>();
		      if(!StringUtils.isEmpty(this.apiKey())) {
		    	  headers.put("Authorization",this.apiKey());
		      }
		      return httpComponent.get(uri, headers);
		}
	   
	      public HttpComponent getHttpComponent(){
		      return httpComponent;
		   }

		   public void setHttpComponent(HttpComponent httpComponent){
		      this.httpComponent = httpComponent;
		   } 
		   
		   
		   public String escape(String text) {
			      String result = text;
			      try{
			         result = URLEncoder.encode(text, "UTF-8");
			      }
			      catch (UnsupportedEncodingException e) {
			        	System.out.println("error");
			      }
			      return result;
			   }
		   
		   
		   
		   protected boolean validate(String json) {
			      try {
			         Map<String, Object> data = JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
			         }.getType());

			         if (data.containsKey("message")) {
			            return false;
			         }
			      } catch(JSONException exception){
			         return true;
			      }
			      return true;
			   }
		   
	   
}
